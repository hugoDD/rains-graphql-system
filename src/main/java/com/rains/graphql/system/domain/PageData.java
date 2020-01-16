package com.rains.graphql.system.domain;

import java.util.ArrayList;
import java.util.List;

public class PageData<T> {
    private final List<T> data ;
    private long total;

    public PageData() {
        data  = new ArrayList<>();
    }

    public PageData(long total, List<T> data ) {
        this.total = total;
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
