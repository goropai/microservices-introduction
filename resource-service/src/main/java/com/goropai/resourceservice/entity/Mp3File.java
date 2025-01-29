package com.goropai.resourceservice.entity;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.tika.Tika;

import java.lang.annotation.*;

@Entity
@Table(name = "resource", schema = "resources")
public class Mp3File {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @Min(value = 1, message = "The ID is invalid: it is negative or zero")
    @Digits(integer = 10, fraction = 0, message = "The ID is invalid: it contains decimals")
    @NotNull(message = "The ID is invalid: it is null")
    private Integer id;

    @Column(name = "data", nullable = false)
    @NotNull(message = "The MP3 file is invalid: it is null")
    @ValidMp3File
    private byte[] data;

    /*@OneToOne(mappedBy = "mp3File")
    private Mp3Metadata mp3Metadata;*/

    public Mp3File() {
    }

    public Mp3File(byte[] data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /*public Mp3Metadata getMp3Metadata() {
        return mp3Metadata;
    }

    public void setMp3Metadata(Mp3Metadata mp3Metadata) {
        this.mp3Metadata = mp3Metadata;
    }*/

}

@Documented
@Constraint(validatedBy = Mp3FileValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface ValidMp3File {
    String message() default "The provided file is not a valid MP3";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}