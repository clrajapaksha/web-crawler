package com.sc.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleUtil {
    private static final Logger LOGGER = Logger.getLogger(GoogleUtil.class.getName());
    private static HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    private GoogleUtil() {
        // do nothing
    }

    public static String doGoogle(String searchTerms) {
        String response = "";
        try {
            String baseUrl = "https://www.google.com/search?q=";
            String encodeUrl = baseUrl + URLEncoder.encode(searchTerms, "UTF-8");
            System.out.println(encodeUrl);


            response = httpRequestHandler.getPageSource(encodeUrl);
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return response;
    }

    public static List<String> getLinksFromSearchPage(String searchPage) {
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.parse(searchPage);
        Elements elements = doc.body().getElementById("main").select("a");
        for (Element element : elements) {
            String link = element.attr("href");
            // filtering and cleaning
            if (link.startsWith("/url?q=")) {
                String url = link.substring(7);
                url = url.substring(0, url.indexOf("&sa="));
                if (!url.startsWith("https://accounts.google.com/ServiceLogin"))
                    links.add(url);
                System.out.println(url);
            }
        }

        System.out.println(links.size() + " links found");
        return links;
    }

    public static List<String> getJsLibs(List<String> pageSources) {
        List<String> libNames = new ArrayList<>();

        for (String webPage : pageSources) {
            Document doc1 = Jsoup.parse(webPage);
            Elements elements1 = doc1.getElementsByTag("script");
            for (Element element : elements1) {
                if (element.attr("src").startsWith("http") && element.attr("src").endsWith(".js")) {
                    String jsLink = element.attr("src");
                    String libName = jsLink.substring(jsLink.lastIndexOf('/') + 1);
                    System.out.println(jsLink);
                    libNames.add(libName);
                }
            }
        }

        return libNames;
    }

}
