package com.example.popularmovies.viewmodel;

import android.app.Application;

import com.example.popularmovies.MovieApp;
import com.example.popularmovies.domain.Movie;
import com.example.popularmovies.MovieRepository;
import com.example.popularmovies.domain.Review;
import com.example.popularmovies.network.Resource;
import com.example.popularmovies.domain.Video;
import com.example.popularmovies.network.response.ReviewListResponse;
import com.example.popularmovies.network.response.VideoListResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    private MutableLiveData<Movie> selectedMovie;
    private MutableLiveData<Resource<List<Video>>> videos;
    private MutableLiveData<Resource<List<Review>>> reviews;

    private Callback<VideoListResponse> videoFetchCallback;
    private Callback<ReviewListResponse> reviewFetchCallback;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        movieRepository = ((MovieApp) application).getAppComponent().getRepository();
        selectedMovie = new MutableLiveData<>();
        videos = new MutableLiveData<>();
        videos.setValue(Resource.loading(null));
        videoFetchCallback = new Callback<VideoListResponse>() {
            @Override
            public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                if (response.body() == null) {
                    videos.setValue(Resource.error(null));
                } else {
                    videos.setValue(Resource.success(response.body().getResults()));
                }
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                videos.setValue(Resource.error(null));
            }
        };
        reviews = new MutableLiveData<>();
        reviews.setValue(Resource.loading(null));
        reviewFetchCallback = new Callback<ReviewListResponse>() {
            @Override
            public void onResponse(Call<ReviewListResponse> call, Response<ReviewListResponse> response) {
                if (response.body() == null) {
                    reviews.setValue(Resource.error(null));
                } else {
                    reviews.setValue(Resource.success(response.body().getResults()));
                }
            }

            @Override
            public void onFailure(Call<ReviewListResponse> call, Throwable t) {
                reviews.setValue(Resource.error(null));
            }
        };
    }

    public LiveData<Movie> getMovie() {
        return selectedMovie;
    }

    public void setMovie(Movie movie) {
        selectedMovie.setValue(movie);
    }

    public boolean isFavoriteMovie(Movie movie) {
        return movieRepository.isFavoriteMovie(movie);
    }

    public void favoriteMovie() {
        movieRepository.favoriteMovie(selectedMovie.getValue());
    }

    public void unfavoriteMovie() {
        movieRepository.unfavoriteMovie(selectedMovie.getValue());
    }

    public void loadVideos() {
        movieRepository.loadVideos(selectedMovie.getValue(), videoFetchCallback);
    }

    public void loadReviews() {
        movieRepository.loadReviews(selectedMovie.getValue(), reviewFetchCallback);
    }

    public LiveData<Resource<List<Video>>> getVideos() {
        return videos;
    }

    public MutableLiveData<Resource<List<Review>>> getReviews() {
        return reviews;
    }
}
