package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import edu.northeastern.cs5520.numadfa21_happytravel.model.PlaceTypeMapping;


public class HomePagePostAdaptor extends RecyclerView.Adapter<HomePagePostHolder> {
    private Context context;
    private ArrayList<Post> postList;
    private ArrayList<TravelHistory> historyList;
    private StorageReference reference;
    private AlertDialog.Builder dialogBuilder;
    private TextView tvPostPlace, tvPostContent, tvPostTime;
    private Button btnDelete, btnBack;
    private RatingBar ratingBarPost;
    private ImageView postImg;
    private AlertDialog dialog;
    public HomePagePostAdaptor(Context context, StorageReference reference) {
        this.context = context;
        this.reference = reference;
        postList = new ArrayList<>();
        historyList = new ArrayList<>();
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }

    public void setHistoryList(ArrayList<TravelHistory> historyList) {
        this.historyList = historyList;
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPostDialog(position);
            }
        });

    }

    public void createPostDialog(int position) {
        TravelHistory history = historyList.get(position);
        dialogBuilder = new AlertDialog.Builder(context);
        View settingPopup = LayoutInflater.from(context).inflate(R.layout.post_popup, null);
        postImg = settingPopup.findViewById(R.id.imgPlacePost);
        btnDelete = settingPopup.findViewById(R.id.btnDeletePost);
        btnBack = settingPopup.findViewById(R.id.btnBackPost);
        tvPostPlace = settingPopup.findViewById(R.id.tvPlacePopup);
        tvPostTime = settingPopup.findViewById(R.id.tvTimePopup);
        tvPostContent = settingPopup.findViewById(R.id.tvContentPopup);
        ratingBarPost = settingPopup.findViewById(R.id.ratingBarPostPopup);

        if (history.getReview_photo_path() == null || history.getReview_photo_path().equals("")) {
            postImg.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            reference.child(history.getReview_photo_path()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri.toString()).into(postImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        tvPostPlace.setText(history.getPlace_id());
        tvPostContent.setText(history.getReview_content());
        tvPostTime.setText(history.getReview_time());
        ratingBarPost.setRating(Float.parseFloat(history.getReview_stars()));

        dialogBuilder.setView(settingPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(TravelHistory.class.getSimpleName());
                String postKey = history.getKey();
                rootUser.child(postKey).removeValue().addOnSuccessListener(suc-> {
                    Toast.makeText(context, "Record is deleted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(fail->{
                    Toast.makeText(context, "Fail to delete", Toast.LENGTH_SHORT).show();
                });
                String userKey = history.getUser_id();
                String type = PlaceTypeMapping.getIdByName(history.getType());
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(userKey);
                userRef.get().addOnCompleteListener(task ->  {
                    if(task.isSuccessful()) {
                        UserInfo user = task.getResult().getValue(UserInfo.class);
                        Map<String, Integer> post = user.getPost();
                        if(post.containsKey(type)) {
                            post.put(type, post.get(type) - 1);
                        }
                        if(post.containsKey("post")) {
                            post.put("post", post.get("post") - 1);
                        }
                        userRef.child("post").setValue(post);
                    }
                });
                postList.remove(position);
                historyList.remove(position);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }
}
