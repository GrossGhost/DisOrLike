package com.example.gross.disorlike.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PictureInfo {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("subreddit")
    @Expose
    private String subreddit;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubreddit() {        return subreddit;    }

    public void setSubreddit(String subreddit) {        this.subreddit = subreddit;    }

    public String getThumbnail() {        return thumbnail;    }

    public void setThumbnail(String thumbnail) {        this.thumbnail = thumbnail;    }
}