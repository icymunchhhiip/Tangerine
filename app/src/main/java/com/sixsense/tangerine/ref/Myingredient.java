package com.sixsense.tangerine.ref;

public class Myingredient {

    String f_id;  //php파일에서 auto_increase 해놓은 'id'
    String m_id;
    String ingredient;
    String date;
    String memo;
    String remaining;
    String storage;

    public Myingredient(String m_id, String ingredient, String date, String memo, String remaining, String storage) {
        this.m_id=m_id;
        this.ingredient = ingredient;
        this.date = date;
        this.memo = memo;
        this.remaining = remaining;
        this.storage = storage;
    }

    public Myingredient(){
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
