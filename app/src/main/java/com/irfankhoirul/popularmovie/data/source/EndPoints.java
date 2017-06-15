package com.irfankhoirul.popularmovie.data.source;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public interface EndPoints {
    String THE_MOVIE_DB_API_KEY = "74fedab3fd9268320c52a2b5e37435d3";
    String POPULAR_MOVIE_END_POINT = "movie/popular?api_key=" + THE_MOVIE_DB_API_KEY;
    String TOP_RATED_MOVIE_END_POINT = "movie/top_rated?api_key=" + THE_MOVIE_DB_API_KEY;

    @GET(POPULAR_MOVIE_END_POINT)
    Call<DataResult> getPopularMovies();

    @GET(TOP_RATED_MOVIE_END_POINT)
    Call<DataResult> getTopRatedMovies();
}
