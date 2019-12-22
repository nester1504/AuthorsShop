package com.nesterenko.authorsshop;

public class Product {

    private int price;
    private String heading;
    private String urlImage;
    String key;

    public Product(int price, String heading) {
        this.price = price;
        this.heading = heading;
    }

    public Product(){

    }
    public void setPrice(int price) {
        this.price = price;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPrice() {
        return price;
    }

    public String getHeading() {
        return heading;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getKey() {
        return key;
    }

    public Product(int price, String heading, String urlImage) {
        this.price = price;
        this.heading = heading;
        this.urlImage = urlImage;
    }

    public Product(int price, String heading, String urlImage, String key) {
        this.price = price;
        this.heading = heading;
        this.urlImage = urlImage;
        this.key = key;
    }
}