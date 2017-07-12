package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.database.Cursor;
import android.net.Uri;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

/**
 * Created by Irfan Khoirul on 7/10/2017.
 */

public interface FavoriteMovieDataSource {
    // Query
    void getById(long id, FavoriteDataObserver<Cursor> cursorFavoriteDataObserver);

    // Query All
    void getAll(FavoriteDataObserver<Cursor> cursorFavoriteDataObserver);

    // Insert
    void insert(Movie movie, FavoriteDataObserver<Uri> uriFavoriteDataObserver);

    // Update
    void delete(long id, FavoriteDataObserver<Integer> integerFavoriteDataObserver);
}
