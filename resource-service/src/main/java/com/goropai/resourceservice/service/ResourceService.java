package com.goropai.resourceservice.service;

import com.goropai.resourceservice.entity.Mp3File;
import com.goropai.resourceservice.entity.dto.Mp3FileDto;
import com.goropai.resourceservice.repository.ResourceRepository;
import com.goropai.resourceservice.service.exceptions.CsvValidationException;
import com.goropai.resourceservice.service.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final Validator validator;

    public ResourceService(ResourceRepository resourceRepository, Validator validator) {
        this.resourceRepository = resourceRepository;
        this.validator = validator;
    }

    public Mp3FileDto save(Mp3FileDto mp3FileDto) {
        Set<ConstraintViolation<Mp3FileDto>> violations = validator.validate(mp3FileDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return resourceRepository.save(new Mp3File(mp3FileDto)).toDto();
    }

    public Mp3FileDto getById(int id) {
        return resourceRepository.findById(id).map(Mp3File::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public boolean existsById(int id) {
        return resourceRepository.existsById(id);
    }

    public List<Integer> deleteByIds(String ids) {
        if (ids.isEmpty() || ids.length() > 200) {
            throw new CsvValidationException("CSV string length is out of bounds [0, 200]");
        }
        List<Integer> correctIds;
        try {
            correctIds = Stream.of(ids.split(","))
                    .map(Integer::valueOf)
                    .filter(this::existsById)
                    .toList();
        }
        catch (NumberFormatException e) {
            throw new CsvValidationException("CSV should be a comma-separated list of integers");
        }
        if (!correctIds.isEmpty()) {
            correctIds.forEach(this::deleteById);
        }
        return correctIds;
    }

    public void deleteById(int id) {
        resourceRepository.deleteById(id);
    }
}