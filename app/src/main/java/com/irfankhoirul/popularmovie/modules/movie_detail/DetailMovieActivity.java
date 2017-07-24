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

package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.util.DisplayMetricUtils;
import com.irfankhoirul.popularmovie.util.GlideApp;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.irfankhoirul.popularmovie.util.ConstantUtil.BACKDROP_PATH_BASE_URL;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.SHOW_DETAIL_CHANGED_RESULT_CODE;

public class DetailMovieActivity extends AppCompatActivity
        implements DetailMovieFragment.MovieDetailFragmentListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.iv_movie_backdrop)
    ImageView ivMovieBackdrop;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fl_header)
    FrameLayout flHeader;
    @BindView(R.id.trailer_backdrop_loading_progress)
    AVLoadingIndicatorView trailerBackdropLoadingProgress;
    @BindView(R.id.iv_trailer_play_button)
    ImageView ivTrailerPlayButton;

    private Trailer trailer;
    private DetailMovieFragment fragment;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "detailMovieFragment", fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setupToolbar();

        setupFragment(savedInstanceState);

        setupBackdropImage();

    }

    private void setupFragment(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragment = DetailMovieFragment.newInstance((Movie) getIntent().getParcelableExtra("movie"), false);
        } else {
            fragment = (DetailMovieFragment) getSupportFragmentManager().getFragment(savedInstanceState, "detailMovieFragment");
        }
        fragmentManager.beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();
    }

    private void setupBackdropImage() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float positiveVerticalOffset = verticalOffset * -1.0f;
                int color = Color.argb((int) positiveVerticalOffset, 255, 255, 255);
                toolbarLayout.setExpandedTitleColor(color);
            }
        });

        ViewGroup.LayoutParams appBarLayoutParams = appBar.getLayoutParams();
        appBarLayoutParams.height = (int) ((9.0f / 16.0f) *
                DisplayMetricUtils.getDeviceWidth(this));
        appBar.setLayoutParams(appBarLayoutParams);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.toolbar_title_movie_detail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowItem(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onTrailerLoaded(Trailer trailer, String backdropUrl) {
        trailerBackdropLoadingProgress.setVisibility(View.GONE);
        ivTrailerPlayButton.setVisibility(View.VISIBLE);
        this.trailer = trailer;

        GlideApp.with(this)
                .load(BACKDROP_PATH_BASE_URL + backdropUrl)
                .placeholder(R.drawable.ic_movie_paceholder)
                .into(ivMovieBackdrop);

        flHeader.setClickable(true);
    }

    @Override
    public void onFavoriteChanged(Movie movie, boolean isFavorite) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("movie", movie);

        if (!isFavorite) {
            setResult(SHOW_DETAIL_CHANGED_RESULT_CODE, resultIntent);
        } else {
            setResult(Activity.RESULT_OK);
        }

        if (isFavorite) {
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        fab.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fl_header)
    public void setFlHeaderClick() {
        if (trailer != null) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
        }
    }

    @OnClick(R.id.fab)
    public void setFabClick() {
        if (fragment != null) {
            fragment.setFabClick();
        }
    }

}
