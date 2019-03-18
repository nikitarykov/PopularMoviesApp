package com.example.popularmovies.network;

import com.example.popularmovies.network.response.MovieListResponse;
import com.example.popularmovies.network.response.ReviewListResponse;
import com.example.popularmovies.network.response.VideoListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<VideoListResponse> getVideos(@Path("id") Integer id, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewListResponse> getReviews(@Path("id") Integer id, @Query("api_key") String api_key);

}
