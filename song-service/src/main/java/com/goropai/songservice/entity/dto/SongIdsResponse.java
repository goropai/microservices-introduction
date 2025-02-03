package com.goropai.songservice.entity.dto;

import java.util.List;

public class SongIdsResponse {
    private List<Integer> ids;

    public SongIdsResponse() {
        ids =  List.of();
    }

    public SongIdsResponse(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
