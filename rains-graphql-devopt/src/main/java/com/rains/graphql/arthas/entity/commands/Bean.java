package com.rains.graphql.arthas.entity.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class Bean extends BaseCommand {
    private String classInfo;
    private String name;
    private String codeSource;
    private Boolean isInterface;
    private Boolean isAnnotation;
    private Boolean isEnum;
    private Boolean isAnonymousClass;
    private Boolean isArray;
    private Boolean isLocalClass;
    private Boolean isMemberClass;
    private Boolean isPrimitive;
    private Boolean isSynthetic;
    private String simpleName;
    private String modifier;
    private String annotation;

    private String interfaces;
    private String superClass;
    private String classLoader;
    private String classLoaderHash;


    private String detail;

    private List<Method> methods;
    private Set<String> methodSign;

    private String codes;

}
