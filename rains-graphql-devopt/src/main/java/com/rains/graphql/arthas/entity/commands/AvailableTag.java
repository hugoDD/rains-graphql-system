package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

import java.util.List;

@Data
public class AvailableTag {
    private String tag;
    private List<String> values;
}
