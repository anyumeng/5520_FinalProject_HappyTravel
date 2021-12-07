package edu.northeastern.cs5520.numadfa21_happytravel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.Optional;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {
    private static final String TAG = "main_search";

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private Optional<String> selectedPlaceId = Optional.empty();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tryUpdateLocation();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();

        this.setupAutoComplete();
    }

    // =========Start of Friend Activity===========

    public void openFriendActivity(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public void openRewardActivity(View view) {
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
    }

    // =========End of Friend Activity==========

    // ===============End of Google Sign in Part================

    private void setupAutoComplete() {
        // Setup auto completion view.
        // Initialize the AutocompleteSupportFragment.
        Places.initialize(getApplicationContext(), "AIzaSyCy17aCHfCwb7B_Tka2hwS5SoHomaUzKM8");

        this.autocompleteFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
                Arrays.asList(
                        Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                        selectedPlaceId = Optional.of(place.getId());
                        gotoLocation(place.getLatLng());
                    }

                    @Override
                    public void onError(@NonNull Status status) {}
                });
    }

    /**
     * Try get location information and show them, if we cannot get permission, then update UI to
     * let user know.
     */
    private void tryUpdateLocation() {
        // If successfully got location, then just show them.
        if (tryShowLocation()) {
            return;
        }

        // If not, then try get permission and try show again.
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            if (isGranted) {
                                tryShowLocation();
                            }
                        });
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * A helper function to try show location information.
     *
     * @return true if we successfully got permission and showed, otherwise false.
     */
    private boolean tryShowLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.fusedLocationClient
                    .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(
                            this,
                            location -> {
                                if (location != null) {
                                    LatLng latlng =
                                            new LatLng(
                                                    location.getLatitude(),
                                                    location.getLongitude());
                                    gotoLocation(latlng);
                                    Log.v(TAG, location.toString());
                                    // explain.setText(R.string.location_updated);
                                } else {
                                    Toast.makeText(
                                            this, "Could not get location", Toast.LENGTH_LONG);
                                }
                            });
            return true;
        }
        return false;
    }

    /**
     * Move the current map to the given location, also created a marker on the location.
     *
     * @param latLng the location to move to.
     */
    private void gotoLocation(LatLng latLng) {
        googleMap.clear();
        googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                        CameraPosition.builder().target(latLng).zoom(15).build()));
        googleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Marker"));
    }

    /**
     * Show pop up menu for the plus button
     *
     * @param view the current view.
     */
    public void showPopup(View view) {
        View showView = LayoutInflater.from(this).inflate(R.layout.main_popup, null);
        ImageButton popupButton = findViewById(R.id.main_menu_button);

        PopupWindow popupWindow =
                new PopupWindow(
                        showView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
        popupWindow.setContentView(showView);
        showView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        popupWindow.showAsDropDown(
                popupButton, 0, -popupButton.getHeight() - showView.getMeasuredHeight());
    }

    /**
     * Go to check-in activity.
     *
     * @param view the current view.
     */
    public void checkIn(View view) {
        if (!this.selectedPlaceId.isPresent()) {
            Toast.makeText(
                            this,
                            String.format("You must select a place before check-in!"),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String checkInPlace = this.selectedPlaceId.get();
        Intent intent = new Intent(this, CheckInActivity.class);
        FirebaseUser user = this.mAuth.getCurrentUser();
        intent.putExtra("userId", user.getUid());
        intent.putExtra("userEmail", user.getEmail());
        intent.putExtra("userName", user.getDisplayName());
        intent.putExtra("checkInPlace", checkInPlace);
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnPoiClickListener(this);
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
        this.selectedPlaceId = Optional.of(pointOfInterest.placeId);
        gotoLocation(pointOfInterest.latLng);
        EditText editText =
                this.autocompleteFragment
                        .getView()
                        .findViewById(R.id.places_autocomplete_search_input);
        editText.setText(pointOfInterest.name);
    }
}
