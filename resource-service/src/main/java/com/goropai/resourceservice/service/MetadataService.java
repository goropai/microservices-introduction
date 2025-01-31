package com.goropai.resourceservice.service;

import com.goropai.songservice.entity.Mp3Metadata;
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
import java.util.Collections;
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

            return webClient.method(HttpMethod.POST).uri(uriBuilder ->
                    uriBuilder.path("/songs")
                            .queryParam("id", id)
                            .queryParam("name", metadata.get("dc:title"))
                            .queryParam("artist", metadata.get("dc:creator"))
                            .queryParam("album", metadata.get("xmpDM:album"))
                            .queryParam("duration", metadata.get("xmpDM:duration"))
                            .queryParam("year", metadata.get("xmpDM:releaseDate"))
                            .build()
            ).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
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
