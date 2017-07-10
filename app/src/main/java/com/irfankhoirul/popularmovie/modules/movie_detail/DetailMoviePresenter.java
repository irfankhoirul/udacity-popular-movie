package com.irfankhoirul.popularmovie.modules.movie_detail;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.RequestCallback;

import java.util.ArrayList;

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
    private Movie movie;
    private ArrayList<Review> reviews = new ArrayList<>();
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private int currentReviewPage = INITIAL_PAGE;
    private int totalReviewPage;

    DetailMoviePresenter(DetailMovieContract.View mView) {
        this.mView = mView;
        movieDataSource = new MovieDataSourceImpl();
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
        movieDataSource.getTrailer(id, new RequestCallback<Trailer>() {
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
                        onFailure();
                    }
                } else {
                    onFailure();
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
        movieDataSource.getReviews(id, page, new RequestCallback<Review>() {
            @Override
            public void onSuccess(DataResult<Review> dataResult) {
                if (dataResult != null) {
                    totalReviewPage = dataResult.getTotalPages();
                    if (dataResult.getResults().size() > 0) {
                        reviews.addAll(dataResult.getResults());
                        mView.updateReviewList();
                        mView.setReviewLoading(false);
                    } else {
                        onFailure();
                    }
                } else {
                    onFailure();
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
