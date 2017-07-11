package com.irfankhoirul.popularmovie.modules.movie_detail;

import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;

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

interface DetailMovieContract {
    interface View {
        void setHasTrailer(Trailer trailer, String backdropUrl);

        void setTrailerLoading(boolean status);

        void setReviewLoading(boolean status);

        void updateReviewList();

        void updateTrailerList();

        void showNoReview();

        void showNoTrailer();

        void updateFavoriteStatus(boolean isFavorite);

        void showError(String message);
    }

    interface Presenter {
        Movie getMovie();

        void setMovie(Movie movie);

        void getReviews(long id, int page);

        int getCurrentReviewPage();

        ArrayList<Review> getReviewList();

        void setReviewList(ArrayList<Review> reviews);

        void getTrailer(long id);

        ArrayList<Trailer> getTrailerList();

        void setTrailerList(ArrayList<Trailer> trailers);

        String getTrailerLink(Trailer trailer);

        void addToFavorite(long date);

        void removeFromFavorite();

        void checkIsFavoriteMovie();

        void setIsFavoriteMovie();

        boolean isFavoriteMovie();
    }
}
