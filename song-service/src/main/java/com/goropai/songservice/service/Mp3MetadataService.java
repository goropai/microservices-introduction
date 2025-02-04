package com.goropai.songservice.service;

import com.goropai.songservice.entity.Mp3Metadata;
import com.goropai.songservice.entity.dto.Mp3MetadataDto;
import com.goropai.songservice.repository.Mp3MetadataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Mp3MetadataService {
    private final Mp3MetadataRepository mp3MetadataRepository;

    public Mp3MetadataService(Mp3MetadataRepository mp3MetadataRepository) {
        this.mp3MetadataRepository = mp3MetadataRepository;
    }

    public Mp3MetadataDto addMp3Metadata(Mp3MetadataDto mp3MetadataDto) {
        Mp3Metadata mp3Metadata = mp3MetadataDto.toMp3Metadata();
        Mp3Metadata savedMp3Metadata = mp3MetadataRepository.save(mp3Metadata);
        Mp3MetadataDto savedMetadataDto = new Mp3MetadataDto();
        savedMetadataDto.setId(savedMp3Metadata.getId());
        savedMetadataDto.setArtist(savedMp3Metadata.getArtist());
        savedMetadataDto.setName(savedMp3Metadata.getName());
        savedMetadataDto.setAlbum(savedMp3Metadata.getAlbum());
        savedMetadataDto.setYear(savedMp3Metadata.getYear());
        savedMetadataDto.setDuration(savedMp3Metadata.getDuration());
        return savedMetadataDto;
    }

    public boolean existsById(int id) {
        return mp3MetadataRepository.existsById(id);
    }

    public Optional<Mp3Metadata> getById(int id) {
        return mp3MetadataRepository.findById(id);
    }

    public void deleteById(Integer integer) {
        mp3MetadataRepository.deleteById(integer);
    }
}
