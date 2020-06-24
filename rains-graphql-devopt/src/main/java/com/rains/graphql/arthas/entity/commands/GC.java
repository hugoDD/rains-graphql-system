package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public class GC {
        private long scavengeCount;
        private long scavengeTime;
        private long marksweepCount;
        private long marksweepTime;
}
