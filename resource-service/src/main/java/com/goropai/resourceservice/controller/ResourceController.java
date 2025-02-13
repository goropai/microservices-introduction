package com.goropai.resourceservice.controller;

import com.goropai.resourceservice.entity.dto.*;
import com.goropai.resourceservice.service.MetadataService;
import com.goropai.resourceservice.service.ResourceService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ResourceIdResponse> uploadAudio(@RequestBody byte[] audioData) throws IOException {
        Mp3FileDto savedFile = resourceService.save(new Mp3FileDto(audioData));
        Mp3MetadataDto result = metadataService.parseAndSave(savedFile);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResourceIdResponse(Optional.ofNullable(result).map(Mp3MetadataDto::getId).orElse(null)));
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
    public ResponseEntity<byte[]> getAudio(@PathVariable @Min(value = 1, message = "Resource ID should be > 0")
                                               @Valid Integer id) {
        Mp3FileDto found = resourceService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.parseMediaType("audio/mpeg"))
                        .body(found.getData());
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
        List<Integer> correctIds = resourceService.deleteByIds(ids);
        SongIdsResponse response = metadataService.deleteMetadata(correctIds);
        return ResponseEntity.status(HttpStatus.OK).body(new ResourceIdsResponse(response.getIds()));
    }
}
