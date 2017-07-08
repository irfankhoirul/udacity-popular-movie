package com.irfankhoirul.popularmovie.data.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_ADULT;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_BACKDROP_PATH;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_ID;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_OVERVIEW;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_POPULARITY;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_POSTER_PATH;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_TITLE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_VIDEO;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_VOTE_COUNT;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.TABLE_NAME;

/*
 * Copyright 2017.  Irfan Khoirul Muhlishin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Entity(tableName = TABLE_NAME)
public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(android.os.Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    @SerializedName("id")
    @Expose
    public long id;

    @ColumnInfo(name = COLUMN_VOTE_COUNT)
    @SerializedName(COLUMN_VOTE_COUNT)
    @Expose
    private int voteCount;

    @ColumnInfo(name = COLUMN_VIDEO)
    @SerializedName(COLUMN_VIDEO)
    @Expose
    private boolean video;

    @ColumnInfo(name = COLUMN_VOTE_AVERAGE)
    @SerializedName(COLUMN_VOTE_AVERAGE)
    @Expose
    private double voteAverage;

    @ColumnInfo(name = COLUMN_TITLE)
    @SerializedName(COLUMN_TITLE)
    @Expose
    private String title;

    @ColumnInfo(name = COLUMN_POPULARITY)
    @SerializedName(COLUMN_POPULARITY)
    @Expose
    private double popularity;

    @ColumnInfo(name = COLUMN_POSTER_PATH)
    @SerializedName(COLUMN_POSTER_PATH)
    @Expose
    private String posterPath;

    @ColumnInfo(name = COLUMN_ORIGINAL_LANGUAGE)
    @SerializedName(COLUMN_ORIGINAL_LANGUAGE)
    @Expose
    private String originalLanguage;

    @ColumnInfo(name = COLUMN_ORIGINAL_TITLE)
    @SerializedName(COLUMN_ORIGINAL_TITLE)
    @Expose
    private String originalTitle;

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;

    @ColumnInfo(name = COLUMN_BACKDROP_PATH)
    @SerializedName(COLUMN_BACKDROP_PATH)
    @Expose
    private String backdropPath;

    @ColumnInfo(name = COLUMN_ADULT)
    @SerializedName(COLUMN_ADULT)
    @Expose
    private boolean adult;

    @ColumnInfo(name = COLUMN_OVERVIEW)
    @SerializedName(COLUMN_OVERVIEW)
    @Expose
    private String overview;

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    @SerializedName(COLUMN_RELEASE_DATE)
    @Expose
    private String releaseDate;

    public Movie() {
    }

    protected Movie(android.os.Parcel in) {
        voteCount = in.readInt();
        id = in.readLong();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static Movie fromContentValues(ContentValues values) {
        final Movie movie = new Movie();
        if (values.containsKey(COLUMN_ID)) {
            movie.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_VOTE_COUNT)) {
            movie.voteCount = values.getAsInteger(COLUMN_VOTE_COUNT);
        }
        if (values.containsKey(COLUMN_VIDEO)) {
            movie.video = values.getAsBoolean(COLUMN_VIDEO);
        }
        if (values.containsKey(COLUMN_VOTE_AVERAGE)) {
            movie.voteAverage = values.getAsDouble(COLUMN_VOTE_AVERAGE);
        }
        if (values.containsKey(COLUMN_TITLE)) {
            movie.title = values.getAsString(COLUMN_TITLE);
        }
        if (values.containsKey(COLUMN_POPULARITY)) {
            movie.popularity = values.getAsDouble(COLUMN_POPULARITY);
        }
        if (values.containsKey(COLUMN_POSTER_PATH)) {
            movie.posterPath = values.getAsString(COLUMN_POSTER_PATH);
        }
        if (values.containsKey(COLUMN_ORIGINAL_LANGUAGE)) {
            movie.originalLanguage = values.getAsString(COLUMN_ORIGINAL_LANGUAGE);
        }
        if (values.containsKey(COLUMN_ORIGINAL_TITLE)) {
            movie.originalTitle = values.getAsString(COLUMN_ORIGINAL_TITLE);
        }
        if (values.containsKey(COLUMN_BACKDROP_PATH)) {
            movie.backdropPath = values.getAsString(COLUMN_BACKDROP_PATH);
        }
        if (values.containsKey(COLUMN_ADULT)) {
            movie.adult = values.getAsBoolean(COLUMN_ADULT);
        }
        if (values.containsKey(COLUMN_OVERVIEW)) {
            movie.overview = values.getAsString(COLUMN_OVERVIEW);
        }
        if (values.containsKey(COLUMN_RELEASE_DATE)) {
            movie.releaseDate = values.getAsString(COLUMN_RELEASE_DATE);
        }
        return movie;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "voteCount=" + voteCount +
                ", id=" + id +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", genreIds=" + genreIds +
                ", backdropPath='" + backdropPath + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(voteCount);
        dest.writeLong(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }
}