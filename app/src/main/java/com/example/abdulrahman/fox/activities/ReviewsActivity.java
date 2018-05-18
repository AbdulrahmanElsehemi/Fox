package com.example.abdulrahman.fox.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.adapter.ReviewListAdapter;
import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.utils.Constants;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {

    @BindView(R.id.rv_reviews_activity)
    RecyclerView rvReviews;
    ReviewsActivityAdapter mReviewAdapter;
    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this);
        getSupportActionBar().hide();

        mMovie = Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.EXTRA_PARCELABLE_MOVIE));

        setReviewLayoutManager();
        setReviewRecyclerAdapter(rvReviews);


    }

    private void setReviewLayoutManager()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this,LinearLayoutManager.VERTICAL,false);
        rvReviews.setLayoutManager(linearLayoutManager);
        rvReviews.setHasFixedSize(true);


    }

    private void setReviewRecyclerAdapter(RecyclerView recyclerView){
        mReviewAdapter = new ReviewsActivityAdapter(this, mMovie);
        recyclerView.setAdapter(mReviewAdapter);
    }


    public void onClickImageButtonClose(View view) {
        finish();
    }

    private class ReviewsActivityAdapter extends ReviewListAdapter {

        public ReviewsActivityAdapter(Context mContext, Movie movie) {
            super(mContext, movie, null);
        }

        @Override
        public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context mContext = parent.getContext();
            int layourIdForReviewItem = R.layout.review_activity_item;
            LayoutInflater inflater = LayoutInflater.from(mContext);

            View view = inflater.inflate(layourIdForReviewItem, parent, false);
            ReviewViewHolder viewHolder = new ReviewViewHolder(view);

            return viewHolder;


        }
    }
}
