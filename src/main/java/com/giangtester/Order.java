package com.giangtester;

import com.opencsv.bean.CsvBindByPosition;

public class Order {
    @CsvBindByPosition(position = 0)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
