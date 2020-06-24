package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class General extends BaseCommand {
    public static final String KEY="KEY";
    public static final String VALUE="VALUE";

    private String key;
    private String value;
}
