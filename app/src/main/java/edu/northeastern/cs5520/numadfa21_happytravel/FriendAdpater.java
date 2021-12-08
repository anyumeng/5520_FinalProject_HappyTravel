package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
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

import java.io.InputStream;

public class FriendAdpater extends RecyclerView.Adapter<FriendAdpater.FriendViewHolder> {
    private Context context;
    private String[] images;
    private String[] names;
    private String[] places;
    private float[] review_stars;
    private String[] review_contents;

    public FriendAdpater(
            Context ct,
            String[] images,
            String[] names,
            String[] places,
            float[] review_stars,
            String[] review_contents) {
        this.context = ct;
        this.images = images;
        this.names = names;
        this.places = places;
        this.review_stars = review_stars;
        this.review_contents = review_contents;
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(images[position]);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.image);
        holder.name.setText(names[position]);
        holder.place.setText(places[position]);
        holder.review_content.setText(review_contents[position]);
        // review star
        holder.review_star.setRating(review_stars[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, place, review_content;
        RatingBar review_star;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageViewPlaceImage);
            int width = 300;
            int height = 300;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            image.setLayoutParams(parms);

            name = itemView.findViewById(R.id.textViewName);
            place = itemView.findViewById(R.id.textViewPlace);
            review_content = itemView.findViewById(R.id.textViewReviewContent);
            review_star = itemView.findViewById(R.id.ratingBarReviewStar);
            review_star.setRating(5);
        }
    }
}
