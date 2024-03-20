package server;

import com.axreng.backend.model.response.SearchRequestResponseDTO;
import com.axreng.backend.model.response.SearchResponseDTO;
import com.axreng.backend.server.impl.HttpClientServerImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CrawCompleteSearch {

    @Test
    public void testCrawlEndpointWith100Requests() {
        String endpointUrl = "http://localhost:4567/crawl";
        String requestBody = "{\"keyword\": \"Linux\"}";
        List<String> ids = new ArrayList<>();
        var gson = new Gson();
        HttpClientServerImpl httpClientServer = new HttpClientServerImpl();

        try {
            for (int i = 0; i < 100; i++) {
                URL url = new URL(endpointUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();
                assertEquals(200, responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                connection.disconnect();
                ids.add(gson
                        .fromJson(String.valueOf(response), SearchResponseDTO.class)
                        .getId()
                );

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ids.forEach(
                System.out::println
        );


        var t = httpClientServer.get(endpointUrl.concat("/").concat(ids.get(0)));
        var y = gson.fromJson(t, SearchRequestResponseDTO.class);
        assertNotNull(y.getUrls());
        while (y.status.equals("processing")) {
            t = httpClientServer.get(endpointUrl.concat("/").concat(ids.get(0)));
            y = gson.fromJson(t, SearchRequestResponseDTO.class);
        }
        var f = httpClientServer.get(endpointUrl.concat("/").concat(ids.get(ids.size())));
        var a = gson.fromJson(t, SearchRequestResponseDTO.class);
        while (a.status.equals("processing")) {
            f = httpClientServer.get(endpointUrl.concat("/").concat(ids.get(ids.size())));
            a = gson.fromJson(f, SearchRequestResponseDTO.class);
        }
        assertEquals(y.getUrls().size(), a.getUrls().size());
        assertEquals(100, ids.size());
    }
}

