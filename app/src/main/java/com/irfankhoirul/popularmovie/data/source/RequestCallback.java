package com.irfankhoirul.popularmovie.data.source;

import com.irfankhoirul.popularmovie.data.pojo.DataResult;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public interface RequestCallback {
    void onSuccess(DataResult dataResult);

    void onFailure();
}
