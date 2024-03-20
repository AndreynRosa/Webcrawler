package com.axreng.backend;

import com.axreng.backend.controller.CrawlController;
import com.axreng.backend.server.AsyncSearchKeyWord;
import com.axreng.backend.server.CrawlService;
import com.axreng.backend.server.HttpClientServer;
import com.axreng.backend.server.SearchStringEnginieeService;
import com.axreng.backend.server.impl.AsyncSearchKeyWordImpl;
import com.axreng.backend.server.impl.CrawlServiceImpl;
import com.axreng.backend.server.impl.HttpClientServerImpl;
import com.axreng.backend.server.impl.SearchStringEngineServiceImpl;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(4567);
        SearchStringEnginieeService searchStringEnginieeService =  new SearchStringEngineServiceImpl();
        HttpClientServer httpClientServer = new HttpClientServerImpl();

        AsyncSearchKeyWord asyncSearchKeyWord = new AsyncSearchKeyWordImpl(httpClientServer, searchStringEnginieeService);

        CrawlService crawlService = new CrawlServiceImpl(asyncSearchKeyWord);
        CrawlController crawlController = new CrawlController(crawlService);
    }
}
