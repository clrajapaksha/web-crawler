package com.sc.crawler;


import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * Web Crawler
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // start
        System.out.println("Enter your search terms:");

        // Step 0: Read a string (search term) from standard input
        Scanner scanner = new Scanner(System.in);
        String searchTerms = scanner.nextLine();

        // Step 1: Get a Google result page for the search term
        String response = GoogleUtil.doGoogle(searchTerms);

        // Step 2: Extract main result links from the page
        List<String> links = GoogleUtil.getLinksFromSearchPage(response);

        // Step 3: Download the respective pages and extract the names of Javascript libraries used in them
        CompletableFuture<List<String>> spiders = SpiderHub.generateSpiders( links );
        List<String> webPages = SpiderHub.harvest(spiders);
        List<String> libNames = GoogleUtil.getJsLibs(webPages);

        // Step 4: Print top 5 most used libraries to standard output
        Printer printer = new Printer(libNames);
        printer.printTopN(5);

        // exit
        System.exit(0);
    }
}
