package com.irfankhoirul.popularmovie.modules.movie_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.irfankhoirul.popularmovie.util.DisplayMetricUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMovieActivity extends AppCompatActivity
        implements ListMovieContract.View, MovieAdapter.MovieClickListener {

    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;

    private ListMovieContract.Presenter presenter;
    private MovieAdapter movieAdapter;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        ButterKnife.bind(this);

        presenter = new ListMoviePresenter(this, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        int marginInPixel = DisplayMetricUtils.convertDpToPixel(8);
        int column = 2;
        RecyclerViewMarginDecoration decoration =
                new RecyclerViewMarginDecoration(marginInPixel, column);
        rvMovies.addItemDecoration(decoration);
        movieAdapter = new MovieAdapter(presenter.getMovieList(), this);
        rvMovies.setAdapter(movieAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.getString("toolbar_title") != null &&
                    getSupportActionBar() != null) {
                getSupportActionBar().setTitle(savedInstanceState.getString("toolbar_title"));
            }

            if (savedInstanceState.getParcelableArrayList("movies") != null) {
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
                presenter.setMovieList(movies);
            }
        } else {
            presenter.getPopularMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", presenter.getMovieList());
        if (getSupportActionBar() != null && getSupportActionBar().getTitle() != null) {
            outState.putString("toolbar_title", getSupportActionBar().getTitle().toString());
        }
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
    public void updateMovieList() {
        movieAdapter.notifyDataSetChanged();
    }

    private void showSortDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sort, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.dialog_title_sort);

        final RadioButton rbSortByPopularity = (RadioButton) dialogView.findViewById(R.id.rb_sort_by_popularity);
        final RadioButton rbSortByRating = (RadioButton) dialogView.findViewById(R.id.rb_sort_by_rating);
        dialogBuilder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (rbSortByPopularity.isChecked()) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_popular_movies);
                    }
                    presenter.getPopularMovies();
                } else if (rbSortByRating.isChecked()) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.title_top_rated_movies);
                    }
                    presenter.getTopRatedMovies();
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
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
    public void showError(String message) {
        Snackbar snackbar = Snackbar
                .make(llContainer, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.getPopularMovies();
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
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}
