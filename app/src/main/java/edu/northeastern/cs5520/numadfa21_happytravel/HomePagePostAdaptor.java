package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class HomePagePostAdaptor extends RecyclerView.Adapter<HomePagePostHolder> {
    private Context context;
    private ArrayList<Post> postList;
    private StorageReference reference;

    public HomePagePostAdaptor(Context context, StorageReference reference) {
        this.context = context;
        this.reference = reference;
        postList = new ArrayList<>();
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }
    @NonNull
    @Override
    public HomePagePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homepage_post_layout, parent, false);
        HomePagePostHolder homePagePostHolder = new HomePagePostHolder(view, context);
        return homePagePostHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull HomePagePostHolder holder, int position) {
        Post post = postList.get(position);
        holder.context = this.context;
        holder.ratingBar.setRating(Float.parseFloat(post.getStar()));
        holder.tvPlace.setText(post.getPlace());
        holder.tvTime.setText(post.getTime());
        if (post.getImageUrl().equals("")) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            reference.child(post.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri.toString()).into(holder.imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }
}
