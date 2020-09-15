package com.sixsense.tangerine.community.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CookingTip implements Serializable {
    @SerializedName("wdate")
    private String wdate;
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("fname")
    private String fname;
    @SerializedName("title")
    private String title;

    public String getWdate(){
        return wdate;
    }
    public void setWdate(String wdate){
        this.wdate = wdate;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}