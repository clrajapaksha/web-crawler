package com.sc.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpRequestHandler {

    private static final Logger LOGGER = Logger.getLogger(HttpRequestHandler.class.getName());
    public String getPageSource(String url) {
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("GET");

            //add request header to avoid 403
            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpClient.setRequestProperty("Accept", "*/*");
            httpClient.setRequestProperty("Cache-Control", "no-cache");
            httpClient.setRequestProperty("Connection", "keep-alive");
            String host = new URI(url).getHost();
            if(host != null)
                httpClient.setRequestProperty( "Host", host );

            int responseCode = httpClient.getResponseCode();

            LOGGER.log(Level.INFO,"Sending 'GET' request to URL : {0}" , url);

            LOGGER.log(Level.INFO,"Response Code : {0}", responseCode);


            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        catch( URISyntaxException e )
        {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return response.toString();
    }

    public CompletableFuture<String> getPageSourceAsync( String url, Executor executor )
    {
        return CompletableFuture.supplyAsync( () -> this.getPageSource( url ), executor );

    }

}
