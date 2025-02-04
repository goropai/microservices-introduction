package com.goropai.songservice.entity.dto;

import com.goropai.songservice.entity.Mp3Metadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Mp3MetadataDto {
    @NotNull(message = "The ID is invalid: it is null")
    private Integer id;

    @NotBlank(message = "The Artist is required")
    @Size(max = 100, message = "The Artist should be max 100 characters")
    private String artist;

    @NotBlank(message = "The Name is required")
    @Size(max = 100, message = "The Name should be max 100 characters")
    private String name;

    @NotBlank(message = "The Album is required")
    @Size(max = 100, message = "The Album should be max 100 characters")
    private String album;

    @NotNull(message = "The Year is required")
    @Mp3Metadata.ValidYear
    private String year;

    @Mp3Metadata.ValidDuration
    private String duration;

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
}
