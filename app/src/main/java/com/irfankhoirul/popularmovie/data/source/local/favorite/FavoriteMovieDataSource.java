package com.irfankhoirul.popularmovie.data.source.local.favorite;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

/**
 * Created by Irfan Khoirul on 7/10/2017.
 */

public interface FavoriteMovieDataSource {
    // Query
    void getById(long id);

    // Query All
    void getAll();

    // Insert
    void insert(Movie movie);

    // Update
    void delete(long id);
}
