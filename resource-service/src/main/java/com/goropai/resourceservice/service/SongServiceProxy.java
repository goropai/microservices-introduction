package com.goropai.resourceservice.service;

import com.goropai.resourceservice.entity.dto.Mp3MetadataDto;
import com.goropai.resourceservice.entity.dto.SongIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "song-service")
public interface SongServiceProxy {

    @PostMapping(path = "/songs")
    ResponseEntity<Mp3MetadataDto> save(@RequestBody Mp3MetadataDto metadataDto);

    @DeleteMapping(path = "/songs")
    ResponseEntity<SongIdsResponse> deleteById(@RequestParam(name = "id") String ids);
}
