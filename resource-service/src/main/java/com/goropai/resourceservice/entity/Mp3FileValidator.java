package com.goropai.resourceservice.entity;

import com.goropai.resourceservice.entity.dto.Mp3FileDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;

public class Mp3FileValidator implements ConstraintValidator<Mp3FileDto.ValidMp3File, byte[]> {

    private final Tika tika;

    public Mp3FileValidator() {
        tika = new Tika();
    }

    public boolean isValid(byte[] mp3Data, ConstraintValidatorContext context) {
        if (mp3Data == null || mp3Data.length == 0) {
            return false;
        }
        try {
            String detectedType = tika.detect(mp3Data);
            return "audio/mpeg".equals(detectedType);
        } catch (Exception e) {
            return false;
        }
    }
}
