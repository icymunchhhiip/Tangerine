package com.sixsense.tangerine.community.item;

import java.io.Serializable;

public class BoardPost implements Serializable {
    private Member writer; //작성자 정보(닉네임,프로필) 가져오기 위한것
    private int b_no; //게시글식별번호 #int(16) primary key, foreign key
    private int category; //카테고리번호 #binary(2)
    private int likes; //좋아요 #int(20)
    private String date;
    private String title; // 제목 #varchar(30)
    private String description; // 본문 #varchar(255)
    private String imgPath; // 사진 #varchar(30)
    private boolean myLike; //내가 좋아요 했는지 여부

    public BoardPost(Member writer, int b_no, int category, String title, String description, String date, int likes, String imgPath, boolean myLike) {
        this.writer = writer;
        this.b_no = b_no;
        this.category = category;
        this.likes = likes;
        this.date = date;
        this.title = title;
        this.description = description;
        this.imgPath = imgPath;
        this.myLike = myLike;
    }

    public Member getWriter() {
        return writer;
    }

    public void setWriter(Member writer) {
        this.writer = writer;
    }

    public int getB_no() {
        return b_no;
    }

    public void setB_no(int b_no) {
        this.b_no = b_no;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void LikePlus(){this.likes += 1;}

    public void LikeMinus(){this.likes -= 1;}

    public boolean isMyLike() {
        return myLike;
    }

    public void setMyLike(boolean myLike) {
        this.myLike = myLike;
    }
}