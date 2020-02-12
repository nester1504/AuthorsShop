package com.nesterenko.authorsshop;

import java.io.Serializable;
import java.util.ArrayList;


public class Product  implements Serializable {

    private int price;
    private String heading;
    private ArrayList<String> urlImageList;
    String key;
    private boolean favorite;

    public Product(int price, String heading, ArrayList<String> urlImageList, boolean favorite) {
        this.price = price;
        this.heading = heading;
        this.urlImageList = urlImageList;
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setUrlImageList(ArrayList<String> urlImageList) {
        this.urlImageList = urlImageList;
    }

    public ArrayList<String> getUrlImageList() {
        return urlImageList;
    }

    public Product(int price, String heading, ArrayList<String> urlImageList) {
        this.price = price;
        this.heading = heading;
        this.urlImageList = urlImageList;
    }

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

    public void setKey(String key) {
        this.key = key;
    }

    public int getPrice() {
        return price;
    }

    public String getHeading() {
        return heading;
    }

    public String getKey() {
        return key;
    }

}