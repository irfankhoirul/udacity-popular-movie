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

package com.irfankhoirul.popularmovie.modules.movie_list;

import android.support.annotation.Nullable;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

import java.util.ArrayList;

interface ListMovieContract {
    interface View {
        void setCurrentState(String state);

        void updateMovieList();

        void setLoading(boolean status, @Nullable String message);

        void setLoadMore(boolean status);

        void showError(String message);
    }

    interface Presenter {
        ArrayList<Movie> getMovieList();

        void setMovieList(ArrayList<Movie> movies);

        void getMovies(String sort, int page);

        void addMovie(Movie movie);

        void removeMovie(Movie movie);

        void getFavoriteMovies();

        int getCurrentPage();

        void setCurrentPage(int page);

        String getSortPreference();

        void setSortPreference(String sort);
    }
}
