package com.sc.crawler;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SpiderHub {

    private static final int THREAD_POOL_SIZE = 10;
    private static Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final Logger LOGGER = Logger.getLogger(SpiderHub.class.getName());

    private SpiderHub() {
        // do nothing
    }

    public static CompletableFuture<List<String>> generateSpiders(List<String> webLinks) {
        List<CompletableFuture<String>> webPageFutures =
                webLinks.stream()
                        .map(webLink ->
                        {
                            HttpRequestHandler client = new HttpRequestHandler();
                            return client.getPageSourceAsync(webLink, executor);
                        }).collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                webPageFutures.toArray(new CompletableFuture[webPageFutures.size()])
        );

        return allFutures.thenApply(v -> webPageFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList())
        );
    }

    public static List<String> harvest(CompletableFuture<List<String>> pageContentFutures)
    {
        List<String> pages = null;
        try {
            pages = pageContentFutures.get();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return pages;
    }
}
