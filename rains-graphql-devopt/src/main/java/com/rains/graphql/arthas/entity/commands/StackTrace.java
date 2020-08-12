package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class StackTrace {
    private String methodName;
    private String fileName;
    private int lineNumber;
    private String className;
    private Boolean nativeMethod;
}
