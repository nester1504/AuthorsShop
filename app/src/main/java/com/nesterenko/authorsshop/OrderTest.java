package com.nesterenko.authorsshop;

public class OrderTest {

    private int price;
    private String product;
    private int image;

    public int getPrice() {
        return price;
    }

    public String getProduct() {
        return product;
    }

    public int getImage() {
        return image;
    }

    public OrderTest(Integer price, String product, int image) {
        this.price = price;
        this.product = product;
        this.image = image;
    }
}
