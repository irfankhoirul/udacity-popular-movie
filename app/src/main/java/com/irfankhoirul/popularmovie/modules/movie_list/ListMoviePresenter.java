package com.irfankhoirul.popularmovie.modules.movie_list;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.source.local.preference.PreferenceDataSource;
import com.irfankhoirul.popularmovie.data.source.local.preference.PreferenceDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.RemoteDataObserver;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

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

class ListMoviePresenter implements ListMovieContract.Presenter {

    public static final int INITIAL_PAGE = 1;
    private ListMovieContract.View mView;
    private MovieDataSource movieDataSource;
    private PreferenceDataSource preferenceDataSource;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ListMovieActivity activity;
    private int currentPage = INITIAL_PAGE;
    private int totalPage;

    public ListMoviePresenter(ListMovieContract.View mView, ListMovieActivity activity) {
        movieDataSource = new MovieDataSourceImpl();
        preferenceDataSource = new PreferenceDataSourceImpl(activity);
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
    public void getMovies(final String sort, final int page) {
        if (totalPage != 0) {
            if (page > totalPage) {
                return;
            }
        }
        if (page == INITIAL_PAGE) {
            movies.clear();
            if (sort.equals(MovieDataSourceImpl.SORT_POPULAR)) {
                mView.setLoading(true, activity.getString(R.string.message_loading_popular_movies));
            } else if (sort.equals(MovieDataSourceImpl.SORT_TOP_RATED)) {
                mView.setLoading(true, activity.getString(R.string.message_loading_top_rated_movies));
            }
        } else {
            mView.setLoadMore(true);
        }
        movieDataSource.getMovies(sort, page, new RemoteDataObserver<Movie>() {
            @Override
            public void onNext(@NonNull DataResult<Movie> dataResult) {
                if (dataResult != null) {
                    totalPage = dataResult.getTotalPages();
                    hideLoading(page);
                    setSortPreference(sort);
                    currentPage = dataResult.getPage();
                    movies.addAll(dataResult.getResults());
                    mView.updateMovieList();
                } else {
                    onError(new NullPointerException("Data Result Is Null!"));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
                hideLoading(page);
                mView.showError(activity.getString(R.string.message_error_load_data));
            }
        });
    }

    private void hideLoading(int page) {
        if (page == INITIAL_PAGE) {
            mView.setLoading(false, null);
        } else {
            mView.setLoadMore(false);
        }
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public String getSortPreference() {
        return preferenceDataSource.getSortPreference();
    }

    @Override
    public void setSortPreference(String sort) {
        preferenceDataSource.setSortPreference(sort);
    }

}
