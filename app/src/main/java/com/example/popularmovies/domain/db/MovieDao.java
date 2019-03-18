package com.example.popularmovies.domain.db;

import com.example.popularmovies.domain.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Insert(onConflict = REPLACE)
    void save(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("select * from movies where id = :movieId")
    Movie getMovie(Integer movieId);

    @Query("select * from movies")
    LiveData<List<Movie>> loadAllMovies();
}
