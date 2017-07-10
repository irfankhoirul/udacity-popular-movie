package com.irfankhoirul.popularmovie.data.source.remote;

import com.irfankhoirul.popularmovie.BuildConfig;
import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

interface EndPoints {
    String MOVIE_END_POINT = "movie/{sort}?api_key=" + BuildConfig.TMDB_API_KEY;
    String TRAILER_END_POINT = "movie/{id}/videos?api_key=" + BuildConfig.TMDB_API_KEY;
    String REVIEW_END_POINT = "movie/{id}/reviews?api_key=" + BuildConfig.TMDB_API_KEY;

    @GET(MOVIE_END_POINT)
    Call<DataResult<Movie>> getMovies(@Path("sort") String sort, @Query("page") int page);

    @GET(TRAILER_END_POINT)
    Call<DataResult<Trailer>> getTrailer(@Path("id") long id);

    @GET(REVIEW_END_POINT)
    Call<DataResult<Review>> getReviews(@Path("id") long id, @Query("page") int page);
}
