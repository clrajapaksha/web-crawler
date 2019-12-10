package uk.scalable.capital.web_crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

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
                if(!url.startsWith( "https://accounts.google.com/ServiceLogin" ))
                    links.add(url);
                System.out.println(url);
            }
        }
        System.out.println(links.size()+" links found");

        // Step 3: Download the respective pages and extract the names of Javascript libraries used in them
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler();
        List<String> webPages = SpiderHub.generateSpiders( links );

        List<String> libNames = new ArrayList<>();
        Map<String, Integer> libNames1 = new HashMap<>();
        for(String webPage: webPages)
        {
            Document doc1 = Jsoup.parse( webPage );
            Elements elements1 = doc1.getElementsByTag( "script" );
            for( Element element : elements1 )
            {
                if( element.attr( "src" ).startsWith( "http" ) && element.attr( "src" ).endsWith( ".js" ) )
                {
                    String jsLink = element.attr( "src" );
                    String libName = jsLink.substring( jsLink.lastIndexOf( '/' )+1 );
                    System.out.println( jsLink );
                    libNames.add(libName);
                    int count = libNames1.containsKey(libName)?libNames1.get(libName):0;
                    libNames1.put(libName, count+1);
                }
            }
        }

        // Step 4: Print top 5 most used libraries to standard output
        for (Map.Entry<String, Integer> entry : libNames1.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }
        NameDecoder docoder = new NameDecoder();
        libNames = docoder.decodeNames(libNames); // for bonus steps
        List<String> result = libNames.stream()
                //.map(String::toLowerCase)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long> comparingByValue(reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(toList());

        System.out.println("\nResults:\n");
        System.out.println(result);
        System.exit(0);
    }
}
