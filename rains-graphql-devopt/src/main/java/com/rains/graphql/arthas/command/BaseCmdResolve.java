package com.rains.graphql.arthas.command;

import com.rains.graphql.arthas.entity.commands.Dashboard;

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

     public abstract <T> Function<List<String>, T> transform();
}
