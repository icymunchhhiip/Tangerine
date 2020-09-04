package com.sixsense.tangerine.network;

import java.lang.reflect.Member;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpInterface {

    @POST("/member.php")
    Call<Member> setMember(@Body Member member);

    @GET("/main-event/main-event.php")
    Call<MainEventList> getMainEvent();

    @GET("/recipe/recent.php")
    Call<RecipeIntroList> getRecentRecipe(
            @Query("m_id") int m_id,
            @Query("page") int page,
            @Query("page_size") int page_size
    );

    @GET("/recipe/condition.php")
    Call<RecipeIntroList> getRecipeCondition(
            @Query("r_name") String r_name,
            @Query("r_kinds") byte r_kinds,
            @Query("r_level") byte r_level,
            @Query("r_tool") byte r_tool,
            @Query("r_time") byte r_time,
            @Query("page") int page,
            @Query("page_size") int page_size
    );

    @GET("/recipe/like.php")
    Call<String> setRecipeLike(
            @Query("m_id") int m_id,
            @Query("recipe_id") int recipe_id,
            @Query("action") String action
    );

    @GET("/recipe/in-recipe.php")
    Call<InRecipe> getInRecipe(
            @Query("recipe_id") int recipe_id
    );
}
