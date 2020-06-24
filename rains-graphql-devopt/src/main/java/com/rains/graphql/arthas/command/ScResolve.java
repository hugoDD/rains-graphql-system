package com.rains.graphql.arthas.command;

import com.rains.graphql.arthas.entity.commands.General;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScResolve extends BaseCmdResolve {
    @Override
    public Function<List<String>, List<General>> transform() {
        return rs -> {
            List<General> list =  rs.stream().map(c->{
               String[] arr = c.split(REGEX);
                General general = new General();
                general.setKey(arr[0]);
                general.setValue(arr[1]);
                return general;
            }).collect(Collectors.toList());

            return list;
        };
    }
}
