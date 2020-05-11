package com.rains.graphql.common.domain;

import java.io.Serializable;

public class ResultResponse implements Serializable {

    private static final long serialVersionUID = -8713837118340960775L;
    private String message;
    private Object data;
    private Integer code = 200;

    public ResultResponse message(String message) {
        this.message = message;
        return this;
    }

    public ResultResponse data(Object data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
