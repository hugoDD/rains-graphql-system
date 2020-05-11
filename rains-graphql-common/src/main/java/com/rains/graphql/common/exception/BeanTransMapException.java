package com.rains.graphql.common.exception;

public class BeanTransMapException extends RuntimeException {
    public BeanTransMapException(Exception e) {
        super(e.getMessage(), e);
    }
}
