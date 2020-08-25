package com.sixsense.tangerine.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeIntroList {
    @SerializedName("has_more")
    public Integer has_more=0;
    @SerializedName("data")
    public List<RecipeIntro> data=new ArrayList<>();

    public class RecipeIntro{
        @SerializedName("recipe_id")
        public Integer recipe_id;
        @SerializedName("recipe_img")
        public String recipe_img;
        @SerializedName("recipe_name")
        public String recipe_name;
        @SerializedName("recipe_min")
        public String recipe_min;
        @SerializedName("recipe_tags")
        public String recipe_tags;
        @SerializedName("mem_profile")
        public String mem_profile;
        @SerializedName("mem_name")
        public String mem_name;
        @SerializedName("recipe_fav")
        public int recipe_fav;
    }
}
