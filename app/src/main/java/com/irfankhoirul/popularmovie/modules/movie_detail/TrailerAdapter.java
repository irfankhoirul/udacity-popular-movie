package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.util.DisplayMetricUtils;
import com.squareup.picasso.Picasso;

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

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailers = new ArrayList<>();
    private TrailerClickListener clickListener;

    TrailerAdapter(List<Trailer> trailers, TrailerClickListener clickListener) {
        this.trailers = trailers;
        this.clickListener = clickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);

        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        String imageThumbnailUrl = "http://img.youtube.com/vi/"
                .concat(trailer.getKey())
                .concat("/hqdefault.jpg");
        Picasso.with(holder.ivTrailerThumbnail.getContext())
                .load(imageThumbnailUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.ivTrailerThumbnail);

        ViewGroup.LayoutParams ivTrailerThumbnailLayoutParams =
                holder.ivTrailerThumbnail.getLayoutParams();
        ivTrailerThumbnailLayoutParams.height = (int) ((9.0f / 16.0f) *
                (DisplayMetricUtils.getDeviceWidth(holder.ivTrailerThumbnail.getContext()) / 2.0f));
        holder.ivTrailerThumbnail.setLayoutParams(ivTrailerThumbnailLayoutParams);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    interface TrailerClickListener {
        void onTrailerItemClick(Trailer trailer);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_trailer_thumbnail)
        ImageView ivTrailerThumbnail;
        @BindView(R.id.iv_trailer_play_button)
        ImageView ivTrailerPlayButton;

        TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onTrailerItemClick(trailers.get(getAdapterPosition()));
        }
    }

}
