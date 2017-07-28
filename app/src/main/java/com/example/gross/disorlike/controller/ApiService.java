package com.example.gross.disorlike.controller;

import com.example.gross.disorlike.model.RedditGson;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("r/aww.json")
    Call<RedditGson> getAwwJson();
}
