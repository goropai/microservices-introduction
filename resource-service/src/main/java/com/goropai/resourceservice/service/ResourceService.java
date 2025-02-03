package com.goropai.resourceservice.service;

import com.goropai.resourceservice.entity.Mp3File;
import com.goropai.resourceservice.repository.ResourceRepository;
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

    public Optional<Mp3File> getById(int id) {
        return resourceRepository.findById(id);
    }

    public boolean existsById(int id) {
        return resourceRepository.existsById(id);
    }

    public void deleteById(int id) {
        resourceRepository.deleteById(id);
    }
}