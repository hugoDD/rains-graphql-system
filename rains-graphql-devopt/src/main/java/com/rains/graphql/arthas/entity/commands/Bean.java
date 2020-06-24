package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class Bean extends BaseCommand {
    private String name;
    private String codeSource;
    private Boolean isInterface;
    private Boolean isAnnotation;
    private Boolean isEnum;
    private Boolean anonymousClass;
    private Boolean array;
    private Boolean localClass;
    private Boolean memberClass;
    private Boolean primitive;
    private Boolean synthetic;
    private String simpleName;
    private String modifier;
    private String annotation;

    private String interfaces;
    private String superClass;
    private String classLoader;
    private String classLoaderHash;

}
