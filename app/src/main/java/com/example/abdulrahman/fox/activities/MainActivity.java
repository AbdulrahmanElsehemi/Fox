package com.example.abdulrahman.fox.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.abdulrahman.fox.R;
import com.example.abdulrahman.fox.fragment.ManinFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState !=null)
        {
            return;
        }
        else
        {
            addFragment();
        }
    }


    private  void addFragment()
    {
        if (!isFinishing())
        {
            ManinFragment fragment=ManinFragment.create();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fl_main_grid, fragment)
                    .commit();
        }
    }
}
