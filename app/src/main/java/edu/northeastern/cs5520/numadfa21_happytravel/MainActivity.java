package edu.northeastern.cs5520.numadfa21_happytravel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import edu.northeastern.cs5520.numadfa21_happytravel.persistence.CheckDao;
import java.util.Arrays;
import java.util.Optional;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {
    private static final String TAG = "main_search";

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private Optional<String> selectedPlaceId = Optional.empty();

    // Google Sign in
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG2 = "GOOGLE SIGN IN";
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

        // The following 2 lines are for simple read/write test, feel free to remove them.
        CheckDao.simpleWrite();
        CheckDao.simpleRead();

        this.setupAutoComplete();

        // Google Sign in
        createSigninRequest();
    }

    // =========Start of Friend Activity===========

    public void openFriendActivity(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    // =========End of Friend Activity==========

    // ===========Start of Google Sign in Part================

    private void createSigninRequest() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(
                                "900066446915-f37btho57t91krdkv3ju7biqk5614kk8.apps"
                                        + ".googleusercontent.com")
                        .requestEmail()
                        .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        mAuth = FirebaseAuth.getInstance();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signIn();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG2, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG2, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's
                                    // information
                                    Log.d(TAG2, "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(
                                            TAG2,
                                            "signInWithCredential:failure",
                                            task.getException());
                                }
                                reload();
                            }
                        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), GoogleSigninInfoActivity.class));
    }

    GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

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
        String userId = this.mAuth.getUid();
        String checkInPlace = this.selectedPlaceId.get();
        Intent intent = new Intent(this, CheckInActivity.class);
        intent.putExtra("userId", userId);
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