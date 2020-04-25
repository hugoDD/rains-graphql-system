package com.rains.graphql.system.enums;

public enum Order {
    ASC("asc"),
    DES("des");

    private String name = "asc";

    Order(String name) {
        this.name = name;
    }


}
