package com.example.abdulrahman.fox.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;



@Parcel
public class MoviesResults {
    @SerializedName("results")
    List<Movie> mMoviesResults;

    public List<Movie> getmMoviesResults() {
        return mMoviesResults;
    }

}
