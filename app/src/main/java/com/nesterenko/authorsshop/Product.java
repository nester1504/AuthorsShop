package com.nesterenko.authorsshop;

public class Product {

    private int price;
    private String heading;
    private int image;
    String key;

    public Product(){

    }

    public Product(int price, String heading, int image, String key) {
        this.price = price;
        this.heading = heading;
        this.image = image;
        this.key = key;
    }

    public Product(int price, String heading, int image) {
        this.price = price;
        this.heading = heading;
        this.image = image;
    }

    public Product(int price, String heading) {
        this.price = price;
        this.heading = heading;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public String getHeading() {
        return heading;
    }

    public int getImage() {
        return image;
    }
}
