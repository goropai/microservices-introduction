package com.goropai.resourceservice.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;

public class Mp3FileValidator implements ConstraintValidator<ValidMp3File, byte[]> {

    private final Tika tika;

    public Mp3FileValidator() {  // The crucial no-argument constructor
        // You can initialize Tika here if you want it to be created only once:
        tika = new Tika();
    }

    public boolean isValid(byte[] mp3Data, ConstraintValidatorContext context) {
        if (mp3Data == null || mp3Data.length == 0) { // Check for null or empty array
            return true; // Or false, depending on your requirements for empty files
        }
        try {
            String detectedType = tika.detect(mp3Data);
            return "audio/mpeg".equals(detectedType);
        } catch (Exception e) {
            // Handle Tika exceptions (e.g., if Tika can't analyze the data)
            e.printStackTrace(); // Or better logging/error handling
            return false; // Or true, depending on how you want to handle errors
        }
    }
}
