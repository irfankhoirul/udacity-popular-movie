package com.irfankhoirul.popularmovie.modules.movie_detail;

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

public class DetailMovieActivity extends AppCompatActivity implements DetailMovieFragment.DetailMovieFragmentListener {

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

    @OnClick(R.id.fl_header)
    public void setFlHeaderClick() {
        if (trailer != null) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
        }
    }
}
