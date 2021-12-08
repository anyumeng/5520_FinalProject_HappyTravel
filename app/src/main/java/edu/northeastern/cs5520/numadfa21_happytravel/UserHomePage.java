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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
//import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import edu.northeastern.cs5520.numadfa21_happytravel.R;

public class UserHomePage extends AppCompatActivity {
    public String TAG = "UserHomePage";
    public static final int CAMERA_REQUEST = 200, STORAGE_REQUEST = 300, COVER_CODE = 10, PROFILE_CODE = 20;
    public String ITEM_NUM = "ITEM_NUM", KEY = "POST";
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    public int REQUEST_CODE;
    private ImageView cover, profile, ic_menu, popupProfile, ic_plus, addFriendProfile, postImg;
    private FloatingActionButton fabCover, fabProfile;
    private String cameraPermission[];
    private String storagePermission[];
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText edtUserName, edtBirthday, edtRegion, edtFriend;
    private TextView tvUserName, tvError, tvFollowedError, tvPostPlace, tvPostContent, tvPostTime;
    private Button btnSubmit, btnCancel, btnFollow, btnFollowCancel, btnDelete, btnBack;
    private RatingBar ratingBarPost;
    private RecyclerView recyclerView;
    private HomePagePostAdaptor adaptor;
    private static ArrayList<Post> postList = new ArrayList<>();
    private static ArrayList<TravelHistory> historyList = new ArrayList<>();
    private String currentUserName;
    private static UserInfo currentUser = null;
    private static HashSet<String> nameSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
//        DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(UserInfo.class.getSimpleName());
//        ArrayList<String> temp = new ArrayList<>();
//        UserInfo user = new UserInfo("dtt", "China", "", "", "", temp);
//        rootUser.push().setValue(user);

        cover = findViewById(R.id.imgBg);
        fabCover = findViewById(R.id.fabCover);
        profile = findViewById(R.id.imgProfile);
        fabProfile = findViewById(R.id.fabProfile);
        ic_menu = findViewById(R.id.ic_menu);
        ic_plus = findViewById(R.id.ic_plus);
        tvUserName = findViewById(R.id.tv_userName);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
//        init(savedInstanceState);
//        if (postList == null || postList.size() == 0) {
//        postList = new ArrayList<>();
        searchHistory(TravelHistory.class.getSimpleName(), "review_time", "2021-11-29T08:51:30.927Z");

//        }
//        else{
//            createRecyclerView();
//        }
        this.currentUserName = "cst";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            this.currentUserName = MainActivity.myName;
            this.currentUserName = "cst";
        }
        if (currentUser == null) {
            readFormFirebase(UserInfo.class.getSimpleName(), "userName", currentUserName);
        }
        else {
            initUserInfo();
        }
        if (nameSet == null) {
            nameSet = new HashSet<>();
            searchUserName(UserInfo.class.getSimpleName());
        }


//        postList.add(0, new Post("", "Hello"));
//        addItem(0,  new Post("", "World"));
        fabCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REQUEST_CODE = 10;
                StoragePermission();
//                CameraPermission();
                ImagePicker.Companion.with(UserHomePage.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .cropOval()	    		//Allow dimmed layer to have a circle inside
                        .cropFreeStyle()	    //Let the user to resize crop bounds
                        .galleryOnly()          //We have to define what image provider we want to use
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent();
            }
        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REQUEST_CODE = 20;
                StoragePermission();
//                CameraPermission();
                ImagePicker.Companion.with(UserHomePage.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .cropOval()	    		//Allow dimmed layer to have a circle inside
                        .cropFreeStyle()	    //Let the user to resize crop bounds
                        .galleryOnly()          //We have to define what image provider we want to use
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent();
            }
        });

        ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditPopup(view);
            }
        });

        ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddFriendDialog();

            }
        });
    }

    /*
    public void CameraPermission() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(UserHomePage.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openGallery(CAMERA_REQUEST);
            } else {
                requestPermissions(cameraPermission, CAMERA_REQUEST);
            }
        }
        else
        {
            openGallery(CAMERA_REQUEST);
        }
    }
     */
    public void initUserInfo() {
        //load image
        if (currentUser.getImageUrl() != null || currentUser.getImageUrl().length() > 0) {
            Glide.with(UserHomePage.this).load(currentUser.getImageUrl()).into(cover);
        }
        if (currentUser.getProfileUrl() != null || currentUser.getProfileUrl().length() > 0) {
            Glide.with(UserHomePage.this).load(currentUser.getProfileUrl()).into(profile);
        }
        //load username
        tvUserName.setText(currentUser.getUserName());
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
//            imageUri = data.getData();
            if (requestCode == COVER_CODE) {
                uploadToFirebase(uri, requestCode);
                cover.setImageURI(uri);
            }
            else if (requestCode == PROFILE_CODE) {
                uploadToFirebase(uri, requestCode);
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

    public void showEditPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_setting);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_setting_edit: {
                        Log.d(TAG, "here");
                        createSettingDialog();
                        break;
                    }
                    case R.id.popup_setting_reward:{
                        //TODO: intent to Reward Activity
                        break;
                    }
                    default: break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    public void createAddFriendDialog() {
        dialogBuilder = new AlertDialog.Builder(UserHomePage.this);
        View settingPopup = getLayoutInflater().inflate(R.layout.addfriend_popup, null);
        edtFriend = settingPopup.findViewById(R.id.edtFriend);
        btnFollow = settingPopup.findViewById(R.id.btnSubmitAddFriend);
        btnFollowCancel = settingPopup.findViewById(R.id.btnCancelAddFriend);
        addFriendProfile = settingPopup.findViewById(R.id.imgProfileAdd);
        tvError = settingPopup.findViewById(R.id.tvError);
        tvFollowedError = settingPopup.findViewById(R.id.tvFollowedError);
        if (currentUser.getProfileUrl() != null && currentUser.getProfileUrl().length() > 0) {
            Glide.with(UserHomePage.this).load(currentUser.getProfileUrl()).into(addFriendProfile);
        }

        dialogBuilder.setView(settingPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnFollowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.INVISIBLE);
                tvFollowedError.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend = edtFriend.getText().toString();
                if (nameSet.contains(friend)) {
                    tvError.setVisibility(View.INVISIBLE);
                    tvFollowedError.setVisibility(View.INVISIBLE);
                    ArrayList<String> temp = currentUser.getFriends();
                    boolean isExist = false;
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).equals(friend)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        tvFollowedError.setVisibility(View.VISIBLE);
                    }
                    else {
                        temp.add(friend);
                        currentUser.setFriends(temp);
                        DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(UserInfo.class.getSimpleName());
                        String userKey = currentUser.getKey();
                        rootUser.child(userKey).setValue(currentUser);
                        Toast.makeText(UserHomePage.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public void showPopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.setting_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void createSettingDialog() {
        dialogBuilder = new AlertDialog.Builder(UserHomePage.this);
        View settingPopup = getLayoutInflater().inflate(R.layout.homepage_popup, null);
        edtUserName = settingPopup.findViewById(R.id.edtUserName);
        edtBirthday = settingPopup.findViewById(R.id.edtBirthday);
        edtRegion = settingPopup.findViewById(R.id.edtRegion);
        btnSubmit = settingPopup.findViewById(R.id.btnSubmit);
        btnCancel = settingPopup.findViewById(R.id.btnCancel);
        popupProfile = settingPopup.findViewById(R.id.imgProfileAdd);
        edtUserName.setText(currentUser.getUserName());
        edtBirthday.setText(currentUser.getBirthday());
        edtRegion.setText(currentUser.getRegion());
        if (currentUser.getProfileUrl() != null && currentUser.getProfileUrl().length() > 0) {
            Glide.with(UserHomePage.this).load(currentUser.getProfileUrl()).into(popupProfile);
        }

        dialogBuilder.setView(settingPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserName = edtUserName.getText().toString();
                String newBirthday = edtBirthday.getText().toString();
                String newRegion = edtRegion.getText().toString();
                boolean valid = isValidInfo(newUserName, newBirthday, newRegion);
                if (valid) {
                    currentUserName = newUserName;
                    currentUser.setUserName(newUserName);
                    currentUser.setBirthday(newBirthday);
                    currentUser.setRegion(newRegion);
                    DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(UserInfo.class.getSimpleName());
                    String userKey = currentUser.getKey();
                    rootUser.child(userKey).setValue(currentUser);

//                    rootUser.child(userKey)
//                            .child("region")
//                            .setValue(newRegion);
                    Toast.makeText(UserHomePage.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UserHomePage.this, "invalid information", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
    private void readFormFirebase(String dbName, String refKey, String refValue) {
        DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(dbName);
        Toast.makeText(UserHomePage.this, "Read current user", Toast.LENGTH_SHORT).show();
        rootUser.orderByChild(refKey)
                .equalTo(refValue)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String userKey = childSnapshot.getKey();
                            currentUser = childSnapshot.getValue(UserInfo.class);
                            currentUser.setKey(userKey);
                            initUserInfo();
                        }
                        Toast.makeText(UserHomePage.this, currentUser.getImageUrl(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void searchHistory(String dbName, String refKey, String refValue) {

        DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(dbName);
        rootUser.orderByChild(refKey)
                .equalTo(refValue)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                            UserInfo user = childSnapshot.getValue(UserInfo.class);
                            TravelHistory history = childSnapshot.getValue(TravelHistory.class);
                            history.setKey(childSnapshot.getKey());
                            historyList.add(history);
                            Post post = new Post(history.getReview_photo_path(), UserHomePage.this, history.getPlace_id(), history.getReview_time(), history.getReview_stars());
                            postList.add(post);
//                            Post post = new Post("", UserHomePage.this, user.getUserName(), user.getBirthday(), "5.0");
//                            postList.add(post);
                            /*
                            Object obj = childSnapshot.getValue();
                            try{
                                HashMap<String, Object> userData = (HashMap<String, Object>) obj;
//                                Post post = new Post((String) userData.get("review_photo_path"), UserHomePage.this, (String) userData.get("place_id"), (String) userData.get("review_time"), (String) userData.get("review_stars"));
                                Post post = new Post("", UserHomePage.this, "gogong", "2020", "5");
                                postList.add(post);
                            }catch (ClassCastException cce){
                                Toast.makeText(UserHomePage.this, "error", Toast.LENGTH_SHORT).show();
                            }
                             */

                        }
                        createRecyclerView();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }
    private void searchUserName(String dbName) {
        DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(dbName);
        Toast.makeText(UserHomePage.this, "Start reading", Toast.LENGTH_SHORT).show();

        rootUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
//                    Toast.makeText(UserHomePage.this, "here", Toast.LENGTH_SHORT).show();
                    UserInfo user = data.getValue(UserInfo.class);
                    nameSet.add(user.getUserName());
                    Toast.makeText(UserHomePage.this, user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean isValidInfo(String userName, String birthday, String Region) {
        if (!userName.equals(currentUserName) && nameSet.contains(userName)) return false;
        if (birthday.length() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            df.setLenient(false);
            Date testDate = null;
            boolean valid = false;
            try {
                df.parse(birthday);
                valid = true;
            }
            catch (ParseException e) { } // valid will still be false
            if (!valid) return false;
        }
        Toast.makeText(UserHomePage.this, String.valueOf(birthday.length()), Toast.LENGTH_SHORT).show();
        return true;
    }

    private void uploadToFirebase(Uri uri, int code){
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference rootUser = FirebaseDatabase.getInstance().getReference(UserInfo.class.getSimpleName());
                        String userKey = currentUser.getKey();
                        if (code == COVER_CODE) {
                            currentUser.setImageUrl(uri.toString());
                            rootUser.child(userKey).setValue(currentUser);
                        }
                        else if (code == PROFILE_CODE) {
                            currentUser.setProfileUrl(uri.toString());
                            rootUser.child(userKey).setValue(currentUser);
                        }

                        Toast.makeText(UserHomePage.this, "Update Successfully", Toast.LENGTH_SHORT).show();
//                        imageView.setImageResource(R.drawable.ic_launcher_background);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UserHomePage.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
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
        adaptor = new HomePagePostAdaptor(UserHomePage.this, reference);
        adaptor.setPostList(postList);
        adaptor.setHistoryList(historyList);
        adaptor.notifyDataSetChanged();
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItem(int pos, Post newPost) {
        postList.add(pos, newPost);
        adaptor.notifyItemInserted(pos);
    }

}