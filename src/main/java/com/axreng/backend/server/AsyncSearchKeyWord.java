package com.axreng.backend.server;

import com.axreng.backend.model.request.RequestDTO;
import com.axreng.backend.server.impl.SearchResultManagerImpl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface AsyncSearchKeyWord {
    void asyncSearch(  RequestDTO requestDTO) throws IOException, InterruptedException, ExecutionException;
}
