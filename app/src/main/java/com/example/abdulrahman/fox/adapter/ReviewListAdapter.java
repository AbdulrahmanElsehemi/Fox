package com.example.abdulrahman.fox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.model.Review;
import com.example.abdulrahman.fox.model.ReviewsResults;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abdulrahman on 5/18/2018.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {
    private Context mContext;
    private Movie mMovie;
    final private IReviewListListener mOnClickListener;

    public interface IReviewListListener {
        void onReviewListClick(int clickReviewIndex);
    }

    public ReviewListAdapter(Context mContext, Movie mMovie, IReviewListListener listener) {
        this.mContext = mContext;
        this.mMovie = mMovie;
        this.mOnClickListener = listener;
    }




    @Override
    public ReviewViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context mContext=parent.getContext();
        int LaoutForReviewItem=R.layout.review_item;
        LayoutInflater inflater=LayoutInflater.from(mContext);

        View view=inflater.inflate(LaoutForReviewItem,parent,false);
        ReviewViewHolder viewHolder= new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ReviewViewHolder holder, int position) {

        ReviewsResults reviewsResults=mMovie.getReviewsResults();
        Review mReview=reviewsResults.getReviews().get(position);

        holder.tvReviewAuthor.setText(mReview.getAuthor()+" :");
        holder.tvReviewContent.setText(mReview.getContent());


    }

    @Override
    public int getItemCount() {
        if (mMovie.getReviewsResults()==null)
        {
            return 0;
        }
        return  mMovie.getReviewsResults().getReviews().size();
    }

    protected class  ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_review_author)
        TextView tvReviewAuthor;
        @BindView(R.id.tv_review_content)
        TextView tvReviewContent;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           int clickPosition=getAdapterPosition();
           mOnClickListener.onReviewListClick(clickPosition);
        }
    }
}
