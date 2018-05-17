package com.example.abdulrahman.fox.fragment;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.adapter.MovieAdapter;
import com.example.abdulrahman.fox.api.MoviesApi;
import com.example.abdulrahman.fox.api.MoviesService;
import com.example.abdulrahman.fox.model.Movie;
import com.example.abdulrahman.fox.model.MoviesResults;
import com.example.abdulrahman.fox.utils.Constants;
import com.example.abdulrahman.fox.utils.Utils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManinFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    @BindView(R.id.movies_list)
    RecyclerView recyclerMovies;
    private  ArrayList<Movie>mMoviesist;
    private MovieAdapter adapter;
    private Context mContext;
    private MenuItem menuItem;

    private String sortBy = Constants.SORT_BY_DEFAULT;

    private int itemMenuSelected = -1;

    private boolean mTwoPane;
    private boolean isSmartphone;

    private MoviesApi moviesApi;

    private static final int MOVIE_LOADER_ID = 1;
    private static boolean isFavSorting;


    public static ManinFragment create(){
        ManinFragment fragment = new ManinFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public ManinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manin, container, false);

        isSmartphone=getResources().getBoolean(R.bool.isSmartphone);
        if (getActivity().findViewById(R.id.fl_details )!=null)
        {
            mTwoPane=true;
        }

        ButterKnife.bind(this,view);
        setLayoutmanger();
        setHasOptionsMenu(true);

        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState !=null)
        {
            itemMenuSelected=savedInstanceState.getInt(Constants.STATE_MENU_SELECTED);
        }
        moviesApi= MoviesService.createService(MoviesApi.class);
        mContext=getContext();

        if (Utils.isOnline(mContext))
        {
            if (Utils.isValidApiKey())
            {
                httpGetMovies(sortBy);
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.STATE_MENU_SELECTED,itemMenuSelected);
    }


    private void setLayoutmanger()
    {
        int nbCell;

        if(mTwoPane)
            nbCell=2;
        else
            nbCell= Utils.calculateNoOfColumns(mContext);

        if (isSmartphone && mTwoPane)
        {
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
            recyclerMovies.setLayoutManager(linearLayoutManager);
            recyclerMovies.setHasFixedSize(true);
        }
        else
        {
            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,nbCell);
            recyclerMovies.setLayoutManager(gridLayoutManager);
            recyclerMovies.setHasFixedSize(true);
        }
    }

    private void setRecyclerAdapter(RecyclerView recyclerView, List<Movie> movieList)
    {
        adapter=new MovieAdapter(mContext,movieList,mTwoPane,isSmartphone);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);

        if (itemMenuSelected ==-1)
        {
            return;
        }

        switch (itemMenuSelected)
        {
            case R.id.item_sort_by_popularity:
                menuItem = menu.findItem(R.id.item_sort_by_popularity);
                menuItem.setChecked(true);
                break;
            case R.id.item_sort_by_top_rated:
                menuItem = menu.findItem(R.id.item_sort_by_top_rated);
                menuItem.setChecked(true);
                break;

            case R.id.item_sort_by_favourite:
                menuItem = menu.findItem(R.id.item_sort_by_favourite);
                menuItem.setChecked(true);
                break;
        }
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id)
        {
            case R.id.item_sort_by_popularity:
                sortBy=Constants.SORT_BY_POPOLARITY;
                item.setChecked(true);
                httpGetMovies(sortBy);
                isFavSorting=false;
                itemMenuSelected=id;
                setToolBar("Popular Movies");

                return true;

            case R.id.item_sort_by_top_rated:
                sortBy=Constants.SORT_BY_TOP_RATED;
                item.setChecked(true);
                httpGetMovies(sortBy);
                isFavSorting=false;
                itemMenuSelected=id;
                setToolBar("Top rated Movies");

                return true;

            case R.id.item_sort_by_favourite:
                sortBy=Constants.SORT_BY_FAVOURITE;
                item.setChecked(true);
                httpGetMovies(sortBy);
                isFavSorting=true;
                itemMenuSelected=id;
                setToolBar("favourite Movies");


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void setToolBar(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void addDetailFragmentForTwoPane(Bundle bundle) {
        MovieDetailsFragment detailFragment = MovieDetailsFragment.create(bundle, mTwoPane);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_details, detailFragment)
                .commit();
    }

    private void httpGetMovies(String sortBy)
    {
        Call<MoviesResults> call=moviesApi.getPopluarMovies(sortBy);
        call.enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {

                if (response.isSuccessful())
                {
                    mMoviesist=new ArrayList<>(response.body().getmMoviesResults().size());
                    mMoviesist=(ArrayList<Movie>)response.body().getmMoviesResults();
                    if (mMoviesist.size() !=0)
                    {
                        if (mTwoPane)
                        {
                            Bundle bundle=new Bundle();
                            bundle.putParcelable(Constants.EXTRA_PARCELABLE_MOVIE, Parcels.wrap(mMoviesist.get(0)));
                            addDetailFragmentForTwoPane(bundle);

                        }
                        setRecyclerAdapter(recyclerMovies,mMoviesist);
                    }
                    else
                    {

                    }
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                Utils.showLongToastMessage(mContext, "Error fetching movies :" + t.getMessage());

            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
