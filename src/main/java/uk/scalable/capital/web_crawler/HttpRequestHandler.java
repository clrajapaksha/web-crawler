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

public class HttpRequestHandler {
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
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);


            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        catch( URISyntaxException e )
        {
            e.printStackTrace();
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
