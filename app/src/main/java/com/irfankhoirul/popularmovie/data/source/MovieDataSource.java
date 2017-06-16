package com.irfankhoirul.popularmovie.data.source;

import android.support.annotation.NonNull;
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

    private static final String TAG = MovieDataSource.class.getSimpleName();
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

    @SuppressWarnings("unchecked")
    private void execute(Call<DataResult> call, final RequestCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess((DataResult) response.body());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                callback.onFailure();
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
