package uk.scalable.capital.web_crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestHandler {
    public String getPageSource(String url) {
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("GET");

            //add request header
            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

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

        return response.toString();
    }

}
