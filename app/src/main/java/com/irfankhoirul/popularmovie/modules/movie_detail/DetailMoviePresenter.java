package com.irfankhoirul.popularmovie.modules.movie_detail;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSource;
import com.irfankhoirul.popularmovie.data.source.remote.MovieDataSourceImpl;
import com.irfankhoirul.popularmovie.data.source.remote.RequestCallback;

/**
 * Created by Irfan Khoirul on 7/7/2017.
 */

public class DetailMoviePresenter implements DetailMovieContract.Presenter {

    DetailMovieContract.View mView;
    MovieDataSource movieDataSource;
    Trailer trailer;

    public DetailMoviePresenter(DetailMovieContract.View mView) {
        this.mView = mView;
        movieDataSource = new MovieDataSourceImpl();
    }

    @Override
    public void getTrailer(int id) {
        mView.setTrailerLoading(true);
        movieDataSource.getTrailer(id, new RequestCallback<Trailer>() {
            @Override
            public void onSuccess(DataResult<Trailer> dataResult) {
                mView.setTrailerLoading(false);
                if (dataResult != null) {
                    if (dataResult.getResults().size() > 0) {
                        trailer = dataResult.getResults().get(0);
                        mView.setHasTrailer();
                    }
                } else {
                    onFailure();
                }
            }

            @Override
            public void onFailure() {
                mView.setTrailerLoading(false);
            }
        });
    }

    @Override
    public String getTrailerLink() {
        if (trailer != null) {
            return "http://www.youtube.com/watch?v=" + trailer.getKey();
        }
        return null;
    }

    @Override
    public void getReviews(int id) {

    }
}
