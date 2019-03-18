package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.adapter.ReviewAdapter;
import com.example.popularmovies.adapter.VideoAdapter;
import com.example.popularmovies.domain.Movie;
import com.example.popularmovies.network.Resource;
import com.example.popularmovies.domain.Video;
import com.example.popularmovies.viewmodel.DetailViewModel;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler {

    public static final String EXTRA_MOVIE = "extra_movie";

    @BindView(R.id.tv_title) TextView titleTextView;
    @BindView(R.id.tv_overview) TextView overviewTextView;
    @BindView(R.id.tv_release_date) TextView releaseDateTextView;
    @BindView(R.id.tv_votes_average) TextView votesAverageTextView;
    @BindView(R.id.iv_poster) ImageView posterImageView;
    @BindView(R.id.ib_favorite) ImageButton favoriteButton;
    @BindView(R.id.tv_videos_label) TextView videoLabelTextView;
    @BindView(R.id.recyclerview_videos) RecyclerView videoRecyclerView;
    @BindView(R.id.tv_reviews_label) TextView reviewLabelTextView;
    @BindView(R.id.recyclerview_reviews) RecyclerView reviewRecyclerView;

    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private DetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        favoriteButton.setOnClickListener(view -> {
            favoriteButton.setSelected(!favoriteButton.isSelected());
            if (favoriteButton.isSelected()) {
                viewModel.favoriteMovie();
            } else {
                viewModel.unfavoriteMovie();
            }
        });

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);
        if (movie == null) {
            closeOnError();
        }

        LinearLayoutManager videoLayoutManager = new LinearLayoutManager(this);
        videoRecyclerView.setLayoutManager(videoLayoutManager);
        videoRecyclerView.setHasFixedSize(true);
        videoRecyclerView.setNestedScrollingEnabled(false);
        videoAdapter = new VideoAdapter(this);
        videoRecyclerView.setAdapter(videoAdapter);

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setNestedScrollingEnabled(false);
        reviewAdapter = new ReviewAdapter();
        reviewRecyclerView.setAdapter(reviewAdapter);

        boolean fetchData = savedInstanceState == null;
        setupViewModel(movie, fetchData);
    }

    private void setupViewModel(Movie movie, boolean fetchData) {
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.getMovie().observe(this, this::populateUI);
        viewModel.getVideos().observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                    videoAdapter.setVideos(resource.data);
                    videoLabelTextView.setVisibility(View.VISIBLE);
                    videoRecyclerView.setVisibility(View.VISIBLE);
            }
            if (resource.data == null || resource.data.isEmpty()) {
                videoLabelTextView.setVisibility(View.INVISIBLE);
                videoRecyclerView.setVisibility(View.INVISIBLE);
            }
        });
        viewModel.getReviews().observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                reviewAdapter.setReviews(resource.data);
                reviewLabelTextView.setVisibility(View.VISIBLE);
                reviewRecyclerView.setVisibility(View.VISIBLE);
            }
            if (resource.data == null || resource.data.isEmpty()) {
                reviewLabelTextView.setVisibility(View.INVISIBLE);
                reviewRecyclerView.setVisibility(View.INVISIBLE);
            }
        });
        if (fetchData) {
            viewModel.setMovie(movie);
            viewModel.loadVideos();
            viewModel.loadReviews();
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie) {
        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getOverview());
        String releaseYear = movie.getReleaseDate().substring(0, 4);
        releaseDateTextView.setText(releaseYear);
        votesAverageTextView.setText(getString(R.string.votes_average_format, movie.getVotesAverage()));
        favoriteButton.setSelected(viewModel.isFavoriteMovie(movie));

        Picasso.with(this)
                .load(getString(R.string.base_image_url) + movie.getPosterPath())
                .into(posterImageView);
    }

    @Override
    public void onClick(Video video) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.base_youtube_uri)).buildUpon()
                        .appendQueryParameter("v", video.getKey())
                        .build());
        startActivity(webIntent);
    }
}
