package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class LoggerLevel {
    private String configuredLevel;
    private String effectiveLevel;
    private String instanceId;
}
