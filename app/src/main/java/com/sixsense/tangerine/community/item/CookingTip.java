package com.sixsense.tangerine.community.item;

import com.google.gson.annotations.SerializedName;

public class CookingTip {

    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("fname")
    private String fname;

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
}