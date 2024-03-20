package com.axreng.backend.server.impl;

import com.axreng.backend.server.HttpClientServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientServerImpl implements HttpClientServer {

    private static final Logger logger = LoggerFactory.getLogger(AsyncSearchKeyWordImpl.class);
    @Override
    public String get(String url)  {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder responseBody = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(1000);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
        }

        catch (IOException e) {
            logger.error("error to get: ".concat(url).concat(" ").concat( "\n Error ").concat(e.getMessage()));
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("error read body in link: ".concat(url).concat(" ").concat(e.getMessage()));
                    return null;
                }
            }
        }
        return responseBody.toString();
    }
}

