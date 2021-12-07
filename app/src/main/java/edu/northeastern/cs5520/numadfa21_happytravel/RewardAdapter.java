package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private List<Reward> rewards;
    private Context context;

    public RewardAdapter(Context context, List<Reward> rewards) {

        this.context = context;
        this.rewards = rewards;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reward_row, parent, false);
        return new RewardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward reward = rewards.get(position);
        Glide.with(context)
                .load(reward.getRewardRequirement().getRewardImageUrl())
                .into(holder.imageView);
        holder.nameTextview.setText(reward.getRewardRequirement().getRewardName());
        int currentCount = reward.getRewardCount();
        int requirement = reward.getRewardRequirement().getRewardRequirement();
        holder.tvPercent.setText(String.format("%d/%d", currentCount, requirement));
        double percentage = currentCount * 100.0 / requirement;
        Log.v("requirement", reward.getRewardRequirement().toString());
        holder.progressBar.setProgress((int) percentage);
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextview;
        TextView tvPercent;
        ProgressBar progressBar;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            nameTextview = itemView.findViewById(R.id.rewardName);
            tvPercent = itemView.findViewById(R.id.tvPercent);
            progressBar = itemView.findViewById(R.id.progressBar2);
        }
    }
}
