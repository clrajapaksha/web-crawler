package uk.scalable.capital.web_crawler;

import com.sun.imageio.stream.StreamFinalizer;

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

    public Map<String, String> nameMap = new HashMap<>();
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
        List<String> names = libs.stream().map( x -> {
            for(String key: nameMap.keySet())
            {
                if( x.contains(key) ) {
                    x = nameMap.get(key);
                    break;
                }
            }
            return x;
        }).collect(Collectors.toList());

        return names;
    }

}
