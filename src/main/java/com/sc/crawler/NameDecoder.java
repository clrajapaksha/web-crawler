package com.sc.crawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NameDecoder {
    /**
     * jQuery - jquery.min.js
     * GPT gpt.js
     * Bootstrap - bootstrap.js
     * Vue -
     */

    private Map<String, String> nameMap = new HashMap<>();
    public NameDecoder()
    {
        nameMap.put("jquery", "jQuery");
        nameMap.put("gpt", "GPT");
        nameMap.put("bootstrap", "Bootstrap");
        nameMap.put("Popper", "Popper");
        nameMap.put("d3", "d3");
        nameMap.put("react", "ReactJS");
        nameMap.put("angular", "Angular");
        nameMap.put("Vue", "vue");
        nameMap.put("polymer", "Polymer");
        nameMap.put("underscore", "underscore");

    }

    public List<String> decodeNames(List<String> libs)
    {
        return libs.stream().map( x -> {
            for(Map.Entry entry: nameMap.entrySet())
            {
                if( x.contains(entry.getKey().toString()) ) {
                    x = nameMap.get(entry.getKey().toString());
                    break;
                }
            }
            return x;
        }).collect(Collectors.toList());

    }

}
