package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_ID;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by Irfan Khoirul on 7/8/2017.
 */

public interface FavoriteMovieDao {

    @Query("SELECT COUNT(*) FROM " + TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Movie movie);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Movie[] movies);

    @Query("SELECT * FROM " + TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Movie movie);
}
