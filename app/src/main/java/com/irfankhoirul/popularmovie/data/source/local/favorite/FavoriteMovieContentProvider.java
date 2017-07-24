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

package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

import java.util.ArrayList;

import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.AUTHORITY;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.CODE_MOVIE_DIRECTORY;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.CODE_MOVIE_ITEM;
import static com.irfankhoirul.popularmovie.data.source.local.favorite.FavoriteMovieContract.MovieEntry.TABLE_NAME;

public class FavoriteMovieContentProvider extends ContentProvider {

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_MOVIE_DIRECTORY);
        MATCHER.addURI(AUTHORITY, TABLE_NAME + "/#", CODE_MOVIE_ITEM);
    }

    private FavoriteMovieDao favoriteMovieDao;
    private Context context;

    @Override
    public boolean onCreate() {
        context = getContext();
        if (context == null) {
            return false;
        }
        favoriteMovieDao = FavoriteMovieDatabase.getInstance(context).movie();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_MOVIE_DIRECTORY || code == CODE_MOVIE_ITEM) {
            final Cursor cursor;
            if (code == CODE_MOVIE_DIRECTORY) {
                cursor = favoriteMovieDao.selectAll();
            } else {
                cursor = favoriteMovieDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIRECTORY:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME;
            case CODE_MOVIE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIRECTORY:
                final long id = favoriteMovieDao.insert(Movie.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                if (id > 0) {
                    return ContentUris.withAppendedId(uri, id);
                } else {
                    throw new SQLiteException("Failed to insert row into URI: " + uri);
                }
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIRECTORY:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MOVIE_ITEM:
                final int count = favoriteMovieDao.deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIRECTORY:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MOVIE_ITEM:
                final Movie movie = Movie.fromContentValues(values);
                movie.id = ContentUris.parseId(uri);
                final int count = favoriteMovieDao.update(movie);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final FavoriteMovieDatabase database = FavoriteMovieDatabase.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIRECTORY:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Movie[] movies = new Movie[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    movies[i] = Movie.fromContentValues(valuesArray[i]);
                }
                return favoriteMovieDao.insertAll(movies).length;
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
