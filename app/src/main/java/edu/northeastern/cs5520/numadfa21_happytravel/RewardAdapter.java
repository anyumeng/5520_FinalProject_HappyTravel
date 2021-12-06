package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private List<Map<String, String>> mapList;
    private Context context;

    public RewardAdapter(Context context, List<Map<String, String>> mapList) {

        this.context = context;
        this.mapList = mapList;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reward_row, parent, false);
        return new RewardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Map<String, String> map = mapList.get(position);
        Glide.with(context).load(map.get("rewardImageUrl")).into(holder.imageView);
        holder.nameTextview.setText(map.get("rewardName"));
        String a = map.get("text");
        String b = map.get("rewardRequirement");
        holder.tvPercent.setText(a + "/" + b);
        Double c = (Double.parseDouble(a) / Double.parseDouble(b)) * 100;
        String d = c.toString().substring(0, 2);
        holder.progressBar.setProgress(Integer.parseInt(d));
    }

    @Override
    public int getItemCount() {
        return mapList.size();
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