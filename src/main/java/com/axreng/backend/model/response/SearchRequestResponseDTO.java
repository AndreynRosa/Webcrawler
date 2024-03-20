package com.axreng.backend.model.response;

import java.util.ArrayList;
import java.util.List;

public class SearchRequestResponseDTO {

    public String id;
    public String status;

    public List<String> urls;

    public SearchRequestResponseDTO(String id) {
        this.id = id;
        this.status = "active";
        this.urls = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
