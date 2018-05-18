package com.example.abdulrahman.fox.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.activities.ReviewsActivity;
import com.example.abdulrahman.fox.adapter.ReviewListAdapter;
import com.example.abdulrahman.fox.adapter.TrailerListAdapter;
import com.example.abdulrahman.fox.api.MoviesApi;
import com.example.abdulrahman.fox.api.MoviesService;
import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.model.ReviewsResults;
import com.example.abdulrahman.fox.model.Trailer;
import com.example.abdulrahman.fox.model.TrailersResults;
import com.example.abdulrahman.fox.utils.Constants;
import com.example.abdulrahman.fox.utils.Utils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment implements
        TrailerListAdapter.ITrailerListListener ,ReviewListAdapter.IReviewListListener{

    @BindView(R.id.iv_backdrop)
    ImageView ivBorddrop;
    @BindView(R.id.iv_poster_detail)
    ImageView ivPoster;
    @BindView(R.id.tv_title_detail)
    TextView tvTitle;
    @BindView(R.id.tv_synopsis)
    TextView tvSynopsis;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_runtime)
    TextView tvRunTime;
    @BindView(R.id.rv_trailer_list)
    RecyclerView rvTrailerList;
    @BindView(R.id.rv_reviews_list)
    RecyclerView rvReviewList;
    @BindView(R.id.iv_hiden_heart)
    ImageView ivHidenHeart;
    @BindView(R.id.tv_reviews_count)
    TextView tvReviewCount;
    @BindView(R.id.tv_reviews_fix)
    TextView tvReviewFix;
    @BindView(R.id.fab_favourite)
    FloatingActionButton fabFavourite;



    private TrailerListAdapter mTrailerAdapter;
    private ReviewListAdapter mReviewAdapter;

    private StringBuilder mParamsForApi;

    private Movie mMovie;
    private Context mContext;
    private MoviesApi moviesApi;
    private int movieFromType;


    private static boolean mTwoPane;

    public static MovieDetailsFragment create(Bundle args, boolean isTwoPane){
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        mTwoPane = isTwoPane;
        return fragment;
    }
    public MovieDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

        setViewMovie();
        setTrailerLayoutManager();
        setReviewLayoutManger();

        if (!mTwoPane)
        {
            setToolBar(mMovie.getTitle(),true,true);
        }


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() !=null)
        {
            Bundle arg=getArguments();
            mMovie= Parcels.unwrap(arg.getParcelable(Constants.EXTRA_PARCELABLE_MOVIE));
            movieFromType = arg.getInt(Constants.EXTRA_MOVIE_FROM_TYPE);
        }
        mContext=getActivity();
        moviesApi= MoviesService.createService(MoviesApi.class);

        if (savedInstanceState !=null)
        {

            mMovie = Parcels.unwrap(savedInstanceState.getParcelable(Constants.STATE_MOVIE_DETAILS));



        }
        else
        {

            if (movieFromType==Constants.MOVIE_FROM_LIST ||
                    (movieFromType == Constants.MOVIE_FROM_CURSOR && Utils.isOnline(getActivity())))
            {
                httpGetMovieDetails(mMovie.getId());

            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.STATE_MOVIE_DETAILS, Parcels.wrap(mMovie));

    }

    private void setViewMovie()
    {
        tvTitle.setText(mMovie.getTitle() + " (" + Utils.getYear(mMovie.getReleaseDate())+")");
        tvSynopsis.setText(mMovie.getSynopsis());
        tvRating.setText(mMovie.getRating());

        Glide.with(mContext).load(Constants.API_POSTER_HEADER_LARGE+mMovie.getPoster()).into(ivPoster);
        Glide.with(mContext).load(Constants.API_BACKDROP_HEADER+mMovie.getBackdrop()).into(ivBorddrop);


        if (mMovie.getTrailersResults() !=null &&mMovie.getReviewsResults() !=null &&mMovie.getRuntime() !=null )
        {
            setTrailerRecyclerAdapter(rvTrailerList);
            tvReviewCount.setText(String.valueOf("("+mMovie.getReviewsResults().getTotalReviews())+")");
            setReviewRecyclerAdapter(rvReviewList);
            tvRunTime.setText(Utils.timeToDisplay(mMovie.getRuntime()));

        }

    }

    private void setTrailerLayoutManager()
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        rvTrailerList.setLayoutManager(linearLayoutManager);
        rvTrailerList.setHasFixedSize(true);
    }

    private void setTrailerRecyclerAdapter(RecyclerView recyclerView)
    {

        mTrailerAdapter =new TrailerListAdapter(mContext,mMovie,this);
        recyclerView.setAdapter(mTrailerAdapter);
    }

    private void setReviewLayoutManger()
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        rvReviewList.setLayoutManager(linearLayoutManager);
        rvReviewList.setHasFixedSize(true);
    }

    private void setReviewRecyclerAdapter(RecyclerView recyclerView)
    {
        mReviewAdapter=new ReviewListAdapter(mContext,mMovie,this);
        recyclerView.setAdapter(mReviewAdapter);
    }

    private  void setToolBar(String title, boolean homeUp, boolean showHomeUp)
    {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(homeUp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(showHomeUp);
    }
    private void httpGetMovieDetails(long movieId) {

        mParamsForApi = new StringBuilder();
        mParamsForApi.append(getString(R.string.api_append_videos));
        mParamsForApi.append(",");
        mParamsForApi.append(getString(R.string.api_append_reviews));
        mParamsForApi.append(",");
        mParamsForApi.append(getString(R.string.api_append_images));

        retrofit2.Call<Movie> call=moviesApi.getMovieDetails(movieId,mParamsForApi.toString());

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(retrofit2.Call<Movie> call, Response<Movie> response) {
                mMovie=response.body();


                // set trailers
                TrailersResults trailersResults= mMovie.getTrailersResults();
                mMovie.setTrailersResults(trailersResults);
                setTrailerRecyclerAdapter(rvTrailerList);

                //set reviews
                ReviewsResults reviewsResults=mMovie.getReviewsResults();
                mMovie.setReviewsResults(reviewsResults);
                tvReviewCount.setText(String.valueOf("("+mMovie.getReviewsResults().getTotalReviews())+")");
                setReviewRecyclerAdapter(rvReviewList);

                //set runTime
                tvRunTime.setText(Utils.timeToDisplay(mMovie.getRuntime()));


            }

            @Override
            public void onFailure(retrofit2.Call<Movie> call, Throwable t) {

            }
        });

    }

    @Override
    public void onTrailerListClick(int clickTrailerIndex) {

        Trailer mTrailer =mMovie.getTrailersResults().getTrailers().get(clickTrailerIndex);
        Utils.showLongToastMessage(getActivity(),Constants.TOAST_WATCHING_TRAILER_+mTrailer.getName());
        Intent playYoutubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL+mTrailer.getKey()));
        startActivity(playYoutubeIntent);
    }

    @Override
    public void onReviewListClick(int clickReviewIndex) {
        Intent reviewsIntent = new Intent(getActivity(), ReviewsActivity.class);
        reviewsIntent.putExtra(Constants.EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMovie));
        startActivity(reviewsIntent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out );
    }
}
