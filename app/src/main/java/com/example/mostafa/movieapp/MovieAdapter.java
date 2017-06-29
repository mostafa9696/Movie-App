package com.example.mostafa.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mostafa on 2/19/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CustomViewHolder> {
    private List<Movie> MoviesList;
    public Context context;
    private final OnItemClickListener listener;
    public MovieAdapter(List<Movie> moviesList, Context con,OnItemClickListener listener ) {
        MoviesList = moviesList;
        this.listener = listener;
        context=con;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie item);
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,null);
        CustomViewHolder viewHolder=new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(MoviesList.get(position),listener,context);
    }

    @Override
    public int getItemCount() {
        return MoviesList.size();
    }
    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView imageView;
        protected TextView textView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.imageView=(ImageView)itemView.findViewById(R.id.img_movie);
            this.textView=(TextView)itemView.findViewById(R.id.MovieName);
        }
        public void bind(final  Movie movie , final OnItemClickListener listener , Context con)
        {
            Picasso.with(con).load(movie.getMoviePoster()).into(imageView);
            textView.setText(movie.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(movie);
                }
            });
        }
    }
}
