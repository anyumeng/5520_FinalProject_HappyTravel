package edu.northeastern.cs5520.yumengan.numadfa21_happytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserHomePage extends AppCompatActivity {
    public String TAG = "UserHomePage";
    public static final int CAMERA_REQUEST = 200, STORAGE_REQUEST = 300;
    public int REQUEST_CODE;
    private ImageView cover, profile, ic_menu;
    private FloatingActionButton fabCover, fabProfile;
    private String cameraPermission[];
    private String storagePermission[];
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText edtUserName, edtBirthday, edtRegion;
    private Button btnSubmit, btnCancel;
    private RecyclerView recyclerView;
    private HomePagePostAdaptor adaptor;
    private ArrayList<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        cover = findViewById(R.id.imgBg);
        fabCover = findViewById(R.id.fabCover);
        profile = findViewById(R.id.imgProfile);
        fabProfile = findViewById(R.id.fabProfile);
        ic_menu = findViewById(R.id.ic_menu);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        createRecyclerView();
        postList.add(0, new Post("", "Hello"));
        addItem(0,  new Post("", "World"));
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
                showPopupMenu(view);
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
            if (requestCode == 10) {
                cover.setImageURI(uri);
            }
            else if (requestCode == 20) {
                profile.setImageURI(uri);
            }
        }
        else {
            Toast.makeText(UserHomePage.this, "Please select picture to replace the cover", Toast.LENGTH_SHORT).show();
        }
    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_setting);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_setting_edit: {
                        Log.d(TAG, "here");
                        createDialog();
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

    public void createDialog() {

        dialogBuilder = new AlertDialog.Builder(UserHomePage.this);
        View settingPopup = getLayoutInflater().inflate(R.layout.homepage_popup, null);
        edtUserName = settingPopup.findViewById(R.id.edtUserName);
        edtBirthday = settingPopup.findViewById(R.id.edtBirthday);
        edtRegion = settingPopup.findViewById(R.id.edtRegion);
        btnSubmit = settingPopup.findViewById(R.id.btnSubmit);
        btnCancel = settingPopup.findViewById(R.id.btnCancel);
        dialogBuilder.setView(settingPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void createRecyclerView() {
        recyclerView = findViewById(R.id.rvPost);
        recyclerView.setHasFixedSize(true);
        adaptor = new HomePagePostAdaptor(UserHomePage.this);
        adaptor.setPostList(postList);
        adaptor.notifyDataSetChanged();
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItem(int pos, Post newPost) {
        postList.add(pos, newPost);
        adaptor.notifyItemInserted(pos);
    }

}