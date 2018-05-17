package com.example.abdulrahman.fox.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.fragment.MovieDetailsFragment;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState !=null)
        {
            return;
        }
        else
        {
            addDetailsFragement();
        }
    }


    private void addDetailsFragement()
    {
        if (!isFinishing())
        {
            MovieDetailsFragment detailsFragment=MovieDetailsFragment.create(getIntent().getExtras(),false);
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fl_details,detailsFragment)
                    .commit();
        }
    }
}
