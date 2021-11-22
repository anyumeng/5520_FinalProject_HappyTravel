package edu.northeastern.cs5520.yumengan.numadfa21_happytravel;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class HomePagePostHolder extends RecyclerView.ViewHolder {
    public Context context;
    public ImageView imageView;
    public TextView textView;
    public HomePagePostHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        imageView = itemView.findViewById(R.id.imgPostPicture);
        textView = itemView.findViewById(R.id.tvContext);
    }
}
