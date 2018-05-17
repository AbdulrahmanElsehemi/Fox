package com.example.abdulrahman.fox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.model.Trailer;
import com.example.abdulrahman.fox.model.TrailersResults;
import com.example.abdulrahman.fox.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abdulrahman on 5/18/2018.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    private Context mContext;
    private Movie mMovie;
    final private ITrailerListListener mOnClickListener;



    public TrailerListAdapter(Context mContext, Movie mMovie, ITrailerListListener listener) {
        this.mContext = mContext;
        this.mMovie = mMovie;
        this.mOnClickListener = listener;
    }

    public interface ITrailerListListener {
        void onTrailerListClick(int clickTrailerIndex);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        Context mContext=parent.getContext();
        int layourIdForTrailerItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layourIdForTrailerItem, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( TrailerViewHolder holder, int position) {
        TrailersResults trailersResults=mMovie.getTrailersResults();
        Trailer mTrailer=trailersResults.getTrailers().get(position);

        if (mMovie.getImages() !=null)
        {
            String mBackdrop=mMovie.getImages().getBackdropsList().get(position).getPath();

            Glide.with(mContext).load(Constants.API_BACKDROP_HEADER+mBackdrop).into(holder.ivTrailerBackdrop);

        }
        holder.tvTrailerTitle.setText(mTrailer.getName());
        holder.itemView.setTag(mTrailer.getKey());
    }

    @Override
    public int getItemCount() {
        if (mMovie.getTrailersResults()==null)
        {
            return 0;
        }
        return
                mMovie.getTrailersResults().getTrailers().size();
    }

    class  TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.iv_trailer_backdrop)
        ImageView ivTrailerBackdrop;
        @BindView(R.id.tv_trailer_title)
        TextView tvTrailerTitle;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onTrailerListClick(clickPosition);
        }
    }
}
