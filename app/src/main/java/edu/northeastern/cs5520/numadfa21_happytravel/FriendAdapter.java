package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private Context context;
    private List<FriendPost> posts;

    public FriendAdapter(
            Context ct,
            List<FriendPost> posts) {
        this.context = ct;
        this.posts = posts;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_row, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendPost post = posts.get(position);
        if(!post.getImageUrl().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(post.getImageUrl());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(0);
        }
        holder.marker.setImageResource(R.drawable.marker);
        holder.name.setText(post.getUserName());
        holder.place.setText(post.getPlaceName());
        holder.review_content.setText(post.getContent());
        // review star
        holder.review_star.setRating(post.getReviewStars());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView image, marker;
        TextView name, place, review_content;
        RatingBar review_star;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageViewPlaceImage);
            int width = 300;
            int height = 300;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            image.setLayoutParams(parms);

            marker = itemView.findViewById(R.id.imageViewMarker);
            name = itemView.findViewById(R.id.textViewName);
            place = itemView.findViewById(R.id.textViewPlace);
            review_content = itemView.findViewById(R.id.textViewReviewContent);
            review_star = itemView.findViewById(R.id.ratingBarReviewStar);
            review_star.setRating(5);
        }
    }
}
