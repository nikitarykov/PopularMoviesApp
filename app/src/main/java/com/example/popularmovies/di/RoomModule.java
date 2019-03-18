package com.example.popularmovies.di;

import android.app.Application;

import com.example.popularmovies.domain.db.MovieDao;
import com.example.popularmovies.domain.db.MovieDatabase;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

import static com.example.popularmovies.domain.db.MovieDatabase.DATABASE_NAME;

@Module(includes = AppModule.class)
public class RoomModule {

    @Singleton
    @Provides
    MovieDatabase provideMovieDatabase(Application application) {
        return Room.databaseBuilder(application, MovieDatabase.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    MovieDao provideMovieDao(MovieDatabase movieDatabase) {
        return movieDatabase.movieDao();
    }
}
