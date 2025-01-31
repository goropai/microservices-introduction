package com.goropai.resourceservice.entity.dto;

import java.util.List;

public class ResourceIdsResponse {
    private List<Integer> ids;

    public ResourceIdsResponse() {
        ids =  List.of();
    }

    public ResourceIdsResponse(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}