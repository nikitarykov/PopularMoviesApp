package com.example.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.domain.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public ReviewAdapter() {
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author) TextView authorTextView;
        @BindView(R.id.tv_content) TextView contentTextView;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        Context context = holder.authorTextView.getContext();
        holder.authorTextView.append(context.getString(R.string.by));
        holder.authorTextView.append(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
