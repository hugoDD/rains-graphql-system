package com.rains.graphql.common.exception;

/**
 * 系统内部异常
 */
public class SysException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public SysException(String message) {
        super(message);
    }
}
