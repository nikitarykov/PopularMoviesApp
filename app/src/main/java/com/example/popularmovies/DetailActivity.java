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

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);
        if (movie == null) {
            closeOnError();
        }

        TextView titleTextView = findViewById(R.id.tv_title);
        TextView overviewTextView = findViewById(R.id.tv_overview);
        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        TextView votesAverageTextView = findViewById(R.id.tv_votes_average);
        ImageView posterImageView = findViewById(R.id.iv_poster);

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
