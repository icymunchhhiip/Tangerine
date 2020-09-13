package com.sixsense.tangerine.setting;

public class Recipe {
    String title;
    String time;
    String tag;
    String userid;

    public Recipe(String title, String time, String tag, String userid) {
        this.title = title;
        this.time = time;
        this.tag = tag;
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
