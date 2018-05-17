package com.example.abdulrahman.fox.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abdulrahman.fox.activities.MovieDetailsActivity;
import com.example.abdulrahman.fox.activities.MainActivity;
import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.fragment.MovieDetailsFragment;
import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.utils.Constants;



import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abdulrahman on 5/11/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> moviesList;
    private boolean isFavouriteMovie;
    private boolean mTwoPane;
    private boolean isLandscapeSmartphone;

   public MovieAdapter(Context mContext,List<Movie> moviesList,boolean mTwoPane,boolean isLandscapeSmartphone)
   {
       this.mContext=mContext;
       this.moviesList=moviesList;
       this.mTwoPane=mTwoPane;
       this.isLandscapeSmartphone=isLandscapeSmartphone;
   }

    @Override
    public MovieViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        Context mContext=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view =null;

        if (isLandscapeSmartphone&&mTwoPane)
        {
            view=inflater.inflate(R.layout.movie_item_small,parent,false);
        }
        else
        {
            view=inflater.inflate(R.layout.movie_item,parent,false);
        }

        MovieViewHolder viewHolder=new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( MovieViewHolder holder, int position) {

        String title, rating,poster;

        Movie mMovie =moviesList.get(position);
        if (mMovie.isFavourite())
        {
            isFavouriteMovie=true;
        }

        poster=mMovie.getPoster();
        rating=mMovie.getRating();
        title=mMovie.getTitle();

        Glide.with(mContext).load(Constants.API_POSTER_HEADER_LARGE+poster).into(holder.moviePoster);


        holder.mobieRating.setText(rating);
        holder.movieTile.setText(title);





    }

    @Override
    public int getItemCount() {

        if (moviesList !=null)
            return moviesList.size();
        else
            return 0;
    }

    class  MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster)
        ImageView moviePoster;
        @BindView(R.id.item_movie_title)
        TextView movieTile;
        @BindView(R.id.rating_movie_item)
        TextView mobieRating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickMovieIndex=getAdapterPosition();
            if (clickMovieIndex != RecyclerView.NO_POSITION)
            {
                Movie mMoviewClickded=moviesList.get(clickMovieIndex);

                int movieFromType;
                int type;

                if (isFavouriteMovie)
                {
                    type=Constants.MOVIE_FROM_CURSOR;
                }
                else
                {
                    type=Constants.MOVIE_FROM_LIST;
                }

                switch (type)
                {
                    case Constants.MOVIE_FROM_LIST:
                        movieFromType=Constants.MOVIE_FROM_LIST;
                        break;

                    case Constants.MOVIE_FROM_CURSOR:
                        movieFromType=Constants.MOVIE_FROM_CURSOR;
                        break;
                    default:
                        return;

                }

                if (mTwoPane)
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMoviewClickded));
                    bundle.putInt(Constants.EXTRA_MOVIE_FROM_TYPE,movieFromType);
                    addDetailFragmentForTwoPane(bundle);
                }
                else
                {
                    Intent movieDetailsIntent = new Intent(mContext, MovieDetailsActivity.class);
                    movieDetailsIntent.putExtra(Constants.EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMoviewClickded));
                    movieDetailsIntent.putExtra(Constants.EXTRA_MOVIE_FROM_TYPE, movieFromType);
                    mContext.startActivity(movieDetailsIntent);
                }

            }
        }


        public void addDetailFragmentForTwoPane(Bundle bundle)
        {
            MovieDetailsFragment detailFragment = MovieDetailsFragment.create(bundle, mTwoPane);
            FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_details, detailFragment)
                    .commit();
        }
    }
}
