package com.sixsense.tangerine.setting;

public class Mymarket {
    String title;
    String price;
    String date;
    String picture;
    String profile;
    String name;


    public Mymarket(String title, String price, String date, String picture, String profile, String name) {
        this.title = title;
        this.price = price;
        this.date = date;
        this.picture = picture;
        this.profile = profile;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }
}
