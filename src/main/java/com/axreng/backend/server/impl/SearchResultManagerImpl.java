package com.axreng.backend.server.impl;

import com.axreng.backend.model.response.SearchRequestResponseDTO;
import com.axreng.backend.server.SearchResultManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.axreng.backend.server.impl.SearchResultManagerFileManager.saveResultsToFile;
import static com.axreng.backend.utils.UniqueIdGeneratorUtils.generateUniqueId;
import static java.util.Objects.isNull;

public class SearchResultManagerImpl implements SearchResultManager {

    private static Map<String, SearchRequestResponseDTO> results = new ConcurrentHashMap<>();
    public String createInitialSearchProcess() {
        var id = generateUniqueId();
        var searchRootPoint = new SearchRequestResponseDTO(id);
        results.put(id, searchRootPoint);
        return id;
    }
    public SearchRequestResponseDTO getResult(String id) {
            var currenmtResult = results.get(id);
            if(isNull(currenmtResult)){
                currenmtResult = getSynchronizedResult(id);
            }
            return currenmtResult;
    }
    public synchronized void moveToDone(String id, SearchRequestResponseDTO requestResponse){
        saveResultsToFile(id, requestResponse);
        results.remove(id);
    }
    public synchronized SearchRequestResponseDTO getSynchronizedResult(String id){
        results = SearchResultManagerFileManager.loadResultsFromFile();
        return results.get(id);
    }
    public void addLinkById(String id, String link) {
        SearchRequestResponseDTO result = results.get(id);
        if (!result.getUrls().contains(link)) {
            result.getUrls().add(link);
        }
    }
}


