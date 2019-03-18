package com.example.popularmovies.di;

import com.example.popularmovies.MovieRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={RepositoryModule.class})
public interface AppComponent {
    MovieRepository getRepository();
}
