package com.irfankhoirul.popularmovie.modules.movie_detail;

import com.irfankhoirul.popularmovie.data.pojo.Review;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 7/7/2017.
 */

public interface DetailMovieContract {
    interface View {
        void setHasTrailer();

        void setTrailerLoading(boolean status);

        void showReviews(ArrayList<Review> reviews);
    }

    interface Presenter {
        void getTrailer(int id);

        String getTrailerLink();

        void getReviews(int id);
    }
}
