package com.irfankhoirul.popularmovie.data.source.local;

/**
 * Created by Irfan Khoirul on 7/7/2017.
 */

public interface PreferenceDataSource {
    void setSortPreference(String sort);

    String getSortPreference();
}
