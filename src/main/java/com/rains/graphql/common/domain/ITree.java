package com.rains.graphql.common.domain;

public interface ITree<ID> {
    ID getId();


    String getLabel();

    default String getKey() {
        return getId() == null ? null : getId() + "";
    }

    default String getValue() {
        return getId() == null ? null : getId() + "";
    }
}
