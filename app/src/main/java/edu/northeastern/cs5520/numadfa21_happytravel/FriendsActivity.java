package edu.northeastern.cs5520.numadfa21_happytravel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private int[] images = {R.drawable.pic1, R.drawable.pic2};
    private String[] names = {"Li Ming", "Zhao Qiang"};
    private String[] places = {"Mission Peak", "Tahoe Lake"};
    private int[] review_stars = {2, 3};
    private String[] review_contents = {"Good", "Great!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView = findViewById(R.id.recyclerViewFriends);
        FriendAdpater friendAdapter =
                new FriendAdpater(this, images, names, places, review_stars, review_contents);
        recyclerView.setAdapter(friendAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
