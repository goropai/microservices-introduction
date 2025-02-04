package com.goropai.resourceservice.service;

import com.goropai.resourceservice.entity.Mp3File;
import com.goropai.resourceservice.entity.dto.Mp3FileDto;
import com.goropai.resourceservice.repository.ResourceRepository;
import com.goropai.resourceservice.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Mp3File save(byte[] data) {
        return resourceRepository.save(new Mp3File(data));
    }

    public Mp3FileDto getById(int id) {
        return resourceRepository.findById(id).map(Mp3File::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public boolean existsById(int id) {
        return resourceRepository.existsById(id);
    }

    public void deleteById(int id) {
        resourceRepository.deleteById(id);
    }
}