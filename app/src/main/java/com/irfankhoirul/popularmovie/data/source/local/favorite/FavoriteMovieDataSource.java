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

package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.database.Cursor;
import android.net.Uri;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

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
