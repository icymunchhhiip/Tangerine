package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectedRecipe {
    @SerializedName("mem_id")
    public int memId;
    @SerializedName("recipe_id")
    public int recipeId;
    @SerializedName("recipe_name")
    public String recipeName;
    @SerializedName("level")
    public int level;
    @SerializedName("food_type")
    public int foodType;
    @SerializedName("r_time")
    public int rTime;
    @SerializedName("like_num")
    public int likeNum;
    @SerializedName("r_tool")
    public int rTool;

    @SerializedName("ingr_list")
    public List<SelectedRecipe.IngrInfo> ingrList = new ArrayList<>();
    @SerializedName("recipe_list")
    public List<SelectedRecipe.RecipeContent> recipeList = new ArrayList<>();

    public class IngrInfo implements Serializable {
        @SerializedName("ingr_name")
        public String ingrName;
        @SerializedName("ingr_count")
        public float ingrCount;
        @SerializedName("ingr_unit")
        public String ingrUnit;
        @SerializedName("ingr_kcal")
        public float ingrKcal;
    }

    public class RecipeContent implements Serializable {
        @SerializedName("r_img")
        public String recipeImg;
        @SerializedName("r_desc")
        public String recipeDesc;
        @SerializedName("r_detail")
        public String descDetail;
        @SerializedName("r_time")
        public String recipeTime;
        @SerializedName("r_num")
        public int rNum;
    }
}
