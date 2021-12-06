package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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
    private Spinner spinner;
    private ImageButton image;
    private ActivityResultLauncher<String> mGetContent;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Optional<String> photoPath = Optional.empty();
    private Optional<Uri> photoUri = Optional.empty();

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

        this.spinner = (Spinner) findViewById(R.id.check_in_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.check_in_spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Image Button for upload image.
        this.image = findViewById(R.id.check_in_photo);
        if(savedInstanceState != null && savedInstanceState.containsKey("uri")) {
            Uri uri = Uri.parse(savedInstanceState.getString("uri"));
            this.image.setImageURI(uri);
            this.photoUri = Optional.of(uri);
        }
        if(savedInstanceState != null && savedInstanceState.containsKey("photo_path")) {
            this.photoPath = Optional.of(savedInstanceState.getString("photo_path"));
        }
        this.mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Log.v(TAG, uri.toString());
                        this.image.setImageURI(uri);
                        this.photoPath = Optional.of(this.uploadPhoto(uri));
                        this.photoUri = Optional.of(uri);
                    }
                });
        this.image.setOnClickListener(this::choosePhoto);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        if(this.photoUri.isPresent()) {
            outState.putString("uri", this.photoUri.get().toString());
        }
        if(this.photoPath.isPresent()) {
            outState.putString("photo_path", this.photoPath.get());
        }
    }

    /**
     * Upload a photo to cloud storage
     * @param uri the uri for the photo
     * @return the file path for this photo.
     */
    private String uploadPhoto(Uri uri) {
        String filePath = String.format("%s/%s.jpg", "reviews", UUID.randomUUID().toString());
        StorageReference imageFile = this.storage.getReference(filePath);
        imageFile.putFile(uri);
        return filePath;
    }

    /**
     * On click operation to let use choose photo.
     *
     * @param view the view of the button.
     */
    private void choosePhoto(View view) {
        mGetContent.launch("image/*");
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
        db.child("type").setValue(this.spinner.getSelectedItem().toString());
        db.child("review_photo_path").setValue(this.photoPath.orElse(""));
        db.child("review_time").setValue(Instant.now().toString());

        Toast.makeText(this, "Check in successfully!", Toast.LENGTH_SHORT).show();
    }
}
