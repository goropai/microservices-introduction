package com.goropai.resourceservice.entity.dto;

public class Mp3MetadataDto {
    private Integer id;

    private String artist;

    private String name;

    private String album;

    private String year;

    private String duration;

    public Mp3MetadataDto(int id, String artist, String name, String album, String year, String durationFormatted) {
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.album = album;
        this.year = year;
        this.duration = durationFormatted;
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
}
