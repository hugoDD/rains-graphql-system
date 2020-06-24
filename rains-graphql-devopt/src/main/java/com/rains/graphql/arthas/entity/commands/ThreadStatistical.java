package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class ThreadStatistical {
    private long live;
    private long peak;
    private long daemon;
}
