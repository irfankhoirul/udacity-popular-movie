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

package com.irfankhoirul.popularmovie.data.source.local.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceDataSourceImpl implements PreferenceDataSource {
    private static final String SHARED_PREFERENCE_NAME = "PreferenceDataSourceImpl";
    private SharedPreferences sharedPreferences;

    public PreferenceDataSourceImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    @Override
    public String getSortPreference() {
        return sharedPreferences.getString("sort", null);
    }

    @Override
    public void setSortPreference(String sort) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sort", sort);
        editor.apply();
    }
}
