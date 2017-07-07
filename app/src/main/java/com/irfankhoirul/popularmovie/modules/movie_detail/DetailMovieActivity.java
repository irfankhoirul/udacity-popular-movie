package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
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
    @BindView(R.id.trailer_backdrop_loading_progress)
    AVLoadingIndicatorView trailerBackdropLoadingProgress;
    @BindView(R.id.review_loading_progress)
    AVLoadingIndicatorView reviewLoadingProgress;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.rv_trailers)
    RecyclerView rvTrailers;
    @BindView(R.id.ll_no_trailer)
    LinearLayout llNoTrailer;
    @BindView(R.id.trailer_loading_progress)
    AVLoadingIndicatorView trailerLoadingProgress;
    @BindView(R.id.fl_trailer)
    FrameLayout flTrailer;
    @BindView(R.id.ll_no_review)
    LinearLayout llNoReview;
    @BindView(R.id.fl_review)
    FrameLayout flReview;

    private DetailMovieContract.Presenter presenter;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        presenter = new DetailMoviePresenter(this);

        setupToolbar();

        setupBackdropImage();

        setupTrailerRecyclerView();

        setupReviewRecyclerView();

        setupMovieData(savedInstanceState);

    }

    private void setupMovieData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable("movie") != null) {
                presenter.setMovie((Movie) savedInstanceState.getParcelable("movie"));
            }
            if (savedInstanceState.getParcelableArrayList("trailers") != null) {
                ArrayList<Trailer> trailers = savedInstanceState.getParcelableArrayList("trailers");
                presenter.setTrailerList(trailers);
                setTrailerLoading(false);
                setHasTrailer();
            }
            if (savedInstanceState.getParcelableArrayList("reviews") != null) {
                ArrayList<Review> reviews = savedInstanceState.getParcelableArrayList("reviews");
                presenter.setReviewList(reviews);
                setReviewLoading(false);
            }
            showMovieData();
        } else if (getIntent() != null && getIntent().hasExtra("movie")) {
            presenter.setMovie((Movie) getIntent().getParcelableExtra("movie"));

            presenter.getTrailer(presenter.getMovie().getId());

            presenter.getReviews(presenter.getMovie().getId());

            showMovieData();
        }
    }

    private void showMovieData() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(presenter.getMovie().getTitle());
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
                .load(POSTER_PATH_BASE_URL + presenter.getMovie().getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(ivMoviePoster);

        Picasso.with(this)
                .load(BACKDROP_PATH_BASE_URL + presenter.getMovie().getBackdropPath())
                .placeholder(R.drawable.image_placeholder)
                .into(ivMovieBackdrop);

        tvMovieTitle.setText(presenter.getMovie().getOriginalTitle());

        String averageRating = getString(R.string.label_average_rating) + presenter.getMovie().getVoteAverage();
        tvAverageRating.setText(averageRating);

        tvReleaseDate.setText(DateUtil.formatDate(presenter.getMovie().getReleaseDate(),
                "yyyy-M-dd", "dd MMMM yyyy"));

        tvSynopsis.setText(presenter.getMovie().getOverview());
    }

    private void setupTrailerRecyclerView() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTrailers.setLayoutManager(layoutManager);
        trailerAdapter = new TrailerAdapter(presenter.getTrailerList(), new TrailerAdapter.TrailerClickListener() {
            @Override
            public void onTrailerItemClick(Trailer trailer) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(presenter.getTrailerLink(trailer))));
            }
        });
        rvTrailers.setAdapter(trailerAdapter);
        rvTrailers.setNestedScrollingEnabled(false);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvTrailers);
    }

    private void setupReviewRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(layoutManager);
        reviewAdapter = new ReviewAdapter(presenter.getReviewList(),
                new ReviewAdapter.ReviewClickListener() {
                    @Override
                    public void onReviewItemClick(Review review) {
                        String url = review.getUrl();
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(DetailMovieActivity.this,
                                R.color.colorPrimary));
                        builder.setSecondaryToolbarColor(ContextCompat.getColor(DetailMovieActivity.this,
                                R.color.colorPrimary));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(DetailMovieActivity.this, Uri.parse(url));
                    }
                });
        rvReviews.setAdapter(reviewAdapter);
        rvReviews.setNestedScrollingEnabled(false);
    }

    private void setupBackdropImage() {
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", presenter.getMovie());
        outState.putParcelableArrayList("trailers", presenter.getTrailerList());
        outState.putParcelableArrayList("reviews", presenter.getReviewList());
        super.onSaveInstanceState(outState);
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
            trailerLoadingProgress.setVisibility(View.VISIBLE);
            trailerBackdropLoadingProgress.setVisibility(View.VISIBLE);
        } else {
            trailerLoadingProgress.setVisibility(View.GONE);
            trailerBackdropLoadingProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void setReviewLoading(boolean status) {
        if (status) {
            reviewLoadingProgress.setVisibility(View.VISIBLE);
        } else {
            reviewLoadingProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateReviewList() {
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateTrailerList() {
        trailerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoReview() {
        rvReviews.setVisibility(View.GONE);
        llNoReview.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoTrailer() {
        rvTrailers.setVisibility(View.GONE);
        llNoTrailer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_movie_backdrop)
    public void setIvMovieBackdropClick() {
        if (presenter.getTrailerList() != null && presenter.getTrailerList().size() > 0 &&
                presenter.getTrailerLink(presenter.getTrailerList().get(0)) != null) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(presenter.getTrailerLink(presenter.getTrailerList().get(0)))));
        }
    }
}
