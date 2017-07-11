package com.irfankhoirul.popularmovie.data.source.local.favorite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.irfankhoirul.popularmovie.data.pojo.Movie;

/**
 * Created by Irfan Khoirul on 7/8/2017.
 */

@Database(entities = {Movie.class}, version = 1)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    /**
     * The only instance
     */
    private static FavoriteMovieDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized FavoriteMovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), FavoriteMovieDatabase.class,
                            "movie")
                    .build();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                FavoriteMovieDatabase.class).build();
    }

    @SuppressWarnings("WeakerAccess")
    public abstract FavoriteMovieDao movie();

}
