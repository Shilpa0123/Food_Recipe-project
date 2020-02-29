package com.foodrecipe.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class myRecipesModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("recipe_name")
    @Expose
    private String recipeName;
    @SerializedName("ingredients")
    @Expose
    private String ingredients;
    @SerializedName("recipe_procedure")
    @Expose
    private String recipeProcedure;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("rating")
    @Expose
    private float rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipeProcedure() {
        return recipeProcedure;
    }

    public void setRecipeProcedure(String recipeProcedure) {
        this.recipeProcedure = recipeProcedure;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}