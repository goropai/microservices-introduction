package com.goropai.resourceservice.service;

import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataService {
    private final WebClient webClient;

    public MetadataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    @Transactional
    public Mono<ResponseEntity<Mp3Metadata>> parseAndSave(int id, byte[] data) throws IOException {
        try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
            Tika tika = new Tika();
            Metadata metadata = new Metadata();

            tika.parse(input, metadata);

            return webClient.method(HttpMethod.POST).uri("/songs")
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .bodyValue(new Mp3Metadata(id,
                            metadata.get("dc:creator"),
                            metadata.get("dc:title"),
                            metadata.get("xmpDM:album"),
                            metadata.get("xmpDM:releaseDate"),
                            Mp3Metadata.getDurationFormatted(Double.parseDouble(metadata.get("xmpDM:duration")))))
                    .retrieve().toEntity(Mp3Metadata.class);
        }
    }

    @Transactional
    public Mono<ResponseEntity<List<Integer>>> deleteMetadata(List<Integer> ids) {
        return webClient.method(HttpMethod.DELETE).uri(uriBuilder ->
                        uriBuilder.path("/songs")
                                .queryParam("id", convertListToString(ids))
                                .build())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .retrieve().toEntityList(Integer.class);
    }

    private String convertListToString(List<Integer> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
