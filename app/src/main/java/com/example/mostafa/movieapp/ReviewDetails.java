package com.example.mostafa.movieapp;

/**
 * Created by Mostafa on 3/6/2017.
 */

public class ReviewDetails {
    private String ReviewAuthor;
    private String ReviewText;
    public ReviewDetails(String author, String text) {
        ReviewText = text;
        this.ReviewAuthor = author;
    }
    public String getAuthor(){return ReviewAuthor;
    }

    public void setText(String text) {
        ReviewText = text;
    }

    public String getText() {

        return ReviewText;
    }

    public void setAuthor(String author) {
        this.ReviewAuthor = author;
    }
}
