package com.goropai.songservice.entity;

import jakarta.persistence.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.*;

@Entity
@Table(name = "song")
public class Mp3Metadata {
    @Id
    @Column(name = "id", nullable = false)
    @NotNull(message = "The ID is invalid: it is null")
    private Integer id;

    @Column(name = "artist", length = 100)
    @NotBlank(message = "The Artist is required")
    @Size(max = 100, message = "The Artist should be max 100 characters")
    private String artist;

    @Column(name = "name", length = 100)
    @NotBlank(message = "The Name is required")
    @Size(max = 100, message = "The Name should be max 100 characters")
    private String name;

    @Column(name = "album", length = 100)
    @NotBlank(message = "The Album is required")
    @Size(max = 100, message = "The Album should be max 100 characters")
    private String album;

    @Column(name = "year")
    @NotNull(message = "The Year is required")
    @ValidYear
    private String year;

    @Column(name = "duration")
    @ValidDuration
    private String duration;

    public Mp3Metadata() {
    }

    public Mp3Metadata(Integer id, String artist, String name, String album, String year, String duration) {
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.album = album;
        this.year = year;
        this.duration = duration;
    }

    public static String getDurationFormatted(double duration) {
        Duration d = Duration.ofSeconds((long) duration);
        return String.format("%02d:%02d", d.toMinutesPart(), d.toSecondsPart());
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void validate() throws ValidationException {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Mp3Metadata>> violations = validator.validate(this);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                throw new ValidationException(errorMessage);
            }
        }
    }

    @Documented
    @Constraint(validatedBy = YearFormatValidator.class)
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidYear {
        String message() default "Invalid year format. Year must be a valid 4-digit integer in range [1900:2099]";
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
        String message() default "Invalid duration format. Duration must be greater than 0 in format mm:ss";
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