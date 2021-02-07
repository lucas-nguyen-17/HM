package com.giangtester;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class Tracking {
    @CsvBindByName(column = "orderId")
    @CsvBindByPosition(position = 0)
    private String orderId;

    @CsvBindByName(column = "delivery")
    @CsvBindByPosition(position = 1)
    private String delivery;

    @CsvBindByName(column = "date")
    @CsvBindByPosition(position = 2)
    private String date;

    @CsvBindByName(column = "information")
    @CsvBindByPosition(position = 3)
    private String information;

    public Tracking(String orderId, String delivery, String date, String information) {
        this.orderId = orderId;
        this.delivery = delivery;
        this.date = date;
        this.information = information;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
