package com.goropai.songservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.Duration;

@Entity
@Table(name = "song", schema = "songs")
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
    @Min(value = 1900, message = "The Year should not be less than 1900")
    @Max(value = 2099, message = "The Year should not be more than 2099")
    private Integer year;

    @Column(name = "duration")
    @NotNull(message = "The Duration is required")
    @JsonIgnore
    private double duration;

    @Transient
    @JsonProperty("duration")
    public String getDurationFormatted() {
        Duration duration = Duration.ofSeconds((long) this.duration);
        return String.format("%02d:%02d", duration.toMinutesPart(), duration.toSecondsPart());
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    /*public Mp3File getMp3File() {
        return mp3File;
    }

    public void setMp3File(Mp3File mp3File) {
        this.mp3File = mp3File;
    }*/
}