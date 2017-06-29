package com.example.mostafa.movieapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mostafa on 3/9/2017.
 */

public class MovieAsyncTask extends AsyncTask<Void,Void,String>{
    String API_key;
    String DataTybe;
    int MovieID;

    public MovieAsyncTask( String dataTybe, int movieID) {
        DataTybe = dataTybe;
        MovieID = movieID;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(DataTybe.equals("popular")||DataTybe.equals("top_rated"))
            API_key="https://api.themoviedb.org/3/movie/"+DataTybe+"?api_key=eda3b76ae69f864057c14a1225174796";
        else
                API_key="https://api.themoviedb.org/3/movie/"+MovieID+"/"+DataTybe+"?api_key=eda3b76ae69f864057c14a1225174796";
        String result="";
        HttpURLConnection httpURLConnection;
        try {
            URL url=new URL(API_key);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            int Check=httpURLConnection.getResponseCode();
            if(Check==200)  // 200 represent connection success
            {
                InputStream inputStream=httpURLConnection.getInputStream();
                InputStreamReader in=new InputStreamReader(inputStream);
                BufferedReader r=new BufferedReader(in);
                StringBuilder builder=new StringBuilder();
                String line;
                while((line=r.readLine())!=null) {
                    builder.append(line);
                }
                result=builder.toString();  // successful
            }
            else {
                result="No data"; // Failed
            }} catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }
    public ArrayList<Movie> GetMovies(String Res) throws JSONException {
        ArrayList<Movie> MoviesItems=new ArrayList<>();
        String Base="http://image.tmdb.org/t/p/w500/";
        JSONObject res=new JSONObject(Res);
        JSONArray Movies=res.getJSONArray("results");
        String title,img,desc,date;
        Double vote;
        int id=0;
        MoviesItems.clear();
        for(int i=0 ; i<Movies.length() ; i++)
        {
            JSONObject obj=Movies.getJSONObject(i);
            title= (String) obj.getString("original_title");
            img=Base+obj.getString("poster_path");
            vote= obj.getDouble("vote_average");
            date=obj.getString("release_date");
            desc=obj.getString("overview");
            id=obj.getInt("id");
            if(title.contains("'"))
            {
                title=title.replace("'"," ");  // character ',' cause exceptione in database
            }
                MoviesItems.add(new Movie(date,img,desc,title,vote,id));
        }
        return MoviesItems;
    }
    public ArrayList<TrailerDetails> GetTrailers(String Res) throws JSONException {
        ArrayList<TrailerDetails> TrailersItems=new ArrayList<>();
        JSONObject jsonObject=new JSONObject(Res);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        for(int i=0 ; i<jsonArray.length() ; i++)
        {
            JSONObject object=jsonArray.getJSONObject(i);
            String key=object.getString("key");
            String Title=object.getString("name");
            TrailersItems.add(new TrailerDetails(key,Title));
        }
        return TrailersItems;
    }
    public ArrayList<ReviewDetails> GetReviews(String Res)  throws JSONException
    {
        ArrayList<ReviewDetails> ReviewsItems=new ArrayList<>();
        JSONObject jsonObject=new JSONObject(Res);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        for(int i=0 ; i<jsonArray.length() ; i++)
        {
            JSONObject object=jsonArray.getJSONObject(i);
            String author=object.getString("author");
            String Text=object.getString("content");
            String Au="Author Name : "+author;
            ReviewsItems.add(new ReviewDetails(Au,Text));
        }
        return ReviewsItems;
    }
}
