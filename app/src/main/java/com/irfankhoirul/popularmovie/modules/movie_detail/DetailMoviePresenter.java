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

package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteDataObserver;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieDataSource;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.RemoteDataObserver;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

class DetailMoviePresenter implements DetailMovieContract.Presenter {

    public static final int ACTION_ADD_TO_FAVORITE = 1;
    public static final int ACTION_REMOVE_FROM_FAVORITE = 2;
    public static final int INITIAL_PAGE = 1;
    private DetailMovieContract.View mView;
    private MovieDataSource movieDataSource;
    private FavoriteMovieDataSource favoriteMovieDataSource;
    private Movie movie;
    private ArrayList<Review> reviews = new ArrayList<>();
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private int currentReviewPage = INITIAL_PAGE;
    private int totalReviewPage;
    private boolean isFavoriteMovie;
    private boolean isTrailerLoaded;
    private boolean isReviewLoaded;

    DetailMoviePresenter(DetailMovieContract.View mView, Context context) {
        this.mView = mView;
        movieDataSource = new MovieDataSourceImpl();
        favoriteMovieDataSource = new FavoriteMovieDataSourceImpl(context);
    }

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public ArrayList<Review> getReviewList() {
        return reviews;
    }

    @Override
    public void setReviewList(ArrayList<Review> reviews) {
        this.reviews.addAll(reviews);
        mView.updateReviewList();
    }

    @Override
    public void getTrailer(long id) {
        mView.setTrailerLoading(true);
        movieDataSource.getTrailer(id, new RemoteDataObserver<Trailer>() {
            @Override
            public void onNext(@NonNull DataResult<Trailer> dataResult) {
                try {
                    mView.setTrailerLoading(false);
                    if (dataResult != null) {
                        isTrailerLoaded = true;
                        if (dataResult.getResults().size() > 0) {
                            trailers.clear();
                            for (int i = 0; i < dataResult.getResults().size(); i++) {
                                if (dataResult.getResults().get(i).getSite().equals("YouTube")) {
                                    trailers.add(dataResult.getResults().get(i));
                                }
                            }
                            mView.setHasTrailer(trailers.get(0), movie.getBackdropPath());
                            mView.updateTrailerList();
                            mView.setTrailerLoading(false);
                        } else {
                            onError(new NullPointerException("No Trailer Found!"));
                        }
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
                    mView.setTrailerLoading(false);
                    mView.showNoTrailer();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public ArrayList<Trailer> getTrailerList() {
        return trailers;
    }

    @Override
    public void setTrailerList(ArrayList<Trailer> trailers) {
        this.trailers.addAll(trailers);
        mView.updateReviewList();
    }

    @Override
    public String getTrailerLink(Trailer trailer) {
        return "http://www.youtube.com/watch?v=" + trailer.getKey();
    }

    @Override
    public void addToFavorite(long date) {
        movie.setDateAdded(date);
        favoriteMovieDataSource.insert(movie, new FavoriteDataObserver<Uri>() {
            @Override
            public void onNext(@NonNull Uri o) {
                try {
                    isFavoriteMovie = true;
                    mView.showSuccess(ACTION_ADD_TO_FAVORITE, "Added to Favorite");
                    mView.updateFavoriteStatus(false, true);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
                try {
                    mView.showError("Failed to add to Favorite");
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void removeFromFavorite() {
        favoriteMovieDataSource.delete(movie.getId(), new FavoriteDataObserver<Integer>() {
            @Override
            public void onNext(@NonNull Integer o) {
                try {
                    isFavoriteMovie = false;
                    mView.showSuccess(ACTION_REMOVE_FROM_FAVORITE, "Removed from Favorite");
                    mView.updateFavoriteStatus(false, false);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
                try {
                    mView.showError("Failed to remove from Favorite");
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void checkIsFavoriteMovie() {
        favoriteMovieDataSource.getById(movie.getId(), new FavoriteDataObserver<Cursor>() {
            @Override
            public void onNext(@NonNull Cursor o) {
                try {
                    if (o.getCount() > 0) {
                        o.moveToFirst();
                        isFavoriteMovie = true;
                        mView.updateFavoriteStatus(true, true);
                    } else {
                        isFavoriteMovie = false;
                        mView.updateFavoriteStatus(true, false);
                    }
                    o.close();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
                try {
                    mView.updateFavoriteStatus(true, false);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setIsFavoriteMovie() {
        isFavoriteMovie = true;
    }

    @Override
    public boolean isFavoriteMovie() {
        return isFavoriteMovie;
    }

    @Override
    public boolean isReviewLoaded() {
        return isReviewLoaded;
    }

    @Override
    public void setReviewLoaded(boolean isReviewLoaded) {
        this.isReviewLoaded = isReviewLoaded;
    }

    @Override
    public boolean isTrailerLoaded() {
        return isTrailerLoaded;
    }

    @Override
    public void setTrailerLoaded(boolean isTrailerLoaded) {
        this.isTrailerLoaded = isTrailerLoaded;
    }

    @Override
    public void getReviews(long id, int page) {
        if (totalReviewPage != 0) {
            if (page > totalReviewPage) {
                return;
            }
        }
        if (page == INITIAL_PAGE) {
            reviews.clear();
        }
        mView.setReviewLoading(true);
        movieDataSource.getReviews(id, page, new RemoteDataObserver<Review>() {
            @Override
            public void onNext(@NonNull DataResult<Review> dataResult) {
                try {
                    if (dataResult != null) {
                        isReviewLoaded = true;
                        totalReviewPage = dataResult.getTotalPages();
                        if (dataResult.getResults().size() > 0) {
                            reviews.addAll(dataResult.getResults());
                            mView.updateReviewList();
                            mView.setReviewLoading(false);
                        } else {
                            onError(new NullPointerException("No Review Found!"));
                        }
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
                    mView.setReviewLoading(false);
                    mView.showNoReview();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getCurrentReviewPage() {
        return currentReviewPage;
    }
}
