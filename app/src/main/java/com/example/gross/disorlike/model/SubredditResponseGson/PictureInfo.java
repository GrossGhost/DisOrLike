package com.example.gross.disorlike.model.SubredditResponseGson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PictureInfo {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("likes")
    @Expose
    private Boolean likes;

    @SerializedName("score")
    @Expose
    private String score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Boolean getLikes() {
        return likes;
    }

    public void setLikes(Boolean likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
