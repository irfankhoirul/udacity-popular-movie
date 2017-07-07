package com.irfankhoirul.popularmovie.data.source.remote;

import com.irfankhoirul.popularmovie.BuildConfig;
import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public interface EndPoints {
    String MOVIE_END_POINT = "movie/{sort}?api_key=" + BuildConfig.TMDB_API_KEY;
    String TRAILER_END_POINT = "movie/{id}/videos?api_key=" + BuildConfig.TMDB_API_KEY;
    String REVIEW_END_POINT = "movie/{id}/reviews?api_key =" + BuildConfig.TMDB_API_KEY;

    @GET(MOVIE_END_POINT)
    Call<DataResult<Movie>> getMovies(@Path("sort") String sort);

    @GET(TRAILER_END_POINT)
    Call<DataResult<Trailer>> getTrailer(@Path("id") int id);

    @GET(REVIEW_END_POINT)
    Call<DataResult<Review>> getReviews(@Path("id") int id);
}
