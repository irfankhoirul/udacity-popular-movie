package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

/**
 * Created by Irfan Khoirul on 7/11/2017.
 */

public class FavoriteMovieDataSourceImpl implements FavoriteMovieDataSource {

    private Context context;

    public FavoriteMovieDataSourceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getById(final long id) {
        String stringId = Long.toString(id);
        Uri uri = FavoriteMovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(stringId).build();
        Cursor cursor = context.getContentResolver()
                .query(uri,
                        null,
                        FavoriteMovieContract.MovieEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null);

        if (cursor != null) {
            Log.d("QueryCursor", String.valueOf(cursor.getCount()));
            //Handle Success
        } else {
            //HandleError
        }
    }

    @Override
    public void getAll() {
        Cursor cursor = context.getContentResolver()
                .query(FavoriteMovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        FavoriteMovieContract.MovieEntry.COLUMN_DATE_ADDED + " DESC");

        if (cursor != null) {
            Log.d("QueryCursor", String.valueOf(cursor.getCount()));
            //Handle success
        } else {
            //Handle error
        }
    }

    @Override
    public void insert(final Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_VIDEO, movie.isVideo());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ADULT, movie.isAdult());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_DATE_ADDED, movie.getDateAdded());

        Uri uri = context.getContentResolver()
                .insert(FavoriteMovieContract.MovieEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Log.d("InsertUri", uri.toString());
            //Handle success
        } else {
            //handle error
        }
    }

    @Override
    public void delete(final long id) {
        String stringId = Long.toString(id);
        Uri uri = FavoriteMovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        int count = context.getContentResolver().delete(uri, null, null);

        if (count != 0) {
            Log.d("DeleteCount", String.valueOf(count));
            //handle success
        } else {
            //handle error
        }

    }
}
