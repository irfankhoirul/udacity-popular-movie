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

import android.database.Cursor;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteDataObserver;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieDataSource;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.local.preference.PreferenceDataSource;
import com.irfankhoirul.popularmovie.data.source.local.preference.PreferenceDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.RemoteDataObserver;
import com.irfankhoirul.popularmovie.util.ConstantUtil;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

import static com.irfankhoirul.popularmovie.modules.movie_list.ListMovieViewState.STATE_IDLE;
import static com.irfankhoirul.popularmovie.modules.movie_list.ListMovieViewState.STATE_LOADING_FAVORITE;
import static com.irfankhoirul.popularmovie.modules.movie_list.ListMovieViewState.STATE_LOADING_MOVIE;

class ListMoviePresenter implements ListMovieContract.Presenter {

    public static final int INITIAL_PAGE = 1;
    private ListMovieContract.View mView;
    private MovieDataSource movieDataSource;
    private PreferenceDataSource preferenceDataSource;
    private FavoriteMovieDataSource favoriteMovieDataSource;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ListMovieActivity activity;
    private int currentPage = INITIAL_PAGE;
    private int totalPage;

    public ListMoviePresenter(ListMovieContract.View mView, ListMovieActivity activity) {
        this.mView = mView;
        this.activity = activity;
        movieDataSource = new MovieDataSourceImpl();
        preferenceDataSource = new PreferenceDataSourceImpl(activity);
        favoriteMovieDataSource = new FavoriteMovieDataSourceImpl(activity);
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
        mView.setCurrentState(STATE_LOADING_MOVIE);
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
        final String tmpSortPreference = getSortPreference();
        setSortPreference(sort);
        movieDataSource.getMovies(sort, page, new RemoteDataObserver<Movie>() {
            @Override
            public void onNext(@NonNull DataResult<Movie> dataResult) {
                try {
                    if (dataResult != null) {
                        hideLoading(page); //Expected exception here if when activity recreated
                        totalPage = dataResult.getTotalPages();
                        currentPage = dataResult.getPage();
                        movies.addAll(dataResult.getResults());
                        mView.updateMovieList();
                        mView.setCurrentState(STATE_IDLE);
                    } else {
                        onError(new NullPointerException("Data Result Is Null!"));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
                try {
                    setSortPreference(tmpSortPreference);
                    hideLoading(page);
                    mView.showError(activity.getString(R.string.message_error_load_data));
                    mView.setCurrentState(STATE_IDLE);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void addMovie(Movie movie) {
        movies.add(0, movie);
        mView.updateMovieList();
    }

    @Override
    public void removeMovie(Movie movie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movie.getId() == movies.get(i).getId()) {
                movies.remove(i);
                break;
            }
        }
        mView.updateMovieList();
    }

    @Override
    public void getFavoriteMovies() {
        mView.setCurrentState(STATE_LOADING_FAVORITE);
        mView.setLoading(true, activity.getString(R.string.message_loading_favorite_movies));
        final String tmpSortPreference = getSortPreference();
        setSortPreference(ConstantUtil.SORT_FAVORITE);
        favoriteMovieDataSource.getAll(new FavoriteDataObserver<Cursor>() {
            @Override
            public void onNext(@NonNull Cursor o) {
                try {
                    movies.clear();
                    if (o.getCount() > 0) {
                        while (o.moveToNext()) {
                            Movie movie = new Movie();
                            movie.setId(o.getLong(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_ID)));
                            movie.setDateAdded(o.getLong(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_DATE_ADDED)));
                            movie.setAdult(o.getInt(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_ADULT)) == 1);
                            movie.setBackdropPath(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                            movie.setOriginalLanguage(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
                            movie.setOriginalTitle(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                            movie.setOverview(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_OVERVIEW)));
                            movie.setPopularity(o.getDouble(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_POPULARITY)));
                            movie.setPosterPath(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                            movie.setReleaseDate(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                            movie.setTitle(o.getString(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_TITLE)));
                            movie.setVideo(o.getInt(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_VIDEO)) == 1);
                            movie.setVoteAverage(o.getDouble(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                            movie.setVoteCount(o.getInt(o.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_VOTE_COUNT)));

                            movies.add(movie);
                        }
                    }
                    o.close();

                    mView.setLoading(false, null);
                    mView.updateMovieList();
                    mView.setCurrentState(STATE_IDLE);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
                try {
                    setSortPreference(tmpSortPreference);
                    mView.setCurrentState(STATE_IDLE);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
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
    public void setCurrentPage(int page) {
        currentPage = page;
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
