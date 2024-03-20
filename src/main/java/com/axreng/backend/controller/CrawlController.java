package com.axreng.backend.controller;

import com.axreng.backend.model.request.RequestDTO;
import com.axreng.backend.server.CrawlService;
import com.axreng.backend.server.impl.CrawlServiceImpl;
import com.google.gson.Gson;

import static java.util.Objects.isNull;
import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlController {
    private final CrawlService crawlService;
    private Gson gson = new Gson();
    public CrawlController(CrawlService crawlService) {
        this.crawlService = crawlService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        get("/crawl/:id", (req, res) -> {
            res.type("application/json");
String id = req.params("id");
            if (isNull(req.params("id")) || id.length() != 8) {
                res.status(400);
                return "A id de busca deve ter 8 caracteres alfanumericos";
            }
            return crawlService.getCrawlById(req.params("id"));
        });

        post("/crawl", (req, res) -> {
            res.type("application/json");
            RequestDTO requestDTO = gson.fromJson(req.body(), RequestDTO.class);
            var keyWord = requestDTO.getKeyword();
            if (isNull(keyWord) || keyWord.length() < 4 || keyWord.length() > 32) {
                res.status(400);
                return "A keyword deve ter entre 4 e 32 caracteres";
            }
            return crawlService.searchByKeyWord(requestDTO);
        });
    }
}
