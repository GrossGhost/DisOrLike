package com.example.gross.disorlike.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Child {
    @SerializedName("data")
    @Expose
    private PictureInfo data;

    public PictureInfo getData() {
        return data;
    }

    public void setData(PictureInfo data) {
        this.data = data;
    }
}
