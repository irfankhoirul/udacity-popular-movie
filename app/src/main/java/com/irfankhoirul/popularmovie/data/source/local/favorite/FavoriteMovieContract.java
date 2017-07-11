package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Irfan Khoirul on 7/8/2017.
 */

public class FavoriteMovieContract {

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_DATE_ADDED = "date_added";

        public static final String AUTHORITY = "com.irfankhoirul.popularmovie";

        public static final int CODE_MOVIE_DIRECTORY = 100;
        public static final int CODE_MOVIE_ITEM = 101;

        public static final Uri BASE_CONTENT_URI =
                Uri.parse("content://" + AUTHORITY);

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME)
                        .build();
    }

}
