package com.axreng.backend.model.response;

import spark.Response;

public class SearchResponseDTO extends Response {

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
