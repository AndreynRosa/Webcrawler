package com.axreng.backend.server.impl;

import com.axreng.backend.model.response.SearchRequestResponseDTO;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SearchResultManagerFileManager {
    private static final String FILENAME = "results.ser";
    public static synchronized void saveResultsToFile(String id, SearchRequestResponseDTO requestResponse) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            var results = new HashMap<String,SearchRequestResponseDTO >();
            results.put(id, requestResponse);
            objectOutputStream.writeObject(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized static ConcurrentHashMap<String, SearchRequestResponseDTO> loadResultsFromFile() {
        ConcurrentHashMap<String, SearchRequestResponseDTO> results = new ConcurrentHashMap<>();
        try (FileInputStream fileInputStream = new FileInputStream(FILENAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            results = (ConcurrentHashMap<String, SearchRequestResponseDTO>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }
}
