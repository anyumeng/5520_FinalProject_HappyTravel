package edu.northeastern.cs5520.numadfa21_happytravel;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import edu.northeastern.cs5520.numadfa21_happytravel.place.PlaceUtils;

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

        // first get the current user name
        String currentUserName = "ym an";
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount != null){
//            currentUserName = signInAccount.getDisplayName();
//        }
        Log.v(TAG, currentUserName);
        friendSet.add("ym an");

        // get the friend user id list of the current user
        // show the posts of the given user
        DatabaseReference reference_user =
                FirebaseDatabase.getInstance().getReference().child("UserInfo");

        reference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);

                    // find the current user
                    if(userInfo.getUserName().equals(currentUserName)) {
                        ArrayList<String> friendList = userInfo.getFriends();
                        friendSet.addAll(friendList);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // transfer place id to place name
        // create the place client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDxBQXCrUd95_va2_cSBz-KeadVfoa1Vio");
        }
        PlacesClient client = Places.createClient(getApplicationContext());


        // show the posts of the given user
        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference().child("TravelHistory");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TravelHistory travelHistory = snapshot.getValue(TravelHistory.class);

                    // should change to friendSet.contains(travelHistory.getUser_name())
                    if(friendSet.contains(travelHistory.getUser_name())) {
                        if(travelHistory.getReview_photo_path() == null) {
                            images.add("");
                        }else {
                            images.add(travelHistory.getReview_photo_path());
                        }

                        names.add(travelHistory.getUser_name());

//                        PlaceUtils.getPlace(travelHistory.getPlace_id(), client).addOnCompleteListener(
//                                task -> {
//                                    Log.v(TAG, task.getResult().getPlace().getName());
//                        });

                        places.add(travelHistory.getPlace_id().substring(0,4));
                        review_stars.add(Float.parseFloat(travelHistory.getReview_stars()));
                        review_contents.add(travelHistory.getReview_content());
                    }
                }

                float[] review_stars_array = new float[review_stars.size()];
                for(int i = 0; i < review_stars.size(); i++) {
                    review_stars_array[i] = review_stars.get(i);
                }

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
