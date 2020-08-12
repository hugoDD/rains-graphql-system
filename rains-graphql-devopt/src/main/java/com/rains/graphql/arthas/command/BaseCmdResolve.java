package com.rains.graphql.arthas.command;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseCmdResolve {
    protected final static String REGEX = "\\s+";
    protected static final String PROMPT = "[arthas@";

    protected Map<String, Integer> chartPoit(String header) {
        String[] names = header.split(REGEX);
        Map<String, Integer> indexMap = new HashMap<>();
        for (String name : names) {
            int index = header.indexOf(name);
            indexMap.put(name, index);

        }
        return indexMap;

    }

    protected List<Integer> chartHeaderPoit(String header) {
        String[] names = header.trim().split(REGEX);
        List<Integer> poits = new ArrayList<>();
        for (String name : names) {
            int index = header.indexOf(name);
            poits.add(index);

        }
        return poits;

    }

     public abstract <T> Function<List<String>, T> transform();
}
