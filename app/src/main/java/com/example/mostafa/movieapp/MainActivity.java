package com.example.mostafa.movieapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MovieListener{
    Boolean TabMode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment mainFragment = new MainFragment();

        if(savedInstanceState==null) {
            mainFragment.SetMovieListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.f1Main, mainFragment, "MyF").commit();
        }
        else
        {
            mainFragment= (MainFragment) getSupportFragmentManager().findFragmentByTag("MyF");
            mainFragment.SetMovieListener(this);
        }
        if (findViewById(R.id.fDetails) != null)
            TabMode=true;
    }
    @Override
    public void setSelectedMovie(Movie movie) {
        if(!TabMode)  // Mobile case
        {
            Intent intent=new Intent(this,MovieDetails_activity.class);
            intent.putExtra("Movie",movie);
            startActivity(intent);
        }
        else  // tablet case
        {
            MovieDetailsFragment detailsFragment=new MovieDetailsFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("Movie",movie);
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fDetails, detailsFragment).commit();
        }
    }
}
