package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.util.DateUtil;
import com.irfankhoirul.popularmovie.util.DisplayMetricUtils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.irfankhoirul.popularmovie.R.id.tv_release_date;

public class DetailMovieActivity extends AppCompatActivity
        implements DetailMovieContract.View {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private static final String POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private static final String BACKDROP_PATH_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @BindView(R.id.iv_movie_poster)
    ImageView ivMoviePoster;
    @BindView(R.id.tv_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.tv_average_rating)
    TextView tvAverageRating;
    @BindView(tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_synopsis)
    TextView tvSynopsis;
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
    @BindView(R.id.iv_trailer_play_button)
    ImageView ivTrailerPlayButton;
    @BindView(R.id.loading_progress)
    AVLoadingIndicatorView loadingProgressTrailer;

    DetailMovieContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        presenter = new DetailMoviePresenter(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.toolbar_title_movie_detail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ViewGroup.LayoutParams appBarLayoutParams = appBar.getLayoutParams();
        appBarLayoutParams.height = (int) ((9.0f / 16.0f) * DisplayMetricUtils.getDeviceWidth(this));
        appBar.setLayoutParams(appBarLayoutParams);

        if (getIntent() != null && getIntent().hasExtra("movie")) {
            final Movie movie = getIntent().getParcelableExtra("movie");

            presenter.getTrailer(movie.getId());

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movie.getTitle());
            }

            appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float positiveVerticalOffset = verticalOffset * -1.0f;
                    int color = Color.argb((int) positiveVerticalOffset, 255, 255, 255);
                    toolbarLayout.setExpandedTitleColor(color);
                }
            });

            Picasso.with(this)
                    .load(POSTER_PATH_BASE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivMoviePoster);

            Picasso.with(this)
                    .load(BACKDROP_PATH_BASE_URL + movie.getBackdropPath())
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivMovieBackdrop);

            tvMovieTitle.setText(movie.getOriginalTitle());

            String averageRating = getString(R.string.label_average_rating) + movie.getVoteAverage();
            tvAverageRating.setText(averageRating);

            tvReleaseDate.setText(DateUtil.formatDate(movie.getReleaseDate(),
                    "yyyy-M-dd", "dd MMMM yyyy"));

            tvSynopsis.setText(movie.getOverview());
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
    public void setHasTrailer() {
        ivTrailerPlayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTrailerLoading(boolean status) {
        if (status) {
            loadingProgressTrailer.setVisibility(View.VISIBLE);
        } else {
            loadingProgressTrailer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReviews(ArrayList<Review> reviews) {

    }

    @OnClick(R.id.iv_movie_backdrop)
    public void setIvMovieBackdropClick() {
        if (presenter.getTrailerLink() != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(presenter.getTrailerLink())));
        }
    }
}
