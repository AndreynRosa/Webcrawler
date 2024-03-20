package com.axreng.backend.server;

import com.axreng.backend.model.request.RequestDTO;
import com.axreng.backend.model.response.SearchRequestResponseDTO;
import com.axreng.backend.model.response.SearchResponseDTO;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface CrawlService {

    String getCrawlById(String id) throws IOException;


    String searchByKeyWord(RequestDTO request) throws IOException, InterruptedException, ExecutionException;
}
