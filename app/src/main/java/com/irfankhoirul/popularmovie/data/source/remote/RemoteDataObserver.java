package com.irfankhoirul.popularmovie.data.source.remote;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Irfan Khoirul on 7/11/2017.
 */

public abstract class RemoteDataObserver<T> implements Observer<DataResult<T>> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull DataResult<T> dataResult) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
