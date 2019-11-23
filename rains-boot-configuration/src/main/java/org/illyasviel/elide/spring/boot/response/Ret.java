package org.illyasviel.elide.spring.boot.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ret<T> {

    private Integer code;
    private String msg;
    private T data;
    private boolean success;

    public Ret() {

    }

    public Ret(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success =(20000==code.intValue());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("'code':").append(code).append(",");
        builder.append("'msg':").append(msg).append(",");
        builder.append("'success':").append(success).append(",");
        builder.append("}");
        return builder.toString();
    }
}
