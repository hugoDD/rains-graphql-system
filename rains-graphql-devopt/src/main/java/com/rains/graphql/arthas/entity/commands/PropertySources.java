package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

import java.util.Map;

@Data
public class PropertySources {
    private String name;
    private Map<String,String> properties;
}
