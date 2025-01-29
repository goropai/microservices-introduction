package com.goropai.resourceservice.service;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class MetadataService {
    private final WebClient webClient;

    public MetadataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    /*public Mono<ResponseEntity<Mp3Metadata>> parseAndSave(int id, byte[] data) {
        try (InputStream input = new ByteArrayInputStream(data)) {
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            Parser parser = new AutoDetectParser();
            ParseContext parseCtx = new ParseContext();

            parser.parse(input, handler, metadata, parseCtx);

            return webClient.post().uri(uriBuilder ->
                    uriBuilder.path("/songs")
                            .queryParam("id", id)
                            .queryParam("name", metadata.get("title"))
                            .queryParam("artist", metadata.get("xmpDM:Artist"))
                            .queryParam("album", metadata.get("xmpDM:Album"))
                            .queryParam("duration", metadata.get("xmpDM:Duration"))
                            .queryParam("year", metadata.get("dcterms:created"))
                            .build()
            ).accept(MediaType.APPLICATION_JSON).retrieve().toEntity(Mp3Metadata.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/
}
