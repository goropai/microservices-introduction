package com.goropai.songservice.service;

import com.goropai.songservice.entity.Mp3Metadata;
import com.goropai.songservice.repository.Mp3MetadataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Mp3MetadataService {
    private final Mp3MetadataRepository mp3MetadataRepository;

    public Mp3MetadataService(Mp3MetadataRepository mp3MetadataRepository) {
        this.mp3MetadataRepository = mp3MetadataRepository;
    }

    public Mp3Metadata addMp3Metadata(Mp3Metadata mp3Metadata) {
        return mp3MetadataRepository.save(mp3Metadata);
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
