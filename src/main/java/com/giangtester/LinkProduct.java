package com.giangtester;

import com.opencsv.bean.CsvBindByPosition;

public class LinkProduct {
    @CsvBindByPosition(position = 0)
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
