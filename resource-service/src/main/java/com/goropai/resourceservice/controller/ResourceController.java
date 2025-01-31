package com.goropai.resourceservice.controller;

import com.goropai.resourceservice.entity.Mp3File;
import com.goropai.resourceservice.entity.dto.ResourceIdResponse;
import com.goropai.resourceservice.entity.dto.ResourceIdsResponse;
import com.goropai.resourceservice.service.MetadataService;
import com.goropai.resourceservice.service.ResourceService;
import com.goropai.songservice.entity.Mp3Metadata;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class ResourceController {
    private final ResourceService resourceService;
    private final MetadataService metadataService;

    public ResourceController(ResourceService resourceService, MetadataService metadataService) {
        this.resourceService = resourceService;
        this.metadataService = metadataService;
    }

    /**
     * Description: Uploads a new MP3 resource.
     * @param audioData Binary MP3 audio data.
     * @return the ID of successfully created resource.
     * Status codes:
     * 200 OK – Resource uploaded successfully.
     * 400 Bad Request – The request body is invalid MP3.
     * 500 Internal Server Error – An error occurred on the server.
     */
    @PostMapping(path = "/resources", consumes = "audio/mpeg")
    @Transactional
    public Mono<ResponseEntity<ResourceIdResponse>> uploadAudio(@RequestBody byte[] audioData) {
        return Mono.fromCallable(() -> resourceService.save(audioData))
                .onErrorResume(PersistenceException.class, ex -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)))
                .flatMap(savedFile ->
                        {
                            try {
                                return metadataService.parseAndSave(savedFile.getId(), savedFile.getData())
                                        .onErrorResume(
                                                ex -> {
                                                    try {
                                                        resourceService.deleteById(savedFile.getId());
                                                    }
                                                    catch (Exception e) {
                                                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                                                    }
                                                    if (ex instanceof WebClientResponseException) {
                                                        return Mono.error(ex);
                                                    }
                                                    else {
                                                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                                                    }
                                                }
                                        );
                            } catch (IOException e) {
                                return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                            }
                        }
                )
                .map(success -> ResponseEntity.status(HttpStatus.OK).body(
                        new ResourceIdResponse(Optional.ofNullable(success.getBody())
                                .map(Mp3Metadata::getId).orElse(null))))
                .onErrorResume(ResponseStatusException.class, ex -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    /**
     * Retrieves the binary audio data of a resource.
     * @param id The ID of the resource to retrieve.
     * @return Returns the audio bytes (MP3 file) for the specified resource.
     * Status codes:
     * 200 OK – Resource retrieved successfully.
     * 400 Bad Request – The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).
     * 404 Not Found – Resource with the specified ID does not exist.
     * 500 Internal Server Error – An error occurred on the server.
     */
    @GetMapping(path = "/resources/{id}")
    public ResponseEntity<byte[]> getAudio(@PathVariable @Validated int id) {
        try {
            Optional<Mp3File> found = resourceService.getById(id);
            return found.map(m -> ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.parseMediaType("audio/mpeg"))
                            .body(m.getData()))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"404\": \"Resource not found\"}".getBytes()));
        }
        catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(e.getBindingResult()
                .getFieldError()).getDefaultMessage());
    }

    /**
     * Deletes specified resources by their IDs. If a resource does not exist, it is ignored without causing an error.
     * @param ids Comma-separated list of resource IDs to remove.
     * @return Returns an array of the IDs of successfully deleted resources.
     * Status codes:
     * 200 OK – Request successful, resources deleted as specified.
     * 400 Bad Request – CSV string format is invalid or exceeds length restrictions.
     * 500 Internal Server Error – An error occurred on the server.
     */
    @DeleteMapping(path = "/resources")
    @Transactional
    public ResponseEntity<ResourceIdsResponse> deleteAudio(@RequestParam(name = "id") String ids) {
        if (ids.length() <= 0 || ids.length() > 200) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Integer> correctIds = Stream.of(ids.split(","))
                .map(Integer::valueOf)
                .filter(resourceService::existsById)
                .toList();
        if (correctIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResourceIdsResponse());
        }
        try {
            correctIds.forEach(resourceService::deleteById);
            metadataService.deleteMetadata(correctIds).subscribe();
        }
        catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResourceIdsResponse(correctIds));
    }
}
