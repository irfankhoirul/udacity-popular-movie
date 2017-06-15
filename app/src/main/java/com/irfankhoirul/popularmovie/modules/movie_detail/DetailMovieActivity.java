package com.irfankhoirul.popularmovie.modules.movie_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.irfankhoirul.popularmovie.R;
import com.irfankhoirul.popularmovie.data.pojo.Movie;
import com.irfankhoirul.popularmovie.util.DateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.irfankhoirul.popularmovie.R.id.tv_release_date;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private static final String POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/w185";

    @BindView(R.id.iv_movie_poster)
    ImageView ivMoviePoster;
    @BindView(R.id.tv_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.tv_average_rating)
    TextView tvAverageRating;
    @BindView(R.id.tv_popularity)
    TextView tvPopularity;
    @BindView(tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_synopsis)
    TextView tvSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.toolbar_title_movie_detail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent() != null && getIntent().hasExtra("movie")) {
            Movie movie = getIntent().getParcelableExtra("movie");

            Picasso.with(this)
                    .load(POSTER_PATH_BASE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivMoviePoster, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, getResources().getString(R.string.message_error));
                        }
                    });

            tvMovieTitle.setText(movie.getTitle());

            String averageRating = getString(R.string.label_average_rating) + movie.getVoteAverage();
            tvAverageRating.setText(averageRating);

            String popularity = getString(R.string.label_popularity) + movie.getPopularity();
            tvPopularity.setText(popularity);

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

}
