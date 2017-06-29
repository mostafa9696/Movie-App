package com.example.mostafa.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mostafa on 2/19/2017.
 */

public class Movie implements Parcelable {
    private String Title;
    private String Overview;
    private String MoviePoster;
    private Double Vote;
    private String Date;
    private int ID;

    public Movie(String data, String moviePoster, String overview, String title, Double vote ,int id ) {
        Date = data;
        MoviePoster = moviePoster;
        Overview = overview;
        Title = title;
        Vote = vote;
        ID=id;
    }

    protected Movie(Parcel in) {
        Title = in.readString();
        Overview = in.readString();
        MoviePoster = in.readString();
        Vote = in.readDouble();
        Date = in.readString();
        ID=in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setData(String data) {
        Date = data;
    }

    public void setMoviePoster(String moviePoster) {
        MoviePoster = moviePoster;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setVote(Double vote) {
        Vote = vote;
    }

    public static Creator<Movie> getCREATOR() {

        return CREATOR;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setDate(String date) {

        Date = date;
    }

    public int getID() {

        return ID;
    }

    public String getDate() {
        return Date;
    }

    public String getMoviePoster() {
        return MoviePoster;
    }

    public String getOverview() {
        return Overview;
    }

    public String getTitle() {
        return Title;
    }

    public Double getVote() {
        return Vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Title);
        parcel.writeString(Overview);
        parcel.writeString(MoviePoster);
        parcel.writeDouble(Vote);
        parcel.writeString(Date);
        parcel.writeInt(ID);
    }

}
