package com.example.abdulrahman.fox.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;



@Parcel
public class TrailersResults {
    @SerializedName("results")
    List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }
}