package edu.northeastern.cs5520.numadfa21_happytravel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "main_search";

    private FusedLocationProviderClient fusedLocationClient;
    private TextInputLayout textInputLayout;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textInputLayout = findViewById(R.id.search_input_layout);
        this.textInputLayout.bringToFront();
        this.setupSearchBar();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tryUpdateLocation();
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
                                // Got last known location. In some rare
                                // situations this
                                // can be null.
                                // TextView explain = findViewById(R.id.explain);
                                LatLng latlng =
                                        new LatLng(location.getLatitude(), location.getLongitude());
                                if (location != null) {
                                    googleMap.animateCamera(
                                            CameraUpdateFactory.newCameraPosition(
                                                    CameraPosition.builder()
                                                            .target(latlng)
                                                            .zoom(20)
                                                            .build()));
                                    googleMap.addMarker(
                                            new MarkerOptions()
                                                    .position(
                                                            new LatLng(
                                                                    location.getLatitude(),
                                                                    location.getLongitude()))
                                                    .title("Marker"));
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

    private void setupSearchBar() {
        this.textInputLayout.setEndIconOnClickListener(
                view -> {
                    // Log.v(TAG, "search bar!");
                    Toast.makeText(this, "Searched!", Toast.LENGTH_LONG).show();
                });
    }

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
