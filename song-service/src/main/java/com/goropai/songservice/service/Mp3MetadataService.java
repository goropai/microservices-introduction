package com.goropai.songservice.service;

import com.goropai.songservice.entity.Mp3Metadata;
import com.goropai.songservice.entity.dto.Mp3MetadataDto;
import com.goropai.songservice.repository.Mp3MetadataRepository;
import com.goropai.songservice.service.exception.MetadataAlreadyExistException;
import com.goropai.songservice.service.exception.MetadataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Mp3MetadataService {
    private final Mp3MetadataRepository mp3MetadataRepository;

    public Mp3MetadataService(Mp3MetadataRepository mp3MetadataRepository) {
        this.mp3MetadataRepository = mp3MetadataRepository;
    }

    public Mp3MetadataDto addMp3Metadata(Mp3MetadataDto mp3MetadataDto) {
        if (existsById(mp3MetadataDto.getId())) {
            throw new MetadataAlreadyExistException(mp3MetadataDto.getId());
        }
        Mp3Metadata mp3Metadata = mp3MetadataDto.toMp3Metadata();
        Mp3Metadata savedMp3Metadata = mp3MetadataRepository.save(mp3Metadata);
        return getMp3MetadataDto(savedMp3Metadata);
    }

    public boolean existsById(int id) {
        return mp3MetadataRepository.existsById(id);
    }

    public Mp3MetadataDto getById(int id) {
        Optional<Mp3Metadata> savedMp3Metadata = mp3MetadataRepository.findById(id);
        return savedMp3Metadata.map(this::getMp3MetadataDto).orElseThrow(() -> new MetadataNotFoundException(id));
    }

    private Mp3MetadataDto getMp3MetadataDto(Mp3Metadata savedMp3Metadata) {
        Mp3MetadataDto savedMetadataDto = new Mp3MetadataDto();
        savedMetadataDto.setId(savedMp3Metadata.getId());
        savedMetadataDto.setArtist(savedMp3Metadata.getArtist());
        savedMetadataDto.setName(savedMp3Metadata.getName());
        savedMetadataDto.setAlbum(savedMp3Metadata.getAlbum());
        savedMetadataDto.setYear(savedMp3Metadata.getYear());
        savedMetadataDto.setDuration(savedMp3Metadata.getDuration());
        return savedMetadataDto;
    }

    public void deleteById(Integer integer) {
        mp3MetadataRepository.deleteById(integer);
    }
}
