package com.example.popularmovies.viewmodel;

import android.app.Application;

import com.example.popularmovies.MovieApp;
import com.example.popularmovies.domain.Movie;
import com.example.popularmovies.MovieRepository;
import com.example.popularmovies.network.response.MovieListResponse;
import com.example.popularmovies.network.Resource;
import com.example.popularmovies.domain.SortType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private MediatorLiveData<Resource<List<Movie>>> movies;
    private Callback<MovieListResponse> movieFetchCallback;
    private LiveData<List<Movie>> favoriteMovies;

    private final MovieRepository movieRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRepository = ((MovieApp) application).getAppComponent().getRepository();
        favoriteMovies = movieRepository.getFavoriteMovies();
        movies = new MediatorLiveData<>();
        movies.setValue(Resource.loading(null));
        movieFetchCallback = new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response.body() == null) {
                    movies.setValue(Resource.error(null));
                } else {
                    movies.setValue(Resource.success(response.body().getResults()));
                }
            }
            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable throwable) {
                movies.setValue(Resource.error(null));
            }
        };
    }

    public void setSortType(SortType sortType) {
        movies.removeSource(favoriteMovies);
        switch (sortType) {
            case MOST_POPULAR: {
                movies.setValue(Resource.loading(movies.getValue().data));
                movieRepository.getPopularMovies(movieFetchCallback);
                break;
            }
            case HIGHEST_RATED: {
                movies.setValue(Resource.loading(movies.getValue().data));
                movieRepository.getTopRatedMovies(movieFetchCallback);
                break;
            }
            case FAVORITE: {
                movies.addSource(favoriteMovies, newData -> movies.setValue(Resource.success(newData)));
                break;
            }
        }
    }

    public LiveData<Resource<List<Movie>>> getMovies() {
        return movies;
    }
}
