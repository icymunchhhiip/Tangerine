package com.sixsense.tangerine.community.item;

import java.io.Serializable;

public class MarketPost implements Serializable {
    private Member writer; //작성자 정보(닉네임,프로필) 가져오기 위한것
    private int mk_no; // 마켓글식별번호 #int(16) primary key, foreign key
    private int localCode; //지역코드
    private int price; // 가격 #int()
    private String title; // 제목 #varchar(30)
    private String description; // 본문 #varcahr(255)
    private String date; // 작성날짜
    private String imgPath; // 사진 #varchar(30)

    public MarketPost(Member writer, int mk_no, int localCode, int price, String title, String description, String date, String imgPath) {
        this.writer = writer;
        this.mk_no = mk_no;
        this.localCode = localCode;
        this.price = price;
        this.date = date;
        this.title = title;
        this.description = description;
        this.imgPath = imgPath;
    }

    public Member getWriter() {
        return writer;
    }

    public void setWriter(Member writer) {
        this.writer = writer;
    }

    public int getMk_no() {
        return mk_no;
    }

    public void setMk_no(int mk_no) {
        this.mk_no = mk_no;
    }

    public int getLocalCode() {
        return localCode;
    }

    public void setLocalCode(int localCode) {
        this.localCode = localCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}