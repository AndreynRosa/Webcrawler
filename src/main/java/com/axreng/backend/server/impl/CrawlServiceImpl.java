package com.axreng.backend.server.impl;

import com.axreng.backend.model.request.RequestDTO;
import com.axreng.backend.model.response.SearchResponseDTO;
import com.axreng.backend.server.AsyncSearchKeyWord;
import com.axreng.backend.server.CrawlService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
public class CrawlServiceImpl implements CrawlService {
    private AsyncSearchKeyWord asyncSearchKeyWord;
    private Gson gson = new Gson();
    private SearchResultManagerImpl searchResultManager;

    public CrawlServiceImpl(AsyncSearchKeyWord asyncSearchKeyWord) {
        this.asyncSearchKeyWord = asyncSearchKeyWord;
        this.searchResultManager = new SearchResultManagerImpl();
    }
    @Override
    public String getCrawlById(String id)  {
        return gson.toJson(searchResultManager.getResult(id));
    }

    @Override
    public String searchByKeyWord(RequestDTO requestDTO) throws IOException, InterruptedException, ExecutionException {
        var rootSearchPointId = searchResultManager.createInitialSearchProcess();
        SearchResponseDTO response = new SearchResponseDTO();
        response.setId(rootSearchPointId);
        requestDTO.setId(rootSearchPointId);
        asyncSearchKeyWord.asyncSearch(requestDTO);
        return gson.toJson(response);
    }
}
