package com.example.popularmovies.di;

import com.example.popularmovies.network.MovieService;
import com.example.popularmovies.domain.db.MovieDao;
import com.example.popularmovies.MovieRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetModule.class, RoomModule.class})
public class RepositoryModule {

    @Provides
    @Singleton
    ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    MovieRepository provideRepository(MovieService movieService,
                                      MovieDao movieDao,
                                      ExecutorService executorService) {
        return new MovieRepository(movieService, movieDao, executorService);
    }
}
