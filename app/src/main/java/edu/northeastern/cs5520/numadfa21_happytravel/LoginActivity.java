package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import edu.northeastern.cs5520.numadfa21_happytravel.model.PlaceTypeMapping;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "google_sign_in";

    // Google Sign in
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG2 = "GOOGLE SIGN IN";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Google Sign in
        createSigninRequest();
    }

    // ===========Start of Google Sign in Part================

    private void createSigninRequest() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(
                                "900066446915-f37btho57t91krdkv3ju7biqk5614kk8.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.v(TAG, String.format("account:%s", mAuth.getCurrentUser().getEmail()));
            goMain();
        }

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

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
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
                                    Log.d(TAG, "signInWithCredential:success");
                                    updateNewUser(task.getResult().getUser());
                                    goMain();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(
                                            TAG2,
                                            "signInWithCredential:failure",
                                            task.getException());
                                }
                                // reload();
                            }
                        });
    }

    private void updateNewUser(FirebaseUser user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(user.getDisplayName());
        userInfo.setFriends(new ArrayList<>());
        userInfo.setEmail(user.getEmail());
        userInfo.setRegion("");
        userInfo.setBirthday("");
        userInfo.setProfileUrl("");
        userInfo.setImageUrl("");
        Map<String, Integer> posts = PlaceTypeMapping.getAllIds().stream().collect(Collectors.toMap(id -> id, id -> 0));
        posts.put("post", 0);
        userInfo.setPost(posts);
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference("UserInfo");
        userDb.child(user.getUid()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().getValue() != null) {
                Log.v(TAG, task.getResult().toString());
                return;
            }
            Log.v(TAG, user.getUid());
            userDb.child(user.getUid()).setValue(userInfo);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), GoogleSigninInfoActivity.class));
    }

    GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }
}
