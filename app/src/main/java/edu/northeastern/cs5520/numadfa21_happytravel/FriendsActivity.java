package edu.northeastern.cs5520.numadfa21_happytravel;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

    private Set<String> friendSet = new HashSet<>();
    private List<String> images = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> places = new ArrayList<>();
    private List<Float> review_stars = new ArrayList<>();
    private List<String> review_contents = new ArrayList<>();

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


        // show the posts of the given user
        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference().child("TravelHistory");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TravelHistory travelHistory = snapshot.getValue(TravelHistory.class);

                    if(friendSet.contains(travelHistory.getUser_id())) {
                        if(travelHistory.getReview_photo_path() == null) {
                            images.add("");
                        }else {
                            images.add(travelHistory.getReview_photo_path());
                        }
                        Log.v(TAG, travelHistory.toString());
                        names.add(travelHistory.getUser_name());
                        places.add(travelHistory.getPlace_name());
                        review_stars.add(Float.parseFloat(travelHistory.getReview_stars()));
                        review_contents.add(travelHistory.getReview_content());
                    }
                }

                float[] review_stars_array = new float[review_stars.size()];
                for(int i = 0; i < review_stars.size(); i++) {
                    review_stars_array[i] = review_stars.get(i);
                }

                Log.v(TAG, "generate view");

                recyclerView = findViewById(R.id.recyclerViewFriends);
                FriendAdpater friendAdapter = new FriendAdpater(FriendsActivity.this,
                        images.toArray(new String[images.size()]),
                        names.toArray(new String[names.size()]),
                        places.toArray(new String[places.size()]),
                        review_stars_array,
                        review_contents.toArray(new String[review_contents.size()]));
                recyclerView.setAdapter(friendAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
