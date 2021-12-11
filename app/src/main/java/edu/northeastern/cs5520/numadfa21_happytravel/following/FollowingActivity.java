package edu.northeastern.cs5520.numadfa21_happytravel.following;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.UserInfo;

public class FollowingActivity extends AppCompatActivity {
    private String currentUserId;
    private RecyclerView recyclerView;
    private FollowingAdapter adapter;
    private List<UserInfo> followings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(this.currentUserId == null) {
            Toast.makeText(this, "could not find current user", Toast.LENGTH_LONG).show();
            return;
        }

        createRecyclerView();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        DatabaseReference currentUserRef = userRef.child(currentUserId);

        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo currentUser = dataSnapshot.getValue(UserInfo.class);
                List<String> followingIds = currentUser.getFriends();
                // for all friends, add their user info to the recylcer view.

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int size = followings.size();
                        followings.clear();
                        adapter.notifyItemRangeRemoved(0, size);
                        for(DataSnapshot userSnapshot: snapshot.getChildren()) {
                            UserInfo user = userSnapshot.getValue(UserInfo.class);
                            user.setKey(userSnapshot.getKey());
                            if(followingIds.contains(user.getKey())) {
                                followings.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFollowings);
        recyclerView.setHasFixedSize(true);
        followings = new ArrayList<>();
        adapter = new FollowingAdapter(this, this.followings, this.currentUserId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}