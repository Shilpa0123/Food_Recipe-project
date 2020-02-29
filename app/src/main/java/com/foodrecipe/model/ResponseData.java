package com.foodrecipe.model;

import com.google.gson.annotations.SerializedName;

public class ResponseData {
    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public String status;

    @SerializedName("id")
    public String id;
}
