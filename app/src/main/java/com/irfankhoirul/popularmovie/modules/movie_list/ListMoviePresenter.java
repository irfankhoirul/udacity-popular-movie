package com.irfankhoirul.popularmovie.modules.movie_list;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.source.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.RequestCallback;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public class ListMoviePresenter implements ListMovieContract.Presenter {

    private ListMovieContract.View mView;
    private MovieDataSource movieDataSource;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ListMovieActivity activity;

    public ListMoviePresenter(ListMovieContract.View mView, ListMovieActivity activity) {
        movieDataSource = new MovieDataSource();
        this.mView = mView;
        this.activity = activity;
    }

    @Override
    public ArrayList<Movie> getMovieList() {
        return movies;
    }

    @Override
    public void setMovieList(ArrayList<Movie> movies) {
        this.movies.addAll(movies);
        mView.updateMovieList();
    }

    @Override
    public void getPopularMovies() {
        mView.setLoading(true, activity.getString(R.string.message_loading_popular_movies));
        movieDataSource.getPopularMovies(new RequestCallback() {
            @Override
            public void onSuccess(DataResult dataResult) {
                movies.clear();
                movies.addAll(dataResult.getResults());
                mView.updateMovieList();
                mView.setLoading(false, null);
            }

            @Override
            public void onFailure() {
                mView.setLoading(false, null);
                mView.showError(activity.getString(R.string.message_error_load_data));
            }
        });
    }

    @Override
    public void getTopRatedMovies() {
        mView.setLoading(true, activity.getString(R.string.message_loading_top_rated_movies));
        movieDataSource.getTopRatedMovies(new RequestCallback() {
            @Override
            public void onSuccess(DataResult dataResult) {
                movies.clear();
                movies.addAll(dataResult.getResults());
                mView.updateMovieList();
                mView.setLoading(false, null);
            }

            @Override
            public void onFailure() {
                mView.setLoading(false, null);
                mView.showError(activity.getString(R.string.message_error_load_data));
            }
        });
    }
}
