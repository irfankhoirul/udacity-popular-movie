package com.irfankhoirul.popularmovie.modules.movie_list;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.source.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.RequestCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public class ListMoviePresenter implements ListMovieContract.Presenter {

    private ListMovieContract.View mView;
    private MovieDataSource movieDataSource;
    private List<Movie> movies = new ArrayList<>();

    public ListMoviePresenter(ListMovieContract.View mView) {
        movieDataSource = new MovieDataSource();
        this.mView = mView;
    }

    @Override
    public List<Movie> getMovieList() {
        return movies;
    }

    @Override
    public void getPopularMovies() {
        mView.setLoading(true, "Getting Popular Movies");
        movieDataSource.getPopularMovies(new RequestCallback() {
            @Override
            public void onSuccess(DataResult dataResult) {
                movies.clear();
                movies.addAll(dataResult.getResults());
                mView.updateMovieList();
                mView.setLoading(false, null);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mView.setLoading(false, null);
                mView.showError("Cannot load data");
            }
        });
    }

    @Override
    public void getTopRatedMovies() {
        mView.setLoading(true, "Getting Top-Rated Movies");
        movieDataSource.getTopRatedMovies(new RequestCallback() {
            @Override
            public void onSuccess(DataResult dataResult) {
                movies.clear();
                movies.addAll(dataResult.getResults());
                mView.updateMovieList();
                mView.setLoading(false, null);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mView.setLoading(false, null);
                mView.showError("Cannot load data");
            }
        });
    }
}
