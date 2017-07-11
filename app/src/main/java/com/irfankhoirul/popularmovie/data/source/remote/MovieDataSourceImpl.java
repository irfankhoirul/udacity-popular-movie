package com.irfankhoirul.popularmovie.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

public class MovieDataSourceImpl implements MovieDataSource {
    public static final String SORT_POPULAR = "popular";
    public static final String SORT_TOP_RATED = "top_rated";

    private static final String TAG = MovieDataSourceImpl.class.getSimpleName();
    private EndPoints endPoint;

    public MovieDataSourceImpl() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        String BASE_URL = "https://api.themoviedb.org/3/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        endPoint = retrofit.create(EndPoints.class);
    }

    @SuppressWarnings("unchecked")
    private <T> void execute(Call<DataResult<T>> call, final RequestCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess((DataResult<T>) response.body());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                callback.onFailure();
            }
        });
    }

    @Override
    public void getMovies(String sort, int page, RequestCallback callback) {
        Call<DataResult<Movie>> call = endPoint.getMovies(sort, page);
        execute(call, callback);
    }

    @Override
    public void getTrailer(long id, RequestCallback callback) {
        Call<DataResult<Trailer>> call = endPoint.getTrailer(id);
        execute(call, callback);
    }

    @Override
    public void getReviews(long id, int page, RequestCallback callback) {
        Call<DataResult<Review>> call = endPoint.getReviews(id, page);
        execute(call, callback);
    }

}
