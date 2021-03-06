package edu.northeastern.cs5520.numadfa21_happytravel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Map<String, RewardRequirement> rewardRequirements;
    private String currentUserId;

    private RewardAdapter adapter;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = root.child("UserInfo");
    private DatabaseReference rewardRequirementRef = root.child("Reward");
    private List<Reward> rewards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rewardRequirements = new HashMap<>();

        // initialize based on current user's data.
        userRef.get().addOnCompleteListener(task -> {
            rewardRequirementRef.get().addOnCompleteListener(requirementTask -> {
                updateAdapter(task.getResult().child(currentUserId), requirementTask.getResult());
            });
        });

        // If there is a new reward, update current user's view.
        rewardRequirementRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userRef.get().addOnCompleteListener(task -> {
                            updateAdapter(task.getResult().child(currentUserId), snapshot);
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        // If the user's post count is changed, update current user's view.
        userRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        rewardRequirementRef.get().addOnCompleteListener(task -> {
                            rewardRequirements = new HashMap<>();
                            updateAdapter(snapshot.child(currentUserId), task.getResult());
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void updateAdapter(DataSnapshot usersSnapshot, DataSnapshot rewardRequirementSnapshot) {
        rewardRequirements = new HashMap<>();
        for (DataSnapshot rewardSnapshot : rewardRequirementSnapshot.getChildren()) {
            RewardRequirement reward = rewardSnapshot.getValue(RewardRequirement.class);
            String placeTypeId = rewardSnapshot.getKey();
            rewardRequirements.put(placeTypeId, reward);
        }
        UserInfo user = usersSnapshot.getValue(UserInfo.class);
        Map<String, Integer> posts = user.getPost();
        rewards = posts.entrySet().stream()
                       .filter(e -> rewardRequirements.containsKey(e.getKey()))
                       .map(e -> {
                           Reward reward = new Reward();
                           reward.setRewardCount(e.getValue());
                           reward.setRewardRequirement(rewardRequirements.get(e.getKey())
                                                                         .getCopy());
                           return reward;
                       }).collect(Collectors.toList());
        adapter = new RewardAdapter(RewardActivity.this, rewards);
        recyclerView.setAdapter(adapter);
    }
}
