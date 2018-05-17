package com.example.abdulrahman.fox.api;

import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.model.MoviesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Abdulrahman on 5/11/2018.
 */

public interface MoviesApi {
    //http://api.themoviedb.org/3/movie/popular?api_key=999999999999999999

    @GET("movie/{sort_by}")
    Call<MoviesResults> getPopluarMovies(@Path("sort_by") String sortBy);

    // http://api.themoviedb.org/3/movie/123?api_key=99999999999999999999
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id")long movieId,
                                @Query("append_to_response") String appendToResponseList);


}
