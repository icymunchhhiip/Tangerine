package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InRecipe {
    @SerializedName("r_id")
    public String rId;
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
    public List<IngrInfo> ingrList = new ArrayList<>();
    @SerializedName("recipe_list")
    public List<RecipeContent> recipeList = new ArrayList<>();

    public class IngrInfo {
        @SerializedName("ingr_name")
        public String ingrName;
        @SerializedName("ingr_cnt")
        public String ingrCnt;
        @SerializedName("ingr_kcal")
        public String ingrKcal;
    }

    public class RecipeContent {
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
