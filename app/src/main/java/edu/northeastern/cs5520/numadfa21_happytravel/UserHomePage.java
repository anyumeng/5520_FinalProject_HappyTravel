package edu.northeastern.cs5520.numadfa21_happytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserHomePage extends AppCompatActivity {
    public String TAG = "UserHomePage";
    public static final int CAMERA_REQUEST = 200, STORAGE_REQUEST = 300, COVER_CODE = 10, PROFILE_CODE = 20;
    public String ITEM_NUM = "ITEM_NUM", KEY = "POST";
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public int REQUEST_CODE;
    private ImageView cover, profile, menu, popupProfile, addFriendButton, addFriendProfile;
    private FloatingActionButton fabCover, fabProfile;
    private String cameraPermission[];
    private String storagePermission[];
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText edtUserName, edtBirthday, edtRegion, edtFriend;
    private TextView tvUserName, tvError, tvFollowedError;
    private Button btnSubmit, btnCancel, btnFollow, btnFollowCancel;
    private RatingBar ratingBarPost;
    private RecyclerView recyclerView;
    private HomePagePostAdaptor adaptor;
    private static ArrayList<Post> postList = new ArrayList<>();
    private static ArrayList<TravelHistory> historyList = new ArrayList<>();


    private DatabaseReference userRef;
    private String currentUserId;
    private Optional<UserInfo> currentUser = Optional.empty();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        this.userRef = FirebaseDatabase.getInstance().getReference("UserInfo");
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cover = findViewById(R.id.imgBg);
        fabCover = findViewById(R.id.fabCover);
        profile = findViewById(R.id.imgProfile);
        fabProfile = findViewById(R.id.fabProfile);
        menu = findViewById(R.id.ic_menu);
        addFriendButton = findViewById(R.id.ic_plus);
        tvUserName = findViewById(R.id.tv_userName);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!currentUser.isPresent()) {
            getCurrentUser(currentUserId);
        } else {
            searchHistory();
            initUserInfo();
        }

        fabCover.setOnClickListener(view -> {
            REQUEST_CODE = 10;
            StoragePermission();
            ImagePicker.Companion.with(UserHomePage.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .cropOval()	    		//Allow dimmed layer to have a circle inside
                    .cropFreeStyle()	    //Let the user to resize crop bounds
                    .galleryOnly()          //We have to define what image provider we want to use
                    .createIntent();
        });

        fabProfile.setOnClickListener(view -> {
            REQUEST_CODE = 20;
            StoragePermission();
            ImagePicker.Companion.with(UserHomePage.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .cropOval()	    		//Allow dimmed layer to have a circle inside
                    .cropFreeStyle()	    //Let the user to resize crop bounds
                    .galleryOnly()          //We have to define what image provider we want to use
                    .createIntent();
        });

        menu.setOnClickListener(view -> showPopupMenu(view));

        addFriendButton.setOnClickListener(view -> createAddFriendDialog());

        createRecyclerView();
    }

    /**
     * Setup UI for all user info related components.
     */
    private void initUserInfo() {
        if(!currentUser.isPresent()) {
            return;
        }
        UserInfo user = currentUser.get();
        //load image
        if (user.getImageUrl() != null && user.getImageUrl().length() > 0) {
            storageRef.child(user.getImageUrl())
                      .getDownloadUrl()
                      .addOnSuccessListener(uri -> Glide.with(UserHomePage.this)
                                                        .load(uri.toString())
                                                        .into(cover))
                      .addOnFailureListener(exception -> {
                          // Handle any errors
                      });
        }
        if (user.getProfileUrl() != null && user.getProfileUrl().length() > 0) {
            storageRef.child(user.getProfileUrl())
                      .getDownloadUrl()
                      .addOnSuccessListener(uri -> Glide.with(UserHomePage.this)
                                                        .load(uri.toString())
                                                        .into(profile))
                      .addOnFailureListener(exception -> {
                          // Handle any errors
                      });
        }
        //load username
        tvUserName.setText(user.getUserName());
    }

    public void StoragePermission() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(UserHomePage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(UserHomePage.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(storagePermission, STORAGE_REQUEST);
            }
        }
        else
        {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(UserHomePage.this, "Please enable camera and write permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case STORAGE_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(UserHomePage.this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            if (requestCode == COVER_CODE) {
                uploadPhoto(uri, requestCode);
                cover.setImageURI(uri);
            }
            else if (requestCode == PROFILE_CODE) {
                uploadPhoto(uri, requestCode);
                profile.setImageURI(uri);
            }
        }
        else {
            Toast.makeText(UserHomePage.this, "Please select picture to replace the cover", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        int size = postList == null?0:postList.size();
        outState.putInt(ITEM_NUM, size);
        for (int i = 0; i < size; i++) {
            outState.putString(KEY + i + "url", postList.get(i).getImageUrl());
            outState.putString(KEY + i + "place", postList.get(i).getPlace());
            outState.putString(KEY + i + "time", postList.get(i).getTime());
            outState.putString(KEY + i + "rateBar", postList.get(i).getStar());
        }
        super.onSaveInstanceState(outState);
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_setting);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_setting_edit: {
                        createSettingDialog();
                        break;
                    }
                    case R.id.popup_setting_reward:{
                        Intent intentReward = new Intent(UserHomePage.this, RewardActivity.class);
                        startActivity(intentReward);
                        break;
                    }
                    case R.id.popup_setting_signout:{
                        signOut();
                    }
                    default: break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    /**
     * Signout the current user, and jump to the LoginAcitivty.
     */
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(
                                "900066446915-f37btho57t91krdkv3ju7biqk5614kk8.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
        GoogleSignInClient googleClient =
                GoogleSignIn.getClient(getApplicationContext(), options);
        googleClient.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Create an add friend dialog to let user add a single friend by input user name.
     */
    private void createAddFriendDialog() {
        dialogBuilder = new AlertDialog.Builder(UserHomePage.this);
        View settingPopup = getLayoutInflater().inflate(R.layout.addfriend_popup, null);
        edtFriend = settingPopup.findViewById(R.id.edtFriend);
        btnFollow = settingPopup.findViewById(R.id.btnSubmitAddFriend);
        btnFollowCancel = settingPopup.findViewById(R.id.btnCancelAddFriend);
        addFriendProfile = settingPopup.findViewById(R.id.imgProfileAdd);
        tvError = settingPopup.findViewById(R.id.tvError);
        tvFollowedError = settingPopup.findViewById(R.id.tvFollowedError);
        if (!currentUser.isPresent()) {
            addFriendProfile.setImageResource(R.mipmap.defaultphoto);
        } else {
            UserInfo user = currentUser.get();
            if (user.getProfileUrl() != null && user.getProfileUrl().length() > 0) {
                Glide.with(UserHomePage.this).load(user.getProfileUrl()).into(addFriendProfile);
            }
        }

        dialogBuilder.setView(settingPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnFollowCancel.setOnClickListener(view -> {
            tvError.setVisibility(View.INVISIBLE);
            tvFollowedError.setVisibility(View.INVISIBLE);
            dialog.dismiss();
        });

        btnFollow.setOnClickListener(view -> {
            String friendEmail = edtFriend.getText().toString();
            this.userRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    UserInfo myself = this.currentUser.get();
                    for (DataSnapshot userSnapshot :task.getResult().getChildren()) {
                        UserInfo friend = userSnapshot.getValue(UserInfo.class);
                        friend.setKey(userSnapshot.getKey());
                        if(friend.getEmail() != null && friend.getEmail().equals(friendEmail)) {

// Uncommment the following code if you want add follower in the list of followed's
//                            ArrayList<String> friendIds = friend.getFriends();
//                            if (friendIds.contains(myself.getKey())) {
//                                tvFollowedError.setVisibility(View.VISIBLE);
//                            }
//                            else {
//                                friend.getFriends().add(myself.getKey());
//                                Toast.makeText(UserHomePage.this, userSnapshot.getKey(), Toast.LENGTH_SHORT).show();
//                                userRef.child(friend.getKey()).child("friends").setValue(friend.getFriends());
//                            }
                            if (myself.getFriends().contains(friend.getKey())) {
                                Toast.makeText(UserHomePage.this, String.format("You've followed: %s", friend.getUserName()), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserHomePage.this, String.format("Follow successfully: %s", friend.getUserName()), Toast.LENGTH_SHORT).show();
                                myself.getFriends().add(friend.getKey());
                                userRef.child(myself.getKey()).child("friends").setValue(myself.getFriends());
                            }
                            return;
                        }
                    }
                    Toast.makeText(UserHomePage.this, String.format("This user does not exist: %s", friendEmail), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    /**
     * Create the setting dialog that let user change their profiles.
     */
    private void createSettingDialog() {
        dialogBuilder = new AlertDialog.Builder(UserHomePage.this);
        View settingPopup = getLayoutInflater().inflate(R.layout.homepage_popup, null);
        edtUserName = settingPopup.findViewById(R.id.edtUserName);
        edtBirthday = settingPopup.findViewById(R.id.edtBirthday);
        edtRegion = settingPopup.findViewById(R.id.edtRegion);
        btnSubmit = settingPopup.findViewById(R.id.btnSubmit);
        btnCancel = settingPopup.findViewById(R.id.btnCancel);
        popupProfile = settingPopup.findViewById(R.id.imgProfileAdd);

        UserInfo user = currentUser.get();

        edtUserName.setText(user.getUserName());
        edtBirthday.setText(user.getBirthday());
        edtRegion.setText(user.getRegion());
        if (user.getProfileUrl() != null && user.getProfileUrl().length() > 0) {
            Glide.with(UserHomePage.this).load(currentUser.get().getProfileUrl()).into(popupProfile);
        }

        dialogBuilder.setView(settingPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        btnSubmit.setOnClickListener(view -> {
            String newUserName = edtUserName.getText().toString();
            String newBirthday = edtBirthday.getText().toString();
            String newRegion = edtRegion.getText().toString();
            if (isValidInfo(newUserName, newBirthday)) {
                UserInfo newUser = currentUser.get();
                newUser.setUserName(newUserName);
                newUser.setBirthday(newBirthday);
                newUser.setRegion(newRegion);
                userRef.child(currentUserId).setValue(newUser);
                Toast.makeText(UserHomePage.this, "Update Successfully", Toast.LENGTH_SHORT)
                     .show();
            } else {
                Toast.makeText(UserHomePage.this, "invalid information", Toast.LENGTH_SHORT)
                     .show();
            }
            dialog.dismiss();
        });
    }

    private void getCurrentUser(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                                    .getReference("UserInfo")
                                                    .child(userId);
        userRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                this.currentUser = Optional.of(task.getResult().getValue(UserInfo.class));
                this.currentUser.get().setKey(task.getResult().getKey());
                searchHistory();
                initUserInfo();
            }
        });
    }

    private void searchHistory() {
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("TravelHistory");
        historyRef.orderByChild("user_id")
                .equalTo(this.currentUser.get().getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int size = historyList.size();
                        historyList.clear();
                        postList.clear();
                        adaptor.notifyItemRangeRemoved(0, size);
                        int i = 0;
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                            UserInfo user = childSnapshot.getValue(UserInfo.class);
                            TravelHistory history = childSnapshot.getValue(TravelHistory.class);
                            history.setKey(childSnapshot.getKey());
                            historyList.add(history);
                            Post post = new Post(history.getReview_photo_path(), UserHomePage.this, history.getPlace_name(), history.getReview_time(), history.getReview_stars());
                            postList.add(post);
                        }
                        adaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    private boolean isValidInfo(String userName, String birthday) {
        if(userName == null || userName.isEmpty() || birthday == null) {
            return false;
        }
        Toast.makeText(UserHomePage.this, birthday, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        df.setLenient(false);
        try {
            df.parse(birthday);
            return true;
        }
        catch (ParseException e) {
            Toast.makeText(UserHomePage.this, birthday, Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
    }

    private void uploadPhoto(Uri uri, int code){
        String folder = code == COVER_CODE ? "covers" : "profiles";
        String filePath = String.format("%s/%s", folder, UUID.randomUUID().toString());
        this.storageRef.child(filePath).putFile(uri).addOnSuccessListener(taskSnapshot -> {
            UserInfo user = currentUser.get();
            if (code == COVER_CODE) {
                user.setImageUrl(filePath);
            }
            else if (code == PROFILE_CODE) {
                user.setProfileUrl(filePath);
            }
            userRef.child(user.getKey()).setValue(user);
            Toast.makeText(UserHomePage.this, user.toString(), Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
        }).addOnFailureListener(e -> {
            Toast.makeText(UserHomePage.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
    public void init(Bundle savedInstanceState) {
        initialItem(savedInstanceState);
//        createRecyclerView();
    }

    public void initialItem(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(ITEM_NUM)) {
            if (postList == null || postList.size() == 0) {
                int size = savedInstanceState.getInt(ITEM_NUM);
                for (int i = 0; i < size; i++) {
                    String url = savedInstanceState.getString(KEY + i + "url");
                    String place = savedInstanceState.getString(KEY + i + "place");
                    String time = savedInstanceState.getString(KEY + i + "time");
                    String rateBar = savedInstanceState.getString(KEY + i + "rateBar");
                    postList.add(new Post(url, UserHomePage.this, place, time, rateBar));
                }
            }
        }
    }
    public void createRecyclerView() {
        recyclerView = findViewById(R.id.rvPost);
        recyclerView.setHasFixedSize(true);
        adaptor = new HomePagePostAdaptor(UserHomePage.this, storageRef);
        adaptor.setPostList(postList);
        adaptor.setHistoryList(historyList);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItem(int pos, Post newPost) {
        postList.add(pos, newPost);
        adaptor.notifyItemInserted(pos);
    }

}