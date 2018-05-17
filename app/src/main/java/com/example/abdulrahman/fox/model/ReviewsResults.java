package com.example.abdulrahman.fox.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;



@Parcel
public class ReviewsResults {
    @SerializedName("results")
    List<Review> reviews;
    @SerializedName("total_results")
    int totalReviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }
}
