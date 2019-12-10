package uk.scalable.capital.web_crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class HttpRequestHandler {

    private final static Logger LOGGER = Logger.getLogger(HttpRequestHandler.class.getName());
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

            LOGGER.info("\nSending 'GET' request to URL : " + url);

            LOGGER.info("Response Code : " + responseCode);


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
            LOGGER.severe(e.getMessage());
        }

        return response.toString();
    }

    public CompletableFuture<String> getPageSourceAsync( String url, Executor executor )
    {
        CompletableFuture<String> responseFuture = CompletableFuture.supplyAsync( () -> {
            return this.getPageSource( url );
        }, executor );
        return responseFuture;
    }

}
