package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

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

import com.example.popularmovies.adapter.MovieAdapter;
import com.example.popularmovies.domain.Movie;
import com.example.popularmovies.domain.SortType;
import com.example.popularmovies.viewmodel.MainViewModel;

import static com.example.popularmovies.DetailActivity.EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String SPINNER_SELECTED_POSITION = "spinner-selected-position";
    private static final int NOT_SELECTED_POSITION = -1;
    private int selectedPosition = NOT_SELECTED_POSITION;

    @BindView(R.id.recyclerview_movies) RecyclerView recyclerView;
    @BindView(R.id.tv_error_message) TextView errorMessageTextView;
    @BindView(R.id.pb_loading_indicator) ProgressBar loadingIndicator;

    @BindArray(R.array.sort_types) String[] sortTypes;
    @BindString(R.string.highest_rated_sort) String highestRatedSort;
    @BindString(R.string.most_popular_sort) String mostPopularSort;
    @BindString(R.string.favorite_sort) String favoriteSort;

    private MovieAdapter movieAdapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(SPINNER_SELECTED_POSITION);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, resource -> {
            switch (resource.status) {
                case LOADING: {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    break;
                }
                case SUCCESS: {
                    movieAdapter.setMovies(resource.data);
                    recyclerView.scrollToPosition(0);
                    showMoviesGrid();
                    break;
                }
                case ERROR: {
                    showErrorMessage();
                    break;
                }
            }
        });
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
            if (selectedPosition != NOT_SELECTED_POSITION) {
                spinner.setSelection(selectedPosition);
                spinner.setTag(R.id.pos, selectedPosition);
            }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object tag = spinner.getTag(R.id.pos);
                    // prevent reloading data on configuration changes
                    if (tag == null || (int) tag != position) {
                        selectedPosition = position;
                        String sortType = sortTypes[position];
                        if (sortType.equals(mostPopularSort)) {
                            viewModel.setSortType(SortType.MOST_POPULAR);
                        } else if (sortType.equals(highestRatedSort)) {
                            viewModel.setSortType(SortType.HIGHEST_RATED);
                        } else if (sortType.equals(favoriteSort)) {
                            viewModel.setSortType(SortType.FAVORITE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // do nothing
                }
            });
        }
        return true;
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_SELECTED_POSITION, selectedPosition);
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
