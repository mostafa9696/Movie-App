package com.example.mostafa.movieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainFragment extends Fragment {
    MovieAdapter adapter=null;
    RecyclerView MoviesList;
    String TaskResult="";
    List<Movie> PopularMovies ,TopRatedMovies, FavoriteMovie ,MoviesItems ;
    RecyclerView.LayoutManager layoutManager;
    String List_type="empty";
    MovieListener movieListener;
    ////////////////////////////
    void SetMovieListener(MovieListener ML)
    {
        movieListener=ML;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        MoviesItems=new ArrayList<>();
        PopularMovies=new ArrayList<>();
        TopRatedMovies=new ArrayList<>();
        FavoriteMovie=new ArrayList<>();
        MoviesList=(RecyclerView)view.findViewById(R.id.recycler_view);
        layoutManager=new GridLayoutManager(getActivity(),2);
        MoviesList.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("List",List_type);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);                            // To save fragment state when screen rotate
        if(savedInstanceState==null) {
            try {
                List_type = "top_rated";
                GetData(List_type);// get popular movies
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                List_type = "popular";
                GetData(List_type);// get popular movies
                SetAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                List_type = "top_rated";
                GetData(List_type);// get popular movies
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                List_type = "popular";
                GetData(List_type);// get popular movies
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List_type=savedInstanceState.getString("List");
            SetAdapter();
        }
    }

    public void GetData(String ListType) throws JSONException {
        MovieAsyncTask movieAsyncTask=new MovieAsyncTask(ListType,0);
        try {
            TaskResult=movieAsyncTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(TaskResult.equals("No data"))
            Toast.makeText(getContext(),"Failed to fetch data !",Toast.LENGTH_LONG).show();
        else {
            if (ListType.equals("popular")) {
                PopularMovies = movieAsyncTask.GetMovies(TaskResult);
            } else if (ListType.equals("top_rated")) {
                TopRatedMovies = movieAsyncTask.GetMovies(TaskResult);
            }
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        if(List_type.equals("favorite"))
        {
            SetFavorite();
            SetAdapter();
        }
    }

    public void SetAdapter()
    {
        if(List_type.equals("popular"))
        {
            MoviesItems=PopularMovies;
        }
        else if(List_type.equals("top_rated"))
        {
            MoviesItems=TopRatedMovies;
        }
        else if(List_type.equals("favorite"))
        {
            MoviesItems=FavoriteMovie;
        }
        adapter=new MovieAdapter(MoviesItems,getActivity(),
                new MovieAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(Movie item) {
                       /* Intent intent=new Intent(getContext(),MovieDetails_activity.class);
                        intent.putExtra("Movie",item);
                        getView().getContext().startActivity(intent);*/
                        movieListener.setSelectedMovie(item);
                    }
                });
        MoviesList.setAdapter(adapter);
    }

    public void SetFavorite()
    {
        FavoriteMovie.clear();
        ArrayList<Movie> res=new ArrayList<>();
        Cursor cursor=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,null,null,null);
        ArrayList<String> FavList=new ArrayList<>();
        String item;
        if(cursor!=null && cursor.getCount()>0 )
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                item=cursor.getString(cursor.getColumnIndex("Title"));
                FavList.add(item);
                cursor.moveToNext();
            }
            for(int i=0 ; i<FavList.size() ; i++)
            {
                String MovieF=FavList.get(i);
                int found=0;
                for(int j=0 ; j<PopularMovies.size() ; j++)
                {
                    String MovieP=PopularMovies.get(j).getTitle();
                    if(MovieF.equals(MovieP))
                    {
                        FavoriteMovie.add(PopularMovies.get(j));
                        found=1;
                        break;
                    }
                }
                if(found==0)
                {
                    for(int j=0 ; j<TopRatedMovies.size() ; j++)
                    {
                        String MovieT=TopRatedMovies.get(j).getTitle();
                        if(MovieF.equals(MovieT))
                        {
                            FavoriteMovie.add(TopRatedMovies.get(j));
                            break;
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ItemID=item.getItemId();
        if(ItemID==R.id.popular)
        {
            List_type="popular";
            if(PopularMovies.isEmpty())
            {
                try {
                    GetData(List_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SetAdapter();
        }
        else if(ItemID==R.id.top)
        {
            List_type="top_rated";
            if(TopRatedMovies.isEmpty())
            {
                try {
                    GetData(List_type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SetAdapter();
        }
        else if(ItemID==R.id.fav)
        {
            List_type="favorite";
            SetFavorite();
            if(FavoriteMovie.size()>0) {
                SetAdapter();
            }
            else
                Toast.makeText(getContext(),"Favorite list is empty",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
