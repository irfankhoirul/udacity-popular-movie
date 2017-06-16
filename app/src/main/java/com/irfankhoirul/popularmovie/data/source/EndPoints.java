package com.irfankhoirul.popularmovie.data.source;

import com.irfankhoirul.popularmovie.BuildConfig;
import com.irfankhoirul.popularmovie.data.pojo.DataResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public interface EndPoints {
    String POPULAR_MOVIE_END_POINT = "movie/popular?api_key=" + BuildConfig.TMDB_API_KEY;
    String TOP_RATED_MOVIE_END_POINT = "movie/top_rated?api_key=" + BuildConfig.TMDB_API_KEY;

    @GET(POPULAR_MOVIE_END_POINT)
    Call<DataResult> getPopularMovies();

    @GET(TOP_RATED_MOVIE_END_POINT)
    Call<DataResult> getTopRatedMovies();
}
