package com.foodrecipe;


import com.foodrecipe.model.ResponseData;
import com.foodrecipe.model.UploadObject;
import com.foodrecipe.model.categoryModel;
import com.foodrecipe.model.myProfile;
import com.foodrecipe.model.myRecipesModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface EndPointUrl {
    @GET("FoodRecipes/user_registration.php?")
    Call<ResponseData> user_registration(
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("emailid") String emailid,
            @Query("uname1") String uname1,
            @Query("pwd1") String pwd

    );

    @GET("FoodRecipes/user_login.php?")
    Call<ResponseData> user_login(
            @Query("uname") String uname,
            @Query("pwd") String pwd

    );

    @GET("FoodRecipes/forgotPassword.php?")
    Call<ResponseData> forgotPassword(
            @Query("emailid") String emailid
    );


    @GET("FoodRecipes/getProfile.php?")
    Call<List<myProfile>> getMyProfile
            (@Query("id") String id);


    @GET("FoodRecipes/update_profile.php?")
    Call<ResponseData> admin_update_profile(
            @Query("id") String id,
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("emailid") String emailid,
            @Query("pwd1") String pwd1,
            @Query("uname1") String uname1
    );

    @Multipart
    @POST("FoodRecipes/update_myrecipe.php?")
    Call<UploadObject> updateRecipe(
            @Part MultipartBody.Part file,
            @PartMap Map<String, String> partMap
    );

    @GET("FoodRecipes/search.php?")
    Call<List<myRecipesModel>> search(
            @Query("search") String search
    );

    @Multipart
    @POST("FoodRecipes/add_recipe.php?")
    Call<UploadObject> add_recipe(
            @Part MultipartBody.Part file,
            @PartMap Map<String, String> partMap
    );

    @Multipart
    @POST("FoodRecipes/update_profile_photo.php?")
    Call<UploadObject> update_profile_photo(
            @Part MultipartBody.Part file,
            @PartMap Map<String, String> partMap);

    @GET("/FoodRecipes/getAllRecipes.php")
    Call<List<myRecipesModel>> getAllRecipes(@Query("country_name") String country_name);

    @GET("FoodRecipes/getRecipieCategory.php")
    Call<List<categoryModel>> getRecipieCategory();

    @GET("/FoodRecipes/myrecipes.php?")
    Call<List<myRecipesModel>> myrecipes(@Query("uname") String uname);


    @GET("/FoodRecipes/update_rating.php?")
    Call<ResponseData> update_rating(
            @Query("rating") String rating,
            @Query("uname") String uname,
            @Query("recipe_name") String recipe_name

    );

}
