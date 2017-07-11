package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.content.Context;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieDataSource;
import com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.RequestCallback;

import java.util.ArrayList;

import static io.reactivex.plugins.RxJavaPlugins.onError;

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

class DetailMoviePresenter implements DetailMovieContract.Presenter {

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
        movieDataSource.getTrailer(id, new RequestCallback<DataResult<Trailer>>() {
            @Override
            public void onSuccess(DataResult<Trailer> dataResult) {
                mView.setTrailerLoading(false);
                if (dataResult != null) {
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
            }

            @Override
            public void onFailure() {
                mView.setTrailerLoading(false);
                mView.showNoTrailer();
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
//        movie.setDateAdded(date);
//        favoriteMovieDataSource.insert(movie, new FavoriteDataObserver<Uri>() {
//            @Override
//            public void onNext(@NonNull Uri o) {
//                mView.updateFavoriteStatus(true);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                super.onError(e);
//                mView.showError("Failed to add to Favorite");
//            }
//        });
    }

    @Override
    public void removeFromFavorite() {
//        favoriteMovieDataSource.delete(movie.getId(), new FavoriteDataObserver<Integer>() {
//            @Override
//            public void onNext(@NonNull Integer o) {
//                mView.updateFavoriteStatus(false);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                super.onError(e);
//                mView.showError("Failed to remove from Favorite");
//            }
//        });
    }

    @Override
    public void checkIsFavoriteMovie() {
//        favoriteMovieDataSource.getById(movie.getId(), new FavoriteDataObserver<Cursor>() {
//            @Override
//            public void onNext(@NonNull Cursor o) {
//                if (o.getCount() > 0) {
//                    o.moveToFirst();
//                    isFavoriteMovie = true;
//                    mView.updateFavoriteStatus(true);
//                } else {
//                    isFavoriteMovie = false;
//                    mView.updateFavoriteStatus(false);
//                }
//                o.close();
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                super.onError(e);
//                mView.updateFavoriteStatus(false);
//                Log.v("IsFavorite", "Error");
//            }
//        });
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
        movieDataSource.getReviews(id, page, new RequestCallback<DataResult<Review>>() {
            @Override
            public void onSuccess(DataResult<Review> dataResult) {
                if (dataResult != null) {
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
            }

            @Override
            public void onFailure() {
                mView.setReviewLoading(false);
                mView.showNoReview();
            }
        });
    }

    @Override
    public int getCurrentReviewPage() {
        return currentReviewPage;
    }
}
