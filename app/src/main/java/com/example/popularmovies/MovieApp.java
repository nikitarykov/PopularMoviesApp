package com.example.popularmovies;

import android.app.Application;

import com.example.popularmovies.di.AppComponent;
import com.example.popularmovies.di.AppModule;
import com.example.popularmovies.di.DaggerAppComponent;
import com.example.popularmovies.di.NetModule;

public class MovieApp extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(getString(R.string.base_movie_db_url)))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}

