package com.goropai.songservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "song")
public class Mp3Metadata {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "artist", length = 100)
    private String artist;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "album", length = 100)
    private String album;

    @Column(name = "year")
    private String year;

    @Column(name = "duration")
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

    /*public void validate() throws ValidationException {
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
    }*/
}