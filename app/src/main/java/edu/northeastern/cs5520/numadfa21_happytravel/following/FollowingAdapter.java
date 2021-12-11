package edu.northeastern.cs5520.numadfa21_happytravel.following;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.UserInfo;

public class FollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<UserInfo> followings;
    private String currentUserId;


    public FollowingAdapter(Context context, List<UserInfo> followings, String currentUserId) {
        this.context = context;
        this.followings = followings;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.following_item, parent, false);
        return new FollowingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserInfo user = followings.get(position);
        FollowingHolder followingHolder = (FollowingHolder) holder;

        followingHolder.getFollowingName().setText(user.getUserName());
        followingHolder.getFollowingEmail().setText(user.getEmail());

        if(!user.getProfileUrl().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(user.getProfileUrl());
            Glide.with(context)
                 .using(new FirebaseImageLoader())
                 .load(storageReference)
                 .into(followingHolder.getFollowingImage());
        } else {
            followingHolder.getFollowingImage().setImageResource(0);
        }

        followingHolder.getUnfollowButton().setOnClickListener(view -> {
            confirmationDialog(user.getKey(), position);
        });
    }

    private void confirmationDialog(String unfollowUserKey, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = dialogBuilder.create();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.unfollowing_confirmation, null, false);
        dialog.setView(dialogView);
        Button confirm = dialogView.findViewById(R.id.unfollowYes);
        Button cancel = dialogView.findViewById(R.id.unfollowNo);
        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        confirm.setOnClickListener(view -> {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserInfo");
            DatabaseReference currentUserRef = userRef.child(currentUserId);
            currentUserRef.get().addOnCompleteListener(task -> {
                UserInfo currentUser = task.getResult().getValue(UserInfo.class);
                currentUser.getFriends().remove(unfollowUserKey);
                currentUserRef.child("friends").setValue(currentUser.getFriends());
            });

            // Refresh adapter.
            followings.remove(position);
            notifyItemRemoved(position);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return this.followings.size();
    }
}
