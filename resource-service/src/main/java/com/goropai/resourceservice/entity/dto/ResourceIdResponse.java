package com.goropai.resourceservice.entity.dto;

public class ResourceIdResponse {
    private Integer id;

    public ResourceIdResponse() {} // Important: No-args constructor for Jackson

    public ResourceIdResponse(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
