package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class RuntimeInfo {
    private String osName;
    private String osVersion;
    private String javaVersion;
    private String javaHome;
    private String systemloadAverage;
    private String processors;
    private String uptime;
}
