package com.example.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(tableName = "movies")
public class Movie implements Parcelable {
    @PrimaryKey
    private Integer id;
    private String title;
    private String releaseDate;
    private String posterPath;
    private Double votesAverage;
    private String overview;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("release_date")
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @JsonProperty("poster_path")
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @JsonProperty("vote_average")
    public Double getVotesAverage() {
        return votesAverage;
    }

    public void setVotesAverage(Double votesAverage) {
        this.votesAverage = votesAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeDouble(votesAverage);
        parcel.writeString(overview);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            Movie movie = new Movie();
            movie.setId(parcel.readInt());
            movie.setTitle(parcel.readString());
            movie.setReleaseDate(parcel.readString());
            movie.setPosterPath(parcel.readString());
            movie.setVotesAverage(parcel.readDouble());
            movie.setOverview(parcel.readString());
            return movie;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
