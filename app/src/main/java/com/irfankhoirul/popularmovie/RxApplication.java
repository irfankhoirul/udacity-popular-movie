package com.irfankhoirul.popularmovie;

import android.app.Application;

import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;

/**
 * Created by Irfan Khoirul on 7/11/2017.
 */

public class RxApplication extends Application {

    private MovieDataSource movieDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        movieDataSource = new MovieDataSourceImpl();
    }

    public MovieDataSource getNetworkService() {
        return movieDataSource;
    }

}
