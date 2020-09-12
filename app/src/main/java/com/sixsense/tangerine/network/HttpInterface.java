package com.sixsense.tangerine.network;

import java.lang.reflect.Member;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
            @Query("m_id") int mId,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    @GET("/recipe/condition.php")
    Call<RecipeIntroList> getRecipeCondition(
            @Query("r_name") String rName,
            @Query("r_kinds") byte rKinds,
            @Query("r_level") byte rLevel,
            @Query("r_tool") byte rTool,
            @Query("r_time") byte rTime,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    @GET("/recipe/like.php")
    Call<String> setRecipeLike(
            @Query("m_id") int mId,
            @Query("recipe_id") int recipeId,
            @Query("action") String action
    );

    @GET("/recipe/in-recipe.php")
    Call<InRecipe> getInRecipe(
            @Query("recipe_id") int recipeId
    );

    @GET("/recipe/ingrs.php")
    Call<List<InRecipe.IngrInfo>> getIngrs();

    @POST("/recipe/insert-recipe.php")
    Call<String> setRecipe(
            @Body SelectedRecipe selectedRecipe
    );

    @DELETE("/recipe/del-recipe.php")
    Call<String> deleteRecipe(
            @Query("recipe_id") int recipeId
    );


}
