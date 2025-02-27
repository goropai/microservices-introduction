package com.goropai.resourceservice.entity;

import com.goropai.resourceservice.entity.dto.Mp3FileDto;
import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Mp3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "data", nullable = false)
    private byte[] data;

    public Mp3File() {
    }

    public Mp3File(byte[] data) {
        this.data = data;
    }

    public Mp3File(Integer id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public Mp3File(Mp3FileDto dto) {
        this.id = dto.getId();
        this.data = dto.getData();
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

    public Mp3FileDto toDto() {
        Mp3FileDto dto = new Mp3FileDto();
        dto.setId(id);
        dto.setData(data);
        return dto;
    }
}