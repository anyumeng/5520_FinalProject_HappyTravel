package edu.northeastern.cs5520.numadfa21_happytravel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.icu.util.Measure;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import edu.northeastern.cs5520.numadfa21_happytravel.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main_search";

    private TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textInputLayout = findViewById(R.id.search_input_layout);
        this.setupSearchBar();
    }

    private void setupSearchBar() {
        this.textInputLayout.setEndIconOnClickListener(view -> {
            // Log.v(TAG, "search bar!");
            Toast.makeText(this, "Searched!", Toast.LENGTH_LONG).show();
        });
    }

    public void showPopup(View view) {
        View showView = LayoutInflater.from(this).inflate(R.layout.main_popup, null);
        ImageButton popupButton = findViewById(R.id.main_menu_button);

        PopupWindow popupWindow = new PopupWindow(showView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setContentView(showView);
        showView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        popupWindow.showAsDropDown(popupButton, 0,
                -popupButton.getHeight() - showView.getMeasuredHeight());
    }
}