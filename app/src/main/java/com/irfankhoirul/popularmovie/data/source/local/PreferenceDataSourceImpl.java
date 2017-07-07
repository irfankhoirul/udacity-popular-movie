package com.irfankhoirul.popularmovie.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Irfan Khoirul on 7/7/2017.
 */

public class PreferenceDataSourceImpl implements PreferenceDataSource {
    private static String SHARED_PREFERENCE_NAME = "PreferenceDataSourceImpl";
    private SharedPreferences sharedPreferences;

    public PreferenceDataSourceImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    @Override
    public void setSortPreference(String sort) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sort", sort);
        editor.apply();
    }

    @Override
    public String getSortPreference() {
        return sharedPreferences.getString("sort", null);
    }
}
