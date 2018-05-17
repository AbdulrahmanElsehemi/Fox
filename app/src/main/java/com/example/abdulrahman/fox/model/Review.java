package com.example.abdulrahman.fox.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class Review {
    @SerializedName("author")
    String author;
    @SerializedName("content")
    String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
