package com.example.mostafa.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mostafa on 3/6/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.CustomViewHolder> {
    private List<TrailerDetails> trailerDetails;
    public Context context;
    private final TrailerAdapter.OnItemClickListener listener;
    public TrailerAdapter(List<TrailerDetails> List, Context con,OnItemClickListener listener ) {
        trailerDetails = List;
        this.listener = listener;
        context=con;
    }
    public interface OnItemClickListener {
        void onItemClick(TrailerDetails item);
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout,null);
        CustomViewHolder viewHolder=new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(trailerDetails.get(position),listener,context);
    }
    @Override
    public int getItemCount() {
        return trailerDetails.size();
    }
    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView textView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.textView=(TextView) itemView.findViewById(R.id.trailer);
        }
        public void bind(final  TrailerDetails Trailer , final TrailerAdapter.OnItemClickListener listener , Context con)
        {
            textView.setText(Trailer.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(Trailer);
                }
            });
        }
    }



}
