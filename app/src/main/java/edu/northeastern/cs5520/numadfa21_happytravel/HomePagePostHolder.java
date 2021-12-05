package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class HomePagePostHolder extends RecyclerView.ViewHolder {
    public Context context;
    public ImageView imageView;
    public TextView tvPlace, tvTime;
    public RatingBar ratingBar;
    public HomePagePostHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        imageView = itemView.findViewById(R.id.imgPlace);
        tvPlace = itemView.findViewById(R.id.tvPlace);
        tvTime = itemView.findViewById(R.id.tvTime);
        ratingBar = itemView.findViewById(R.id.ratingBarUser);
    }
}
