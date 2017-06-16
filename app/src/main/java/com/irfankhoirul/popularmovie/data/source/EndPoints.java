package com.irfankhoirul.popularmovie.data.source;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public interface EndPoints {
    String THE_MOVIE_DB_API_KEY = "<your_the_movie_db_API_key_here>";
    String POPULAR_MOVIE_END_POINT = "movie/popular?api_key=" + THE_MOVIE_DB_API_KEY;
    String TOP_RATED_MOVIE_END_POINT = "movie/top_rated?api_key=" + THE_MOVIE_DB_API_KEY;

    @GET(POPULAR_MOVIE_END_POINT)
    Call<DataResult> getPopularMovies();

    @GET(TOP_RATED_MOVIE_END_POINT)
    Call<DataResult> getTopRatedMovies();
}
