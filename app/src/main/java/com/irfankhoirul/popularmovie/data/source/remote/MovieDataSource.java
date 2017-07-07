package com.irfankhoirul.popularmovie.data.source.remote;

/**
 * Created by Irfan Khoirul on 7/7/2017.
 */

public interface MovieDataSource {
    void getMovies(String sort, RequestCallback callback);

    void getTrailer(int id, RequestCallback callback);

    void getReviews(int id, RequestCallback callback);
}
