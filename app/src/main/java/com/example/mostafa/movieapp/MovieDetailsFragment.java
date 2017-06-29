package com.example.mostafa.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MovieDetailsFragment extends Fragment {
    Boolean HasReviews=false,HasTrailers=false;
    ImageView Poster,Fav;
    MovieAsyncTask movieAsyncTask;
    Button Reviews,Trailers;
    RecyclerView RecyclerList;
    TextView Vote , Title , Description , Date;
    Movie movie;
    String MovieName,TaskAnswer;
    RecyclerView.LayoutManager layoutManager;
    ReviewAdapter reviewAdapter=null;
    TrailerAdapter trailerAdapter=null;
    Cursor Is_fav=null;
    int MovieID;
    ArrayList <ReviewDetails>ReviewList;
    ArrayList <TrailerDetails>TrailersList;
    ScrollView scrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_details,container,false);
        Bundle bundle=getArguments();
        RecyclerList=(RecyclerView)view.findViewById(R.id.Rec);
        layoutManager=new LinearLayoutManager(getContext());
        RecyclerList.setLayoutManager(layoutManager);
        Reviews=(Button)view.findViewById(R.id.reviewButton);
        Trailers=(Button)view.findViewById(R.id.trailerButton);
        scrollView=(ScrollView)view.findViewById(R.id.parentScrollView);
        if (bundle != null) {
            movie = bundle.getParcelable("Movie");
            Poster = (ImageView) view.findViewById(R.id.poster_img);
            Vote = (TextView) view.findViewById(R.id.Vote);
            Title = (TextView) view.findViewById(R.id.title);
            Description = (TextView) view.findViewById(R.id.description);
            Date = (TextView) view.findViewById(R.id.Releasedate);
            Fav=(ImageView)view.findViewById(R.id.Favorite);
            MovieID=movie.getID();
            ///////////////////////////////////////////
            MovieName=movie.getTitle();
            Picasso.with(getContext()).load(movie.getMoviePoster()).into(Poster);
            Vote.setText("Vote : " + Double.toString(movie.getVote()));
            Title.setText(movie.getTitle());
            Description.setText(movie.getOverview());
            Date.setText("Release date : " + movie.getDate());
            Uri uri=MovieContract.MovieEntry.CONTENT_URI;
            uri=uri.buildUpon().appendPath(movie.getTitle()).build();
            Is_fav=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,"Title=?",new String[]{MovieName},null);
            if (Is_fav.getCount() > 0) {
                Fav.setImageResource(R.drawable.fav);
            } else {
                Fav.setImageResource(R.drawable.unfav);
            }
        }
        if(reviewAdapter!=null)
        {
            movieAsyncTask=new MovieAsyncTask("reviews",MovieID);
            ReviewList=new ArrayList();
            try {
                ReviewList=movieAsyncTask.GetReviews(TaskAnswer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            reviewAdapter=new ReviewAdapter(ReviewList,getContext());
            RecyclerList.setAdapter(reviewAdapter);
        }
        Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Is_fav=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        null,"Title=?",new String[]{MovieName},null);
                if (Is_fav.getCount() > 0) {
                    Fav.setImageResource(R.drawable.unfav);
                    Uri uri=MovieContract.MovieEntry.CONTENT_URI;
                    uri=uri.buildUpon().appendPath(MovieName).build();
                    int del=getContext().getContentResolver().delete(uri,null,null);
                    Toast.makeText(getContext(),"Removed from favorite movies" ,Toast.LENGTH_LONG).show();
                } else {
                    Fav.setImageResource(R.drawable.fav);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.COLUMN_NAME,MovieName);
                    Uri uri=getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,contentValues);
                    if(uri!=null)
                    Toast.makeText(getContext(),"Inserted to favorite movies" ,Toast.LENGTH_LONG).show();
                }
            }
        });

        Reviews.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SetReviews();
                FocusOnView();
            }
        });
        Trailers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SetTrailers();
                FocusOnView();
            }
        });
        return view;
    }
    private final void FocusOnView(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, RecyclerList.getTop());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("rev",HasReviews);
        outState.putBoolean("tr",HasTrailers);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
        {
            Boolean GetRev = savedInstanceState.getBoolean("rev",false);
            Boolean GetTra = savedInstanceState.getBoolean("tr",false);
            if(GetRev.equals(true))
            {
                SetReviews();  // To store views if rotate the screen
            }
            else if(GetTra.equals(true))
            {
                SetTrailers();  // To store views if rotate the screen
            }
        }
    }
    public void SetReviews()
    {
        HasReviews=true;
        HasTrailers=false;
        movieAsyncTask=new MovieAsyncTask("reviews",MovieID);
        try {
            TaskAnswer=movieAsyncTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ReviewList=new ArrayList();
        try {
            ReviewList=movieAsyncTask.GetReviews(TaskAnswer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(ReviewList.size()>0) {
            reviewAdapter = new ReviewAdapter(ReviewList, getContext());
            RecyclerList.setAdapter(reviewAdapter);
        }
        else
            Toast.makeText(getContext(),"No Reviews for this movie",Toast.LENGTH_LONG).show();
    }
    public void SetTrailers()
    {
        HasReviews=false;
        HasTrailers=true;
        movieAsyncTask=new MovieAsyncTask("videos",MovieID);
        try {
            TaskAnswer=movieAsyncTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TrailersList=new ArrayList();
        try {
            TrailersList=movieAsyncTask.GetTrailers(TaskAnswer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trailerAdapter=new TrailerAdapter(TrailersList,getContext(), new TrailerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TrailerDetails item) {
                String Link = "https://www.youtube.com/watch?v=";
                String MovieKey = item.getKey();
                String VideoUri = Link + MovieKey;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(VideoUri));
                startActivity(i);
            }
        });
        RecyclerList.setAdapter(trailerAdapter);
    }

}
