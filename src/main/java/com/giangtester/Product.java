package com.giangtester;

public class Product {
    private String link;
    private String price;
    private String image;

    public Product(String link, String price, String image) {
        this.link = link;
        this.price = price;
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
