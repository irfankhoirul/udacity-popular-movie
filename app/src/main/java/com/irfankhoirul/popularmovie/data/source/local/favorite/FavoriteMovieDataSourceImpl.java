package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

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

public class FavoriteMovieDataSourceImpl implements FavoriteMovieDataSource {

    private Context context;

    public FavoriteMovieDataSourceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getById(final long id, FavoriteDataObserver<Cursor> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Cursor> e) throws Exception {
                String stringId = Long.toString(id);
                Uri uri = FavoriteMovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(stringId).build();
                Cursor cursor = context.getContentResolver()
                        .query(uri,
                                null,
                                FavoriteMovieContract.MovieEntry.COLUMN_ID + " = ?",
                                new String[]{String.valueOf(id)},
                                FavoriteMovieContract.MovieEntry.COLUMN_DATE_ADDED + " DESC");

                if (cursor != null) {
                    Log.d("QueryCursor", String.valueOf(cursor.getCount()));
                    e.onNext(cursor);
                } else {
                    e.onError(new NullPointerException("Failed to query data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    @Override
    public void getAll(FavoriteDataObserver<Cursor> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Cursor> e) throws Exception {
                Cursor cursor = context.getContentResolver()
                        .query(FavoriteMovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                FavoriteMovieContract.MovieEntry.COLUMN_DATE_ADDED + " DESC");

                if (cursor != null) {
                    Log.d("QueryCursor", String.valueOf(cursor.getCount()));
                    e.onNext(cursor);
                } else {
                    e.onError(new NullPointerException("Failed to query all data"));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    @Override
    public void insert(final Movie movie, FavoriteDataObserver<Uri> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Uri> e) throws Exception {
                ContentValues contentValues = new ContentValues();
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ID, movie.getId());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_VIDEO, movie.isVideo());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_ADULT, movie.isAdult());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_DATE_ADDED, movie.getDateAdded());

                Uri uri = context.getContentResolver()
                        .insert(FavoriteMovieContract.MovieEntry.CONTENT_URI, contentValues);
                if (uri != null) {
                    Log.d("InsertUri", uri.toString());
                    e.onNext(uri);
                } else {
                    e.onError(new NullPointerException("Failed to insert data"));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    @Override
    public void delete(final long id, FavoriteDataObserver<Integer> integerFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                String stringId = Long.toString(id);
                Uri uri = FavoriteMovieContract.MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                int count = context.getContentResolver().delete(uri, null, null);

                if (count != 0) {
                    Log.d("DeleteCount", String.valueOf(count));
                    e.onNext(count);
                } else {
                    e.onError(new NullPointerException("Failed to delete data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerFavoriteDataObserver);
    }
}
