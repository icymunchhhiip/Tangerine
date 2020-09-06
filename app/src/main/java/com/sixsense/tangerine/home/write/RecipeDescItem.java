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
        this.descbtnum=descbtnum;
        this.cookingtime = cookingtime;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public int getBtnum() {
        return descbtnum;
    }

    public void setBtnum(int descbtnum) {
        this.descbtnum = descbtnum;
    }

    public String getFood_image() {
        return food_image_addr;
    }

    public void setFood_image(String food_image_addr) {
        this.food_image_addr = food_image_addr;
    }

    public String getRecipe_desc() {
        return recipe_desc;
    }

    public void setRecipe_desc(String recipe_desc) {
        this.recipe_desc = recipe_desc;
    }

    public String getRecipe_desc_detail() {
        return recipe_desc_detail;
    }

    public void setRecipe_desc_detail(String getRecipe_desc_detail) {
        this.recipe_desc_detail = recipe_desc_detail;
    }
}
