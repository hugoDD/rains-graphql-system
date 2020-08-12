package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

import java.util.List;

@Data
public class Method {
    private String declaringClass;
    private String methodName;
    private String modifier;
    private String annotation;
    private List<String> parameters;
    private String returnObj;
    private String exceptions;
    private String classLoaderHash;
}
