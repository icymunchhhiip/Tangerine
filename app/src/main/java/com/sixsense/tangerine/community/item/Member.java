package com.sixsense.tangerine.community.item;

import java.io.Serializable;

public class Member implements Serializable {
    private int id;
    private String nickname;
    private String profilePath;
    private String localString;
    private int localCode = 0;

    public Member(int id){
        this.id = id;
    }

    public Member(int id, String nickname, String profilePath){
        this.id = id;
        this.nickname = nickname;
        this.profilePath = profilePath;
    }

    public Member(int id, String nickname, String profilePath, String localString, int localCode){
        this.id = id;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.localString = localString;
        this.localCode = localCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getLocalString() {
        return localString;
    }

    public int getLocalCode() {
        return localCode;
    }

    public void setLocalAddress(String localString,int localCode) {
        this.localString = localString;
        this.localCode = localCode;
    }
}