package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeIntroList {
    @SerializedName("has_more")
    public Integer hasMore = 0;
    @SerializedName("data")
    public List<RecipeIntro> data = new ArrayList<>();

    public class RecipeIntro implements Serializable {
        @SerializedName("recipe_id")
        public Integer recipeId;
        @SerializedName("recipe_img")
        public String recipeImg;
        @SerializedName("recipe_name")
        public String recipeName;
        @SerializedName("recipe_min")
        public String recipeMin;
        @SerializedName("recipe_tags")
        public String recipeTags;
        @SerializedName("mem_profile")
        public String memProfile;
        @SerializedName("mem_id")
        public Long memId;
        @SerializedName("mem_name")
        public String memName;
        @SerializedName("recipe_fav")
        public int recipeFav;
    }
}
