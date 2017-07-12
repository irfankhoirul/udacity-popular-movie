package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.COLUMN_ID;
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

@Dao
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
