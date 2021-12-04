package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import edu.northeastern.cs5520.numadfa21_happytravel.place.PlaceUtils;

/**
 * Activity for check-in, we asks user to select stars and potentially upload pictures and review.
 */
public class CheckInActivity extends AppCompatActivity {

    PlacesClient client;

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

        PlaceUtils.getPlace(placeName, this.client)
                .addOnCompleteListener(
                        task -> {
                            Place place = task.getResult().getPlace();
                            TextView placeNameView = findViewById(R.id.check_in_place_name);
                            placeNameView.setText(place.getName());
                        });
    }
}
