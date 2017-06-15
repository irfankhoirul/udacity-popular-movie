package com.irfankhoirul.popularmovie.modules.movie_list;

import android.support.annotation.Nullable;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

import java.util.List;

/**
 * Created by Irfan Khoirul on 4/2/2017.
 */

public interface ListMovieContract {
    interface View {
        void updateMovieList();

        void setLoading(boolean status, @Nullable String message);

        void showError(String message);
    }

    interface Presenter {
        List<Movie> getMovieList();

        void getPopularMovies();

        void getTopRatedMovies();
    }
}
