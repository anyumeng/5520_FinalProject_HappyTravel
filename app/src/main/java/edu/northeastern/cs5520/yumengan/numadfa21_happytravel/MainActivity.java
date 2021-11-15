package edu.northeastern.cs5520.yumengan.numadfa21_happytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

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
}