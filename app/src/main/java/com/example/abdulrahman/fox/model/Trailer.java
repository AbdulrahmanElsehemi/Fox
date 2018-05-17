package com.example.abdulrahman.fox.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class Trailer {
    @SerializedName("key")
    String key;
    @SerializedName("name")
    String name;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
