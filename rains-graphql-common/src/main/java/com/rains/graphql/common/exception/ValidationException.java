package com.rains.graphql.common.exception;

/**
 * 校验异常，数据校验出错时抛出
 * @author RY
 */
public class ValidationException extends RuntimeException{
	static final long serialVersionUID = -7034897190745766939L;


	/**
	 * 校验异常类
	 */
    public ValidationException() {
    	super();
    }



    /**
     * 校验异常类
     * @param message 异常内容
     */
    public ValidationException(String message) {
    	super(message);
    }



    /**
     * 校验异常类
     * @param message 异常内容
     * @param cause 异常堆栈
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * 校验异常类
     * @param cause 异常堆栈
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }


}
