package com.irfankhoirul.popularmovie.modules.movie_list;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 6/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final static String TAG = MovieAdapter.class.getSimpleName();
    private final static String POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private final MovieClickListener listener;

    private List<Movie> movies = new ArrayList<>();
    private int defaultTitleTextColor;
    private int defaultTitleBackgroundColor;

    public MovieAdapter(List<Movie> movies, MovieClickListener listener) {
        this.listener = listener;
        this.movies = movies;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (defaultTitleBackgroundColor == 0) {
            defaultTitleBackgroundColor = ContextCompat.getColor(
                    parent.getContext(), R.color.colorPrimary);
        }
        if (defaultTitleTextColor == 0) {
            defaultTitleTextColor = ContextCompat.getColor(
                    parent.getContext(), android.R.color.white);
        }

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MovieViewHolder holder, int position) {
        final Movie item = movies.get(position);

        holder.tvMovieTitle.setText(item.getTitle());
        Picasso.with(holder.itemView.getContext())
                .load(POSTER_PATH_BASE_URL + item.getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.ivMoviePoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                holder.tvMovieTitle.setBackgroundColor(
                                        palette.getVibrantColor(defaultTitleBackgroundColor));
                                holder.tvMovieTitle.setTextColor(
                                        palette.getLightMutedColor(defaultTitleTextColor));
                            }
                        };

                        Bitmap posterBitmap = ((BitmapDrawable) holder.ivMoviePoster.getDrawable())
                                .getBitmap();
                        if (posterBitmap != null && !posterBitmap.isRecycled()) {
                            Palette.from(posterBitmap).generate(paletteListener);
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, holder.itemView.getContext().getString(R.string.message_error));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.cv_movie)
        CardView cvMovie;
        @BindView(R.id.iv_movie_poster)
        ImageView ivMoviePoster;
        @BindView(R.id.tv_movie_title)
        TextView tvMovieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onMovieItemClick(movies.get(getAdapterPosition()));
        }
    }

    public interface MovieClickListener {
        void onMovieItemClick(Movie movie);
    }
}
