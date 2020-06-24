package com.rains.graphql.arthas.entity.commands;

import lombok.Data;

@Data
public  class Memory{
    private String used;
    private String total;
    private String max;
    private String usage;
}
