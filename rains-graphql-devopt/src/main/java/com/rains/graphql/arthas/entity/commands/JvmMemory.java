package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class JvmMemory {
    private Memory heap;
    private Memory edenSpace;
    private Memory survivorSpace;
    private Memory oldGen;
    private Memory nonheap;
    private Memory codeCache;
    private Memory metaspace;
    private Memory compressedClassSpace;
    private Memory direct;
    private Memory mapped;

    private GC gc;






}
