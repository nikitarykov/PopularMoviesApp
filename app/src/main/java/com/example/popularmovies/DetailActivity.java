package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    @BindView(R.id.tv_title) TextView titleTextView;
    @BindView(R.id.tv_overview) TextView overviewTextView;
    @BindView(R.id.tv_release_date) TextView releaseDateTextView;
    @BindView(R.id.tv_votes_average) TextView votesAverageTextView;
    @BindView(R.id.iv_poster) ImageView posterImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);
        if (movie == null) {
            closeOnError();
        }

        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getOverview());
        String releaseYear = movie.getReleaseDate().substring(0, 4);
        releaseDateTextView.setText(releaseYear);
        votesAverageTextView.setText(getString(R.string.votes_average_format, movie.getVotesAverage()));

        Picasso.with(this)
                .load(getString(R.string.base_image_url) + movie.getPosterPath())
                .into(posterImageView);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
