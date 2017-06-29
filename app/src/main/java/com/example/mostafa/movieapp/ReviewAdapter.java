package com.example.mostafa.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mostafa on 3/6/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {
    private List<ReviewDetails> ReviewList;
    public Context context;
    public ReviewAdapter(List<ReviewDetails> List, Context con) {
        ReviewList = List;
        context=con;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,null);
        CustomViewHolder viewHolder=new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(ReviewList.get(position),context);
    }
    @Override
    public int getItemCount() {
        return ReviewList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView reviewer,reviewText;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.reviewer=(TextView) itemView.findViewById(R.id.reviewer);
            this.reviewText=(TextView) itemView.findViewById(R.id.review);
        }
        public void bind(final  ReviewDetails Review  , Context con)
        {

            reviewer.setText(Review.getAuthor());
            reviewText.setText(Review.getText());
        }
    }
}
