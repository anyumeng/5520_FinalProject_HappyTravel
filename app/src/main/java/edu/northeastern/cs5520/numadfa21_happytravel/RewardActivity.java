package edu.northeastern.cs5520.numadfa21_happytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Reward> listReward;
    private String eat = "";
    private String play = "";
    private String total = "";
    private String currentName;


    private RewardAdapter adapter;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference rewardRef = root.child("RewardImage");
    private DatabaseReference userRef = root.child("User");
    private List<Map<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listReward = new ArrayList<>();
        this.currentName = "sara";

        rewardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reward reward = dataSnapshot.getValue(Reward.class);
                    listReward.add(reward);
                }

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);

                            if(user.getName().equals(currentName)){
                                eat = user.getPost().getEat().toString();
                                play = user.getPost().getPlay().toString();
                                total = user.getPost().getPost().toString();
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (Reward reward : listReward) {
                            if (reward.getRewardName().equals("eat")) {
                                Map<String, String> map = new HashMap<>();
                                map.put("text", eat);
                                map.put("rewardImageUrl", reward.getRewardImageUrl());
                                map.put("rewardName", reward.getRewardName());
                                map.put("rewardRequirement", reward.getRewardRequirement().toString());
                                list.add(map);
                            }
                            if (reward.getRewardName().equals("play")) {
                                Map<String, String> map = new HashMap<>();
                                map.put("text", play);
                                map.put("rewardImageUrl", reward.getRewardImageUrl());
                                map.put("rewardName", reward.getRewardName());
                                map.put("rewardRequirement", reward.getRewardRequirement().toString());
                                list.add(map);
                            }
                            if (reward.getRewardName().equals("total")) {
                                Map<String, String> map = new HashMap<>();
                                map.put("text", total);
                                map.put("rewardImageUrl", reward.getRewardImageUrl());
                                map.put("rewardName", reward.getRewardName());
                                map.put("rewardRequirement", reward.getRewardRequirement().toString());
                                list.add(map);
                            }
                        }
                        adapter = new RewardAdapter(RewardActivity.this, list);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}