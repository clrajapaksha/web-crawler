package com.sc.crawler;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class Printer {
    private List<String> libNames;
    public Printer(List<String> libNames)
    {
        this.libNames = libNames;
    }

    public void printTopN(int n)
    {
        NameDecoder docoder = new NameDecoder();
        libNames = docoder.decodeNames(libNames); // for bonus steps
        List<String> result = libNames.stream()
                //.map(String::toLowerCase)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long> comparingByValue(reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(toList());

        System.out.println("\nResults:\n");
        System.out.println(result);
    }
}
