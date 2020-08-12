package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

import java.util.List;

@Data
public class LoggerBean {
    private String name;

    private List<LoggerLevel> level ;

}
