package com.irfankhoirul.popularmovie.data.source.remote;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public interface RequestCallback<T> {
    void onSuccess(DataResult<T> dataResult);

    void onFailure();
}
