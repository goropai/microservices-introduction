package com.goropai.resourceservice.service;

import com.goropai.resourceservice.entity.dto.Mp3FileDto;
import com.goropai.resourceservice.entity.dto.Mp3MetadataDto;
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
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataService {
    private final WebClient webClient;

    public MetadataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    @Transactional
    public Mono<Mp3MetadataDto> parseAndSave(Mp3FileDto mp3FileDto) throws IOException {
        try (ByteArrayInputStream input = new ByteArrayInputStream(mp3FileDto.getData())) {
            Tika tika = new Tika();
            Metadata metadata = new Metadata();
            tika.parse(input, metadata);
            return webClient.method(HttpMethod.POST).uri("/songs")
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .bodyValue(new Mp3MetadataDto(mp3FileDto.getId(),
                            metadata.get("dc:creator"),
                            metadata.get("dc:title"),
                            metadata.get("xmpDM:album"),
                            metadata.get("xmpDM:releaseDate"),
                            getDurationFormatted(Double.parseDouble(metadata.get("xmpDM:duration")))))
                    .retrieve().bodyToMono(Mp3MetadataDto.class);
        }
    }

    public static String getDurationFormatted(double duration) {
        Duration d = Duration.ofSeconds((long) duration);
        return String.format("%02d:%02d", d.toMinutesPart(), d.toSecondsPart());
    }

    @Transactional
    public Mono<ResponseEntity<List<Integer>>> deleteMetadata(List<Integer> ids) {
        if (ids.isEmpty()) {
            return Mono.empty();
        }
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
