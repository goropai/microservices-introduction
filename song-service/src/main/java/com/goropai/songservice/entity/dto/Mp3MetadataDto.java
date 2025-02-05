package com.goropai.songservice.entity.dto;

import com.goropai.songservice.entity.Mp3Metadata;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;

public class Mp3MetadataDto {
    @NotNull(message = "Song ID is null")
    private Integer id;

    @NotBlank(message = "The Artist is required")
    @Size(max = 100, message = "The Artist should be up to 100 characters")
    private String artist;

    @NotBlank(message = "The Name is required")
    @Size(max = 100, message = "The Name should be up to 100 characters")
    private String name;

    @NotBlank(message = "The Album is required")
    @Size(max = 100, message = "The Album should be up to 100 characters")
    private String album;

    @NotNull(message = "The Year is required")
    @ValidYear
    private String year;

    @ValidDuration
    private String duration;

    public Mp3MetadataDto() {
    }

    public Mp3MetadataDto(Integer id, String artist, String name, String album, String year, String duration) {
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.album = album;
        this.year = year;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Mp3Metadata toMp3Metadata() {
        return new Mp3Metadata(this.id, this.artist, this.name, this.album, this.year, this.duration);
    }

    @Documented
    @Constraint(validatedBy = YearFormatValidator.class)
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidYear {
        String message() default "Year must be in YYYY format between 1900-2099";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Component
    public static class YearFormatValidator implements ConstraintValidator<ValidYear, String> {
        @Override
        public boolean isValid(String yearString, ConstraintValidatorContext context) {
            if (yearString == null || yearString.isEmpty()) {
                return false;
            }

            if (yearString.length() != 4) {
                return false;
            }

            try {
                int year = Integer.parseInt(yearString);
                return year >= 1900 && year <= 2099;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    @Documented
    @Constraint(validatedBy = DurationFormatValidator.class)
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidDuration {
        String message() default "Duration must be in mm:ss format and > 0";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Component
    public static class DurationFormatValidator implements ConstraintValidator<ValidDuration, String> {
        @Override
        public boolean isValid(String durationStr, ConstraintValidatorContext context) {
            if (durationStr == null || durationStr.isEmpty()) {
                return false;
            }

            if (!Arrays.stream(durationStr.split(":")).filter(f -> f.length() != 2).toList().isEmpty()) {
                return false;
            }

            try {
                String[] parts = durationStr.split(":");
                long minutes = Integer.parseInt(parts[0]);
                long seconds = Integer.parseInt(parts[1]);
                if (minutes >= 60 || seconds >= 60) {
                    return false;
                }
                return minutes * 60L + seconds > 0;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
