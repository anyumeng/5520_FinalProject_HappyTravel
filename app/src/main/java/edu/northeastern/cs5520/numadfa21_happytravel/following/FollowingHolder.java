package edu.northeastern.cs5520.numadfa21_happytravel.following;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.cs5520.numadfa21_happytravel.R;

public class FollowingHolder extends RecyclerView.ViewHolder {
    private TextView followingName;
    private TextView followingEmail;
    private ImageView followingImage;
    private Button unfollowButton;

    public FollowingHolder(View itemView) {
        super(itemView);
        this.followingImage = itemView.findViewById(R.id.followingImage);
        this.followingName = itemView.findViewById(R.id.followingName);
        this.followingEmail = itemView.findViewById(R.id.followingEmail);
        this.unfollowButton = itemView.findViewById(R.id.unfollowButton);
    }

    public TextView getFollowingName() {
        return this.followingName;
    }

    public ImageView getFollowingImage() {
        return this.followingImage;
    }

    public TextView getFollowingEmail() {
        return this.followingEmail;
    }

    public Button getUnfollowButton() {
        return this.unfollowButton;
    }
}
