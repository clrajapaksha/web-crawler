package uk.scalable.capital.web_crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Web Crawler
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // Step 0: Read a string (search term) from standard input
        Scanner scanner = new Scanner(System.in);
        String searchTerms = scanner.nextLine();

        // Step 1: Get a Google result page for the search term
        String response = "";
        try {
            String baseUrl = "https://www.google.com/search?q=";
            String encodeUrl = baseUrl + URLEncoder.encode(searchTerms, "UTF-8");
            System.out.println(encodeUrl);

            HttpRequestHandler httpRequestHandler = new HttpRequestHandler();
            response = httpRequestHandler.getPageSource(encodeUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // Step 2: Extract main result links from the page
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.body().getElementById("main").select("a");
        for(Element element: elements)
        {
            String link = element.attr("href");
            // filtering and cleaning
            if(link.startsWith("/url?q=")) {
                String url = link.substring(7);
                url = url.substring(0, url.indexOf("&sa="));
                links.add(url);
                System.out.println(url);
            }
        }
        System.out.println(links.size()+" links found");

        // Step 3: Download the respective pages and extract the names of Javascript libraries used in them
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler();
        String webPage = httpRequestHandler.getPageSource(links.get(0));
        Document doc1 = Jsoup.parse(webPage);
        Elements elements1 = doc1.getElementsByTag("script");
        for(Element element: elements1)
        {
            if(element.attr("src").startsWith("http") && element.attr("src").endsWith(".js"))
            {
                System.out.println(element.attr("src"));
            }
        }

        // Step 4: Print top 5 most used libraries to standard output

    }
}
