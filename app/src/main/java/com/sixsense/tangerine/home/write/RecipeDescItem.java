package com.sixsense.tangerine.home.write;

public class RecipeDescItem {
    String food_image_addr;
    String recipe_desc;
    String recipe_desc_detail;
    int descbtnum;
    String cookingtime;

    public RecipeDescItem(String recipe_image_addr, String recipe_desc, String recipe_desc_detail, int descbtnum, String cookingtime) {
        this.food_image_addr = recipe_image_addr;
        this.recipe_desc = recipe_desc;
        this.recipe_desc_detail = recipe_desc_detail;
        this.descbtnum = descbtnum;
        this.cookingtime = cookingtime;
    }

    public void setBtnum(int descbtnum) {
        this.descbtnum = descbtnum;
    }
}
