package com.example.gross.disorlike.controller;

import com.example.gross.disorlike.model.LoginResponseGson.LoginResponse;
import com.example.gross.disorlike.model.SubredditResponseGson.SubredditResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("r/aww.json")
    Call<SubredditResponse> getAwwJson(@HeaderMap HashMap<String,String> headerMap, @Query("after") String afterItemName);

    @POST("api/login/{user}")
    Call<LoginResponse> signIn(
            @Path("user") String username,
            @Query("user") String user,
            @Query("passwd") String password,
            @Query("api_type") String type
    );
}
