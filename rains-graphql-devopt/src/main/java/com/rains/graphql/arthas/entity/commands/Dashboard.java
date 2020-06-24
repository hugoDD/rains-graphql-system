package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

import java.util.List;

@Data
public class Dashboard extends  BaseCommand{
    private String pid;
    private List<ThreadInfo> threads;
    private JvmMemory memory;
    private RuntimeInfo runtimeInfo;


}
