package com.example.popularmovies.domain.db;

import com.example.popularmovies.domain.Movie;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "movie-database";

    public abstract MovieDao movieDao();
}