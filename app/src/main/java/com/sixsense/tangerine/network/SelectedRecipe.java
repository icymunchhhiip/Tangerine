package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectedRecipe {
    @SerializedName("mem_id")
    public Long mem_id;
    @SerializedName("recipe_name")
    public String recipe_name;
    @SerializedName("level")
    public String level;
    @SerializedName("food_type")
    public String foodType;
    @SerializedName("r_time")
    public String rTime;
    @SerializedName("like_num")
    public String likeNum;
    @SerializedName("r_tool")
    public String rTool;
    @SerializedName("total_kcal")
    public String totalKcal;
    @SerializedName("ingr_list")
    public List<SelectedRecipe.IngrInfo> ingrList = new ArrayList<>();
    @SerializedName("recipe_list")
    public List<SelectedRecipe.RecipeContent> recipeList = new ArrayList<>();

    public class IngrInfo implements Serializable {
        @SerializedName("ingr_name")
        public String ingrName;
        @SerializedName("ingr_count")
        public String ingrCount;
        @SerializedName("ingr_unit")
        public String ingrUnit;
        @SerializedName("ingr_kcal")
        public String ingrKcal;
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
    }
}
