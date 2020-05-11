package com.rains.graphql.common.log;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogVo implements Serializable {

    private String username;

    private String ip;

   private Long time;

   private String operation;
}
