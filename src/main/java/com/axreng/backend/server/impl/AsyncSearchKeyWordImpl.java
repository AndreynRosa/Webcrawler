package com.axreng.backend.server.impl;

import com.axreng.backend.model.request.RequestDTO;
import com.axreng.backend.model.response.SearchRequestResponseDTO;
import com.axreng.backend.server.AsyncSearchKeyWord;
import com.axreng.backend.server.HttpClientServer;
import com.axreng.backend.server.SearchStringEnginieeService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

import org.slf4j.Logger;

import static com.axreng.backend.utils.HtmlHandlerUtils.htmlExtractLinks;
import static com.axreng.backend.utils.PropertiesUtils.getProperties;


public class AsyncSearchKeyWordImpl implements AsyncSearchKeyWord {
    public static final int MAX_THREADS = 30;
    private HttpClientServer httpClientServer;
    private SearchStringEnginieeService searchStringEnginieeService;
    private static final Logger logger = LoggerFactory.getLogger(AsyncSearchKeyWordImpl.class);
    private Instant initialTime;
    private ExecutorService executor;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ConcurrentLinkedQueue<String> linksWillBeProcessd;
    private BlockingQueue<RequestDTO> requestsQueue;
    private SearchResultManagerImpl searchResultManager;

    public AsyncSearchKeyWordImpl(HttpClientServer httpClientServer, SearchStringEnginieeService searchStringEnginieeService) {
        this.httpClientServer = httpClientServer;
        this.searchStringEnginieeService = searchStringEnginieeService;
        this.searchResultManager = new SearchResultManagerImpl();
        this.requestsQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void asyncSearch(RequestDTO request) {
        requestsQueue.offer(request);
        scheduleNextRequest();
    }

    private void scheduleNextRequest() {
        if (!requestsQueue.isEmpty()) {
            scheduler.schedule(this::processNextRequest, 0, TimeUnit.SECONDS);
        }
    }

    private void processNextRequest() {
        try {
            initialTime = Instant.now();
            RequestDTO request = requestsQueue.poll();
            processRequest(request);
        } catch (Exception e) {
            logger.error("Error processing request: " + e.getMessage());
        }
    }

    private void processRequest(RequestDTO request) {
        try {
            executor = Executors.newFixedThreadPool(MAX_THREADS);
            int[] lps = searchStringEnginieeService.computeLPSArray(request.getKeyword());
            StringBuilder htmlFirstPage = new StringBuilder();
            String rootLink = getProperties();
            htmlFirstPage.append(getHtmlResponse(rootLink));
            linksWillBeProcessd = new ConcurrentLinkedQueue<>();
            var urls = extractLinks(htmlFirstPage.toString(), linksWillBeProcessd);
            searchStringEnginieeService.searchAnyMatch(htmlFirstPage.toString(), request.getKeyword(), request.getId(), rootLink, lps);
            webCrawlerRecursiveSearch(request, lps, urls);
        } catch (Exception e) {
            logger.error("Error processing request: " + e.getMessage());
        } finally {
            scheduleNextRequest();
        }
    }

    private void webCrawlerRecursiveSearch(RequestDTO request, int[] lps, List<String> urls) {
        HashMap<String, String> linksAndHtml = asyncHttpSeach(urls);
        var nextHtmlToProcess = new ArrayList<String>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        linksAndHtml.forEach((link, html) -> {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                try {
                    nextHtmlToProcess.addAll(extractLinks(html, linksWillBeProcessd));
                    searchStringEnginieeService.searchAnyMatch(html, request.getKeyword(), request.getId(), link, lps);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            futures.add(future);
        });

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.exceptionally(e -> {
            logger.error("Error to complete async search any match: ".concat(e.getMessage()));
            return null;
        });
        allOf.join();

        if (!nextHtmlToProcess.isEmpty()) {
            webCrawlerRecursiveSearch(request, lps, nextHtmlToProcess);
        } else {
            doneSearchResult(request);
        }
    }

    private void doneSearchResult(RequestDTO request) {
        SearchRequestResponseDTO searchResult = this.searchResultManager.getResult(request.getId());
        searchResult.setStatus("done");
        var end = Instant.now();
        Duration duration = Duration.between(initialTime, end);
        this.searchResultManager.moveToDone(request.getId(), searchResult);
        var r = duration.toMillis() / 1000l;
        logger.info("Finish process whit ".concat(String.valueOf(r)).concat(" seconds")
                .concat(" keyWord: ")
                .concat(request.getKeyword())
                .concat(" whit id: ")
                .concat(request.getId()));
        executor.shutdown();
    }

    private HashMap<String, String> asyncHttpSeach(List<String> a) {
        List<Future<HashMap<String, String>>> futures = new ArrayList<>();
        HashMap<String, String> htmlsToProcess = new HashMap<>();
        a.forEach(link -> {
            Callable<HashMap<String, String>> callable = () -> {
                try {
                    if (linksWillBeProcessd.contains(link)) {
                        var currenthml = httpClientServer.get(link);
                        if (!Objects.isNull(currenthml) && !currenthml.isBlank()) {
                            htmlsToProcess.put(link, currenthml);
                        }
                    }
                } catch (IOException e) {
                    return null;
                }
                return htmlsToProcess;
            };
            Future<HashMap<String, String>> future = executor.submit(callable);
            futures.add(future);

        });

        for (Future<HashMap<String, String>> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return htmlsToProcess;
    }

    private String getHtmlResponse(String link) {
        try {
            return httpClientServer.get(link);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<String> extractLinks(String html, ConcurrentLinkedQueue<String> linksWillBeProcessd) throws IOException {
        if (html.isBlank() || html.isEmpty()) {
            return null;
        }
        return htmlExtractLinks(html, linksWillBeProcessd);
    }

}
