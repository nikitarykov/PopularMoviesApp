package com.example.popularmovies;

import com.example.popularmovies.network.MovieService;
import com.example.popularmovies.domain.Movie;
import com.example.popularmovies.domain.db.MovieDao;
import com.example.popularmovies.network.response.MovieListResponse;
import com.example.popularmovies.network.response.ReviewListResponse;
import com.example.popularmovies.network.response.VideoListResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import androidx.lifecycle.LiveData;
import retrofit2.Callback;

public class MovieRepository {

    private static final String API_KEY = BuildConfig.API_KEY;

    private final MovieService movieService;
    private final MovieDao movieDao;
    private final ExecutorService executorService;

    public MovieRepository(MovieService movieService,
                           MovieDao movieDao,
                           ExecutorService executorService) {
        this.movieService = movieService;
        this.movieDao = movieDao;
        this.executorService = executorService;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.loadAllMovies();
    }

    public void getPopularMovies(Callback<MovieListResponse> callback) {
        movieService.getPopularMovies(API_KEY).enqueue(callback);
    }

    public void getTopRatedMovies(Callback<MovieListResponse> callback) {
        movieService.getTopRatedMovies(API_KEY).enqueue(callback);
    }

    public boolean isFavoriteMovie(Movie movie) {
        try {
            return executorService.submit(() -> movieDao.getMovie(movie.getId())).get() != null;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void favoriteMovie(Movie movie) {
        executorService.execute(() -> movieDao.save(movie));
    }

    public void unfavoriteMovie(Movie movie) {
        executorService.execute(() -> movieDao.delete(movie));
    }

    public void loadVideos(Movie movie, Callback<VideoListResponse> callback) {
        movieService.getVideos(movie.getId(), API_KEY).enqueue(callback);
    }

    public void loadReviews(Movie movie, Callback<ReviewListResponse> callback) {
        movieService.getReviews(movie.getId(), API_KEY).enqueue(callback);
    }
}
