package com.example.popularmovies;

import com.example.popularmovies.domain.MoviesListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDatabaseApi {

    @GET("movie/popular")
    Call<MoviesListResponse> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<MoviesListResponse> getTopRatedMovies(@Query("api_key") String api_key);

}
