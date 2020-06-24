package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

import java.util.Map;

@Data
public class ThreadInfo {

    private long id;
    private String name;
    private String group;
    private int priority;
    private String state;
    private String cpu;//%
    private String time;
    private Boolean interrupted;
    private Boolean daemon;

    //private Map<String,String> tatol;
}
