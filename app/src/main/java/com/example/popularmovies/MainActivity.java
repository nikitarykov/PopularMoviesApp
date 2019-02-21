package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.popularmovies.domain.Movie;
import com.example.popularmovies.domain.MoviesListResponse;

import static com.example.popularmovies.DetailActivity.EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private TextView errorMessageTextView;
    private ProgressBar loadingIndicator;
    private MovieAdapter movieAdapter;
    private MovieDatabaseApi movieDatabaseApi;
    private String[] sortTypes;
    private String mostPopularSort;
    private String highestRatedSort;
    private String movieDatabaseApiKey;
    private Callback<MoviesListResponse> apiCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview_movies);
        errorMessageTextView = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        sortTypes = getResources().getStringArray(R.array.sort_types);
        mostPopularSort = getString(R.string.most_popular_sort);
        highestRatedSort = getString(R.string.highest_rated_sort);
        movieDatabaseApiKey = getString(R.string.movie_db_api_key);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_movie_db_url))
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        movieDatabaseApi = retrofit.create(MovieDatabaseApi.class);
        apiCallback = new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(Call<MoviesListResponse> call, Response<MoviesListResponse> response) {
                if (response.body() == null) {
                    showErrorMessage();
                } else {
                    movieAdapter.setMovies(response.body().getResults());
                    recyclerView.scrollToPosition(0);
                    showMoviesGrid();
                }
            }
            @Override
            public void onFailure(Call<MoviesListResponse> call, Throwable throwable) {
                showErrorMessage();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        MenuItem spinnerItem = menu.findItem(R.id.sort_spinner);
        View actionView = spinnerItem.getActionView();
        if (actionView instanceof Spinner) {
            Spinner spinner = (Spinner) actionView;
            spinner.setAdapter(ArrayAdapter.createFromResource(this,
                    R.array.sort_types, android.R.layout.simple_spinner_item));
            spinner.setOnItemSelectedListener(this);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sortType = sortTypes[position];
        loadMovies(sortType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    private void loadMovies(String sortType) {
        loadingIndicator.setVisibility(View.VISIBLE);
        if (sortType.equals(mostPopularSort)) {
            movieDatabaseApi.getPopularMovies(movieDatabaseApiKey).enqueue(apiCallback);
        } else if (sortType.equals(highestRatedSort)) {
            movieDatabaseApi.getTopRatedMovies(movieDatabaseApiKey).enqueue(apiCallback);
        }
    }

    private void showMoviesGrid() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

}