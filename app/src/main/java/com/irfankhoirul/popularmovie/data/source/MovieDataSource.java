package com.irfankhoirul.popularmovie.data.source;

import android.util.Log;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public class MovieDataSource {

    private EndPoints endPoint;

    public MovieDataSource() {
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

    private void execute(Call<DataResult> call, final RequestCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                callback.onSuccess((DataResult) response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error", t.getMessage());
                callback.onFailure(t);
            }
        });
    }

    public void getPopularMovies(RequestCallback callback) {
        Call<DataResult> call = endPoint.getPopularMovies();
        execute(call, callback);
    }

    public void getTopRatedMovies(RequestCallback callback) {
        Call<DataResult> call = endPoint.getTopRatedMovies();
        execute(call, callback);
    }

}
