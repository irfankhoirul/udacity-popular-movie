package com.irfankhoirul.popularmovie.modules.movie_list;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.glidepalette.GlidePalette;
import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.util.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final static String TAG = MovieAdapter.class.getSimpleName();
    private final static String POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private final MovieClickListener listener;

    private List<Movie> movies = new ArrayList<>();
    private Context context;

    MovieAdapter(List<Movie> movies, MovieClickListener listener) {
        this.listener = listener;
        this.movies = movies;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MovieViewHolder holder, int position) {
        final Movie item = movies.get(position);

        holder.tvMovieTitle.setText(item.getTitle());
        GlideApp.with(context)
                .load(POSTER_PATH_BASE_URL + item.getPosterPath())
                .placeholder(R.drawable.ic_movie_paceholder)
                .listener(GlidePalette.with(POSTER_PATH_BASE_URL + item.getPosterPath())
                        .use(GlidePalette.Profile.VIBRANT)
                        .intoBackground(holder.tvMovieTitle, GlidePalette.Swatch.RGB)
                        .intoTextColor(holder.tvMovieTitle, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .crossfade(true)
                )
                .into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface MovieClickListener {
        void onMovieItemClick(Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
}
