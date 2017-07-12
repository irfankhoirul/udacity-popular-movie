package com.irfankhoirul.popularmovie.modules.movie_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.modules.movie_detail.DetailMovieActivity;
import com.irfankhoirul.popularmovie.modules.movie_detail.DetailMovieFragment;
import com.irfankhoirul.popularmovie.util.DisplayMetricUtils;
import com.irfankhoirul.popularmovie.util.MultiPageRecyclerViewScrollListener;
import com.irfankhoirul.popularmovie.util.RecyclerViewMarginDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.irfankhoirul.popularmovie.util.ConstantUtil.SHOW_DETAIL_CHANGED_RESULT_CODE;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.SHOW_DETAIL_REQ_CODE;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.SORT_FAVORITE;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.SORT_POPULAR;
import static com.irfankhoirul.popularmovie.util.ConstantUtil.SORT_TOP_RATED;

public class ListMovieActivity extends AppCompatActivity
        implements ListMovieContract.View, MovieAdapter.MovieClickListener,
        DetailMovieFragment.MovieListFragmentListener {

    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;
    @BindView(R.id.avi_load_more)
    AVLoadingIndicatorView aviLoadMore;

    private ListMovieContract.Presenter presenter;
    private MovieAdapter movieAdapter;
    private AlertDialog loadingDialog;
    private AlertDialog sortDialog;
    private MultiPageRecyclerViewScrollListener moviesScrollListener;
    private boolean isTablet;
    private String currentState;

    @Override
    protected void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (sortDialog != null) {
            sortDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        ButterKnife.bind(this);

        presenter = new ListMoviePresenter(this, this);

        setupMovieRecyclerView();

        setupMovieData(savedInstanceState);
    }

    private void setupMovieData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("toolbar_title") != null &&
                    getSupportActionBar() != null) {
                getSupportActionBar().setTitle(savedInstanceState.getString("toolbar_title"));
            }

            if (savedInstanceState.getParcelableArrayList("movies") != null) {
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
                presenter.setMovieList(movies);
            }

            if (savedInstanceState.getInt("page") != 0) {
                presenter.setCurrentPage(savedInstanceState.getInt("page"));
            }

            String viewState = savedInstanceState.getString("viewState");
            if (viewState != null) {
                currentState = viewState;
                switch (viewState) {
                    case ListMovieViewState.STATE_SHOW_SORT_DIALOG:
                        showSortDialog();
                        break;
                    case ListMovieViewState.STATE_LOADING_MOVIE:
                        presenter.getMovies(presenter.getSortPreference(),
                                ListMoviePresenter.INITIAL_PAGE);
                        break;
                    case ListMovieViewState.STATE_LOADING_FAVORITE:
                        presenter.getFavoriteMovies();
                        break;
                }
            }
        } else {
            getMoviesByPreference();
        }
    }

    private void setupMovieRecyclerView() {
        int marginInPixel = DisplayMetricUtils.convertDpToPixel(8);
        int deviceWidthInDp = DisplayMetricUtils.convertPixelsToDp(
                DisplayMetricUtils.getDeviceWidth(this));
        isTablet = findViewById(R.id.item_detail_container) != null;
        int column;
        if (isTablet) {
            column = (int) (1.0f / 3.0f * deviceWidthInDp) / 200;
        } else {
            column = deviceWidthInDp / 200;
        }
        RecyclerViewMarginDecoration decoration =
                new RecyclerViewMarginDecoration(RecyclerViewMarginDecoration.ORIENTATION_VERTICAL,
                        marginInPixel, column);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, column);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.addItemDecoration(decoration);
        movieAdapter = new MovieAdapter(presenter.getMovieList(), this);
        rvMovies.setAdapter(movieAdapter);

        moviesScrollListener = new MultiPageRecyclerViewScrollListener(
                presenter.getMovieList() != null, new MultiPageRecyclerViewScrollListener.LoadNextListener() {
            @Override
            public void onStartLoadNext() {
                if (presenter.getSortPreference().equals(SORT_POPULAR) ||
                        presenter.getSortPreference().equals(SORT_TOP_RATED)) {
                    presenter.getMovies(presenter.getSortPreference(), presenter.getCurrentPage() + 1);
                }
            }
        });

        rvMovies.addOnScrollListener(moviesScrollListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", presenter.getMovieList());
        if (getSupportActionBar() != null && getSupportActionBar().getTitle() != null) {
            outState.putString("toolbar_title", getSupportActionBar().getTitle().toString());
        }
        outState.putInt("page", presenter.getCurrentPage());
        outState.putString("viewState", currentState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                showSortDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCurrentState(String state) {
        currentState = state;
    }

    @Override
    public void updateMovieList() {
        movieAdapter.notifyDataSetChanged();
        moviesScrollListener.isLoading(false);
    }

    private void showSortDialog() {
        setCurrentState(ListMovieViewState.STATE_SHOW_SORT_DIALOG);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sort, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.dialog_title_sort);

        final RadioButton rbSortByPopularity = (RadioButton) dialogView.findViewById(R.id.rb_sort_by_popularity);
        final RadioButton rbSortByRating = (RadioButton) dialogView.findViewById(R.id.rb_sort_by_rating);
        final RadioButton rbShowFavorite = (RadioButton) dialogView.findViewById(R.id.rb_show_favorite);
        if (presenter.getSortPreference().equals(SORT_POPULAR)) {
            rbSortByPopularity.setChecked(true);
        } else if (presenter.getSortPreference().equals(SORT_TOP_RATED)) {
            rbSortByRating.setChecked(true);
        } else if (presenter.getSortPreference().equals(SORT_FAVORITE)) {
            rbShowFavorite.setChecked(true);
        }
        dialogBuilder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setCurrentState(ListMovieViewState.STATE_IDLE);
                if (rbSortByPopularity.isChecked() &&
                        (!presenter.getSortPreference().equals(SORT_POPULAR) ||
                                presenter.getMovieList().isEmpty())) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_popular_movies);
                    }
                    presenter.getMovies(SORT_POPULAR,
                            ListMoviePresenter.INITIAL_PAGE);
                } else if (rbSortByRating.isChecked() &&
                        (!presenter.getSortPreference().equals(SORT_TOP_RATED) ||
                                presenter.getMovieList().isEmpty())) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_top_rated_movies);
                    }
                    presenter.getMovies(SORT_TOP_RATED,
                            ListMoviePresenter.INITIAL_PAGE);
                } else if (rbShowFavorite.isChecked() &&
                        (!presenter.getSortPreference().equals(SORT_FAVORITE) ||
                                presenter.getMovieList().isEmpty())) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_favorite_movies);
                    }
                    presenter.getFavoriteMovies();
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setCurrentState(ListMovieViewState.STATE_IDLE);
                dialog.dismiss();
            }
        });
        sortDialog = dialogBuilder.create();
        sortDialog.setCancelable(false);
        sortDialog.show();
    }

    @Override
    public void setLoading(boolean isLoading, String message) {
        if (isLoading) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_loading, null);
            TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            dialogBuilder.setView(dialogView);
            loadingDialog = dialogBuilder.create();
            loadingDialog.setCancelable(false);
            loadingDialog.show();
        } else if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void setLoadMore(boolean status) {
        if (status) {
            aviLoadMore.setVisibility(View.VISIBLE);
        } else {
            aviLoadMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar
                .make(llContainer, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getMoviesByPreference();
                    }
                });

        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red_700));

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red_100));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.red_700));
        snackbar.show();
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        if (isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailMovieFragment fragment = DetailMovieFragment.newInstance(movie, isTablet);
            fragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailMovieActivity.class);
            intent.putExtra("movie", movie);
            startActivityForResult(intent, SHOW_DETAIL_REQ_CODE);
        }
    }

    private void getMoviesByPreference() {
        if (presenter.getSortPreference() != null) {
            if (presenter.getSortPreference().equals(SORT_POPULAR)) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.title_popular_movies));
                }
                presenter.getMovies(SORT_POPULAR, presenter.getCurrentPage());
            } else if (presenter.getSortPreference().equals(SORT_TOP_RATED)) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.title_top_rated_movies));
                }
                presenter.getMovies(SORT_TOP_RATED, presenter.getCurrentPage());
            } else {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.title_favorite_movies));
                }
                presenter.getFavoriteMovies();
            }
        } else {
            presenter.getMovies(SORT_POPULAR, presenter.getCurrentPage());
        }
    }

    @Override
    public void onShowItem(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onRemoveItem(Movie movie) {
        if (presenter.getSortPreference().equals(SORT_FAVORITE)) {
            presenter.removeMovie(movie);
        }
    }

    @Override
    public void onAddItem(Movie movie) {
        if (presenter.getSortPreference().equals(SORT_FAVORITE)) {
            presenter.addMovie(movie);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOW_DETAIL_REQ_CODE && resultCode == SHOW_DETAIL_CHANGED_RESULT_CODE) {
            if (presenter.getSortPreference().equals(SORT_FAVORITE)) {
                presenter.removeMovie((Movie) data.getParcelableExtra("movie"));
            }
        }
    }
}
