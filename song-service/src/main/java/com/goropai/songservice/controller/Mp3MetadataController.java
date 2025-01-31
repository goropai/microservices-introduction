package com.goropai.songservice.controller;

import com.goropai.songservice.entity.Mp3Metadata;
import com.goropai.songservice.service.Mp3MetadataService;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class Mp3MetadataController {
    private final Mp3MetadataService mp3MetadataService;

    public Mp3MetadataController(final Mp3MetadataService mp3MetadataService) {
        this.mp3MetadataService = mp3MetadataService;
    }

    /**
     * Creates a new song metadata record in the database.
     * @param id Numeric string, must match an existing Resource ID.
     * @param name 1-100 characters text.
     * @param artist 1-100 characters text.
     * @param album 1-100 characters text.
     * @param duration Format mm:ss, with leading zeros.
     * @param year YYYY format between 1900-2099.
     * @return Returns the ID of the successfully created metadata record (should match the Resource ID).
     * Status codes:
     * 200 OK – Metadata created successfully.
     * 400 Bad Request – Song metadata is missing or contains errors.
     * 409 Conflict – Metadata for this ID already exists.
     * 500 Internal Server Error – An error occurred on the server.
     */
    @PostMapping(path = "/songs")
    public ResponseEntity<Mp3Metadata> save(@RequestParam final int id,
                                            @RequestParam final String name,
                                            @RequestParam final String artist,
                                            @RequestParam final String album,
                                            @RequestParam final String duration,
                                            @RequestParam final String year) {
        if (mp3MetadataService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Mp3Metadata metadata = new Mp3Metadata();
        metadata.setId(id);
        metadata.setName(name);
        metadata.setArtist(artist);
        metadata.setAlbum(album);
        metadata.setDuration(Double.parseDouble(duration));
        metadata.setYear(Integer.valueOf(year));

        try {
            return ResponseEntity.status(HttpStatus.OK).body(mp3MetadataService.addMp3Metadata(metadata));
        }
        catch (Exception e) {
            Throwable rootCause = findRootCause(e);
            if (rootCause instanceof ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Throwable findRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * Get song metadata by ID.
     * @param id ID of the metadata to retrieve.
     * @return
     * Status codes:
     * 200 OK – Metadata retrieved successfully.
     * 400 Bad Request – The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).
     * 404 Not Found – Song metadata with the specified ID does not exist.
     * 500 Internal Server Error – An error occurred on the server.
     */
    @GetMapping(path = "/songs/{id}")
    public ResponseEntity<Mp3Metadata> getById(@PathVariable @Validated final int id) {
        try {
            Optional<Mp3Metadata> found = mp3MetadataService.getById(id);
            return found.map(m -> ResponseEntity.status(HttpStatus.OK).body(m))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Deletes specified song metadata records by their IDs. If a metadata record does not exist, it is ignored
     * without causing an error.
     * @param ids Comma-separated list of metadata IDs to remove.
     * @return Returns an array of the IDs of successfully deleted metadata records.
     * Status codes:
     * 200 OK – Request successful, metadata records deleted as specified.
     * 400 Bad Request – CSV string format is invalid or exceeds length restrictions.
     * 500 Internal Server Error – An error occurred on the server.
     */
    @DeleteMapping(path = "/songs")
    @Transactional
    public ResponseEntity<List<Integer>> deleteById(@RequestParam(name = "id") String ids) {
        if (ids.length() <= 0 || ids.length() > 200) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Integer> correctIds = Stream.of(ids.split(","))
                .map(Integer::valueOf)
                .filter(mp3MetadataService::existsById)
                .toList();
        if (correctIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            correctIds.forEach(mp3MetadataService::deleteById);
        }
        catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(correctIds);
    }
}
