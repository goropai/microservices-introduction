package com.goropai.songservice.controller;

import com.goropai.songservice.entity.Mp3Metadata;
import com.goropai.songservice.entity.dto.SongIdsResponse;
import com.goropai.songservice.service.Mp3MetadataService;
import com.goropai.songservice.service.exception.CsvValidationException;
import com.goropai.songservice.service.exception.MetadataAlreadyExistException;
import com.goropai.songservice.service.exception.MetadataNotFoundException;
import jakarta.transaction.Transactional;
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
    public ResponseEntity<Mp3Metadata> save(@RequestBody Mp3Metadata metadata) {
        if (mp3MetadataService.existsById(metadata.getId())) {
            throw new MetadataAlreadyExistException(metadata.getId());
        }
        metadata.validate();
        return ResponseEntity.status(HttpStatus.OK).body(mp3MetadataService.addMp3Metadata(metadata));
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
        Optional<Mp3Metadata> found = mp3MetadataService.getById(id);
        return found.map(m -> ResponseEntity.status(HttpStatus.OK).body(m))
                .orElseThrow(() -> new MetadataNotFoundException(id));
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
    public ResponseEntity<SongIdsResponse> deleteById(@RequestParam(name = "id") String ids) {
        if (ids.isEmpty() || ids.length() > 200) {
            throw new CsvValidationException("CSV string length is out of bounds [0, 200]");
        }
        List<Integer> correctIds;
        try {
            correctIds = Stream.of(ids.split(","))
                    .map(Integer::valueOf)
                    .filter(mp3MetadataService::existsById)
                    .toList();
        }
        catch (NumberFormatException e) {
            throw new CsvValidationException("CSV contains invalid characters");
        }
        correctIds.forEach(mp3MetadataService::deleteById);
        return ResponseEntity.status(HttpStatus.OK).body(new SongIdsResponse(correctIds));
    }
}
