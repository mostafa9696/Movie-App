package com.example.mostafa.movieapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetails_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_activity);
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
        if (savedInstanceState == null) {
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            movieDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fDetails, movieDetailsFragment, "").commit();
        }
    }

}
