package uk.scalable.capital.web_crawler;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SpiderHub
{

    private final static int THREAD_POOL_SIZE = 10;
    private static Executor executor = Executors.newFixedThreadPool( THREAD_POOL_SIZE );

    public static List<String> generateSpiders( List<String> webLinks )
    {
        List<String> webPages = null;
        try
        {
            List<CompletableFuture<String>> webPageFutures = webLinks.stream()
                                                                     .map( webLink ->
                                                                     {
                                                                         HttpRequestHandler client = new HttpRequestHandler();
                                                                         return client.getPageSourceAsync( webLink, executor );
                                                                     } ).collect( Collectors.toList() );

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    webPageFutures.toArray( new CompletableFuture[webPageFutures.size()] )
            );

            CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply( v ->
            {
                return webPageFutures.stream()
                                     .map( pageContentFuture -> pageContentFuture.join() )
                                     .collect( Collectors.toList() );
            } );

            webPages = allPageContentsFuture.get();
        }
        catch( InterruptedException e )
        {
            e.printStackTrace();
        }
        catch( ExecutionException e )
        {
            e.printStackTrace();
        }
        finally
        {
            return webPages;
        }

    }
}
