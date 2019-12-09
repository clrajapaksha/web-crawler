package uk.scalable.capital.web_crawler;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SpiderHub
{
    public void generateSpiders(List<String> webLinks)
    {
       CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
           @Override
           public String get() {
               return null;
           }
       });
    }
}
