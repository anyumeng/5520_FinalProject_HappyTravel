package edu.northeastern.cs5520.numadfa21_happytravel;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsActivity extends AppCompatActivity {
    private String TAG = "==========FRIENDS ACTIVITY==============";

    private RecyclerView recyclerView;
    private FriendAdapter adapter;

    private Set<String> friendSet = new HashSet<>();
    private List<FriendPost> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

//        // first get the current user name
//        String currentUser = "";
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount != null){
//            currentUser = signInAccount.getDisplayName();
//        }
//        String currentUserName = currentUser;
//        Log.v(TAG, currentUserName);
//        Log.v(TAG, currentUser);

        // first get the current user id
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v(TAG, currentUserId);

        // get the friend list of the current user
        DatabaseReference reference_user =
                FirebaseDatabase.getInstance().getReference().child("UserInfo");

        reference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);

                    // find the current user
                    if(snapshot.getKey().equals(currentUserId)) {
                        ArrayList<String> friendList = userInfo.getFriends();
                        friendSet.addAll(friendList);
                        Log.v(TAG, String.format("Get all friend ids: %s", userInfo));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        initRecyclerView();

        // show the posts of the given user
        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference().child("TravelHistory");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = posts.size();
                posts.clear();
                adapter.notifyItemRangeRemoved(0, size);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TravelHistory travelHistory = snapshot.getValue(TravelHistory.class);
                    if(friendSet.contains(travelHistory.getUser_id())) {
                        FriendPost post = new FriendPost();
                        if(travelHistory.getReview_photo_path() == null) {
                            post.setImageUrl("");
                        } else {
                            post.setImageUrl(travelHistory.getReview_photo_path());
                        }
                        post.setUserName(travelHistory.getUser_name());
                        post.setPlaceName(travelHistory.getPlace_name());
                        post.setReviewStars(Float.parseFloat(travelHistory.getReview_stars()));
                        post.setContent(travelHistory.getReview_content());
                        posts.add(0, post);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFriends);
        recyclerView.setHasFixedSize(true);
        adapter = new FriendAdapter(FriendsActivity.this, this.posts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
    }
}
