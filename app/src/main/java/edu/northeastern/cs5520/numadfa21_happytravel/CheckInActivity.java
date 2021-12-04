package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Optional;

import edu.northeastern.cs5520.numadfa21_happytravel.place.PlaceUtils;

/**
 * Activity for check-in, we asks user to select stars and potentially upload pictures and review.
 */
public class CheckInActivity extends AppCompatActivity {
    private static final String TAG = "CheckInUpload";

    private PlacesClient client;
    private RatingBar ratingBar;
    private Optional<Place> uploadPlace = Optional.empty();
    private String userId;
    private TextView reviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCy17aCHfCwb7B_Tka2hwS5SoHomaUzKM8");
        }
        this.client = Places.createClient(getApplicationContext());

        Intent intent = getIntent();
        String placeName = intent.getStringExtra("checkInPlace");
        this.userId = intent.getStringExtra("userId");
        Log.v(TAG, String.format("user id: %s", this.userId));

        PlaceUtils.getPlace(placeName, this.client)
                  .addOnCompleteListener(
                          task -> {
                              Place place = task.getResult().getPlace();
                              TextView placeNameView = findViewById(R.id.check_in_place_name);
                              placeNameView.setText(place.getName());
                              uploadPlace = Optional.of(place);
                          });

        this.ratingBar = findViewById(R.id.check_in_rating);
        this.reviewTextView = findViewById(R.id.check_in_review);
    }

    /**
     * On click operation for check in button, upload details to realtime database.
     *
     * @param view the current view.
     */
    public void uploadCheckIn(View view) {
        if (!uploadPlace.isPresent()) {
            Toast.makeText(this, "No place found", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("TravelHistory").push();
        if(this.userId == null) {
            this.userId = "unknown";
        }
        db.child("user_id").setValue(this.userId);
        db.child("place_id").setValue(this.uploadPlace.get().getId());
        db.child("review_content").setValue(this.reviewTextView.getText().toString());
        db.child("review_stars").setValue(String.valueOf(this.ratingBar.getRating()));

        Toast.makeText(this, "Check in successfully!", Toast.LENGTH_SHORT).show();
    }
}
