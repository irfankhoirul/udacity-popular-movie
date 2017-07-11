package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.data.pojo.Review;
import com.irfankhoirul.popularmovie.data.pojo.Trailer;
import com.irfankhoirul.popularmovie.modules.movie_list.ListMovieActivity;
import com.irfankhoirul.popularmovie.util.DateUtil;
import com.irfankhoirul.popularmovie.util.DisplayMetricUtils;
import com.irfankhoirul.popularmovie.util.GlideApp;
import com.irfankhoirul.popularmovie.util.MultiPageRecyclerViewScrollListener;
import com.irfankhoirul.popularmovie.util.RecyclerViewMarginDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.irfankhoirul.popularmovie.R.id.iv_movie_backdrop;
import static com.irfankhoirul.popularmovie.R.id.tv_release_date;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.BACKDROP_PATH_BASE_URL;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.POSTER_PATH_BASE_URL;

/**
 * Created by Irfan Khoirul on 7/10/2017.
 */

public class DetailMovieFragment extends Fragment implements DetailMovieContract.View {

    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.fl_header)
    FrameLayout flHeader;
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
    @BindView(iv_movie_backdrop)
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
    @BindView(R.id.ll_review)
    LinearLayout llReview;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_vote_count)
    TextView tvVoteCount;

    private DetailMovieContract.Presenter presenter;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private MultiPageRecyclerViewScrollListener reviewsScrollListener;
    private FragmentActivity activity;
    private MovieListFragmentListener movieListFragmentListener;
    private MovieDetailFragmentListener movieDetailFragmentListener;
    private boolean isTablet;

    public static DetailMovieFragment newInstance(Movie movie, boolean isTablet) {
        DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        bundle.putBoolean("isTablet", isTablet);
        detailMovieFragment.setArguments(bundle);
        return detailMovieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments().getBoolean("isTablet")) {
            movieListFragmentListener = (ListMovieActivity) context;
        } else {
            movieDetailFragmentListener = (DetailMovieActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (movieListFragmentListener != null) {
            movieListFragmentListener = null;
        }
        if (movieDetailFragmentListener != null) {
            movieDetailFragmentListener = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", presenter.getMovie());
        outState.putParcelableArrayList("trailers", presenter.getTrailerList());
        outState.putParcelableArrayList("reviews", presenter.getReviewList());
        outState.putBoolean("isFavorite", presenter.isFavoriteMovie());
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        activity = getActivity();

        presenter = new DetailMoviePresenter(this, activity);

        if (getArguments() != null) {
            isTablet = getArguments().getBoolean("isTablet");
        }

        if (isTablet) {
            setupBackdropImage();
        } else {
            flHeader.setVisibility(View.GONE);
        }

        setupTrailerRecyclerView();

        setupReviewRecyclerView();

        setupMovieData(savedInstanceState);

        return view;
    }

    @OnClick(R.id.iv_movie_backdrop)
    public void setIvMovieBackdropClick() {
        if (presenter.getTrailerList() != null && presenter.getTrailerList().size() > 0 &&
                presenter.getTrailerLink(presenter.getTrailerList().get(0)) != null) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(presenter.getTrailerLink(presenter.getTrailerList().get(0)))));
        }
    }

    @OnClick(R.id.fab)
    public void setFabClick() {
        if (!presenter.isFavoriteMovie()) {
            presenter.addToFavorite(System.currentTimeMillis());
        } else {
            presenter.removeFromFavorite();
        }
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
                if (trailers != null && trailers.size() > 0) {
                    setHasTrailer(trailers.get(0), presenter.getMovie().getBackdropPath());
                }
            }
            if (savedInstanceState.getParcelableArrayList("reviews") != null) {
                ArrayList<Review> reviews = savedInstanceState.getParcelableArrayList("reviews");
                presenter.setReviewList(reviews);
                setReviewLoading(false);
            }
            if (savedInstanceState.getBoolean("isFavorite")) {
                presenter.setIsFavoriteMovie();
                updateFavoriteStatus(true);
            } else {
                presenter.checkIsFavoriteMovie();
            }
        } else if (getArguments() != null && getArguments().getParcelable("movie") != null) {
            presenter.setMovie((Movie) getArguments().getParcelable("movie"));

            presenter.checkIsFavoriteMovie();

            presenter.getTrailer(presenter.getMovie().getId());

            presenter.getReviews(presenter.getMovie().getId(), DetailMoviePresenter.INITIAL_PAGE);
        }

        showMovieData();
    }

    private void showMovieData() {

        if (isTablet) {
            if (movieListFragmentListener != null) {
                movieListFragmentListener.onShowItem(presenter.getMovie().getTitle());
            }
        } else {
            if (movieDetailFragmentListener != null) {
                movieDetailFragmentListener.onShowItem(presenter.getMovie().getTitle());
            }
        }

        setupPosterImage();

        GlideApp.with(this)
                .load(POSTER_PATH_BASE_URL + presenter.getMovie().getPosterPath())
                .placeholder(R.drawable.ic_movie_paceholder)
                .into(ivMoviePoster);

        GlideApp.with(this)
                .load(BACKDROP_PATH_BASE_URL + presenter.getMovie().getBackdropPath())
                .placeholder(R.drawable.ic_movie_paceholder)
                .into(ivMovieBackdrop);

        tvMovieTitle.setText(presenter.getMovie().getOriginalTitle());

        String averageRating = String.valueOf(presenter.getMovie().getVoteAverage());
        tvAverageRating.setText(averageRating);
        ratingBar.setRating((float) presenter.getMovie().getVoteAverage() / 2.0f);
        tvVoteCount.setText(String.valueOf(presenter.getMovie().getVoteCount()));
        tvReleaseDate.setText(DateUtil.formatDate(presenter.getMovie().getReleaseDate(),
                "yyyy-M-dd", "dd MMMM yyyy"));
        tvSynopsis.setText(presenter.getMovie().getOverview());
    }

    private void setupTrailerRecyclerView() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rvTrailers.setLayoutManager(layoutManager);
        int marginInPixel = DisplayMetricUtils.convertDpToPixel(4);
        int column = 1;
        RecyclerViewMarginDecoration decoration =
                new RecyclerViewMarginDecoration(RecyclerViewMarginDecoration.ORIENTATION_HORIZONTAL,
                        marginInPixel, column);
        rvTrailers.addItemDecoration(decoration);
        trailerAdapter = new TrailerAdapter(presenter.getTrailerList(), getActivity(), isTablet,
                new TrailerAdapter.TrailerClickListener() {
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        rvReviews.setLayoutManager(layoutManager);
        reviewAdapter = new ReviewAdapter(presenter.getReviewList(),
                new ReviewAdapter.ReviewClickListener() {
                    @Override
                    public void onReviewItemClick(Review review) {
                        String url = review.getUrl();
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(activity,
                                R.color.colorPrimary));
                        builder.setSecondaryToolbarColor(ContextCompat.getColor(activity,
                                R.color.colorPrimary));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(activity, Uri.parse(url));
                    }
                });
        rvReviews.setAdapter(reviewAdapter);
        rvReviews.setNestedScrollingEnabled(false);

        reviewsScrollListener = new MultiPageRecyclerViewScrollListener(
                presenter.getReviewList() != null, new MultiPageRecyclerViewScrollListener.LoadNextListener() {
            @Override
            public void onStartLoadNext() {
                presenter.getReviews(presenter.getMovie().getId(), presenter.getCurrentReviewPage() + 1);
            }
        });

        rvReviews.addOnScrollListener(reviewsScrollListener);
    }

    private void setupPosterImage() {
        int width = (int) (DisplayMetricUtils.getDeviceWidth(getActivity()) * 1.0f / 3.0f);
        int height = (int) ((1.5f / 1.0f) * width);

        ViewGroup.LayoutParams ivTrailerThumbnailLayoutParams = ivMoviePoster.getLayoutParams();
        ivTrailerThumbnailLayoutParams.width = width;
        ivTrailerThumbnailLayoutParams.height = height;
        ivMoviePoster.setLayoutParams(ivTrailerThumbnailLayoutParams);
    }

    private void setupBackdropImage() {
        int width;
        if (isTablet) {
            width = (int) (DisplayMetricUtils.getDeviceWidth(getActivity()) * 2.0f / 3.0f);
        } else {
            width = (int) (DisplayMetricUtils.getDeviceWidth(getActivity()));
        }
        int height = (int) (9.0f / 16.0f * width);
        ViewGroup.LayoutParams backdropLayoutParams = ivMovieBackdrop.getLayoutParams();
        backdropLayoutParams.width = width;
        backdropLayoutParams.height = height;
        ivMovieBackdrop.setLayoutParams(backdropLayoutParams);
    }

    @Override
    public void setHasTrailer(Trailer trailer, String backdropUrl) {
        if (movieDetailFragmentListener != null) {
            movieDetailFragmentListener.onTrailerLoaded(trailer, backdropUrl);
            ivTrailerPlayButton.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void updateFavoriteStatus(boolean isFavorite) {
        if (isTablet) {
            if (isFavorite) {
                fab.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
            fab.setVisibility(View.VISIBLE);
        } else {
            if (movieDetailFragmentListener != null) {
                movieDetailFragmentListener.onFavoriteChanged(isFavorite);
            }
        }
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar
                .make(llContainer, message, Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.red_700));

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(activity, R.color.red_100));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(activity, R.color.red_700));
        snackbar.show();
    }

    public interface MovieListFragmentListener {
        void onShowItem(String title);
    }

    public interface MovieDetailFragmentListener extends MovieListFragmentListener {
        void onTrailerLoaded(Trailer trailer, String backdropUrl);

        void onFavoriteChanged(boolean isFavorite);
    }
}
