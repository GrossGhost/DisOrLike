package com.example.gross.disorlike.controller;

import com.example.gross.disorlike.model.RedditGson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("r/aww.json")
    Call<RedditGson> getAwwJson(@Query("after") String afterItemName);
}
