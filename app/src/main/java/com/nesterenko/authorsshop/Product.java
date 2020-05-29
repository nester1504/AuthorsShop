package com.nesterenko.authorsshop;

import java.io.Serializable;
import java.util.ArrayList;


public class Product  implements Serializable {

    private int price;
    private String heading;
    private ArrayList<String> urlImageList;
    public String key;
    private boolean favorite;
    private String description;
    private String userName;
    private String phone;
    private String userid;

    public Product(int price, String heading, ArrayList<String> urlImageList, boolean favorite, String description, String userName, String phone, String userid) {
        this.price = price;
        this.heading = heading;
        this.urlImageList = urlImageList;
        this.favorite = favorite;
        this.description = description;
        this.userName = userName;
        this.phone = phone;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
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