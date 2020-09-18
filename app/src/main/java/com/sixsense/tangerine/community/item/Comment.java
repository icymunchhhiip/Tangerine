package com.sixsense.tangerine.community.item;

public class Comment {
    int p_no; // 댓글이 달린 게시글 식별번호

    Member writer; // 작성자 정보
    int c_no; // 댓글 식별번호
    String description; // 댓글 내용
    String date; // 댓글 작성 날짜

    public Comment(int p_no, Member writer, int c_no, String description, String date) {
        this.p_no = p_no;
        this.writer = writer;
        this.c_no = c_no;
        this.description = description;
        this.date = date;
    }

    public int getP_no() {
        return p_no;
    }

    public void setP_no(int p_no) {
        this.p_no = p_no;
    }

    public Member getWriter() {
        return writer;
    }

    public void setWriter(Member writer) {
        this.writer = writer;
    }

    public int getC_no() {
        return c_no;
    }

    public void setC_no(int c_no) {
        this.c_no = c_no;
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
}