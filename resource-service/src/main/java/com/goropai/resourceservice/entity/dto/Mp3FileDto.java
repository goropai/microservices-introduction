package com.goropai.resourceservice.entity.dto;

import com.goropai.resourceservice.entity.Mp3FileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

public class Mp3FileDto {
    @Min(value = 1, message = "Resource ID should be > 0")
    @Digits(integer = 10, fraction = 0, message = "Resource ID shouldn't contain decimals")
    private Integer id;

    @NotNull(message = "Resource is absent")
    @ValidMp3File
    private byte[] data;

    public Mp3FileDto() {
    }

    public Mp3FileDto(byte[] data) {
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

    @Documented
    @Constraint(validatedBy = Mp3FileValidator.class)
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidMp3File {
        String message() default "Resource should be of audio/mpeg type";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
