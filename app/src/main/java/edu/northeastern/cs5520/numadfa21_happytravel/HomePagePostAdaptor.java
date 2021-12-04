package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.cs5520.yumengan.numadfa21_happytravel.R;

public class HomePagePostAdaptor extends RecyclerView.Adapter<HomePagePostHolder> {
    private Context context;
    private ArrayList<Post> postList;

    public HomePagePostAdaptor(Context context) {
        this.context = context;
        postList = new ArrayList<>();
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }
    @NonNull
    @Override
    public HomePagePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homepage_post_layout, parent, false);
        HomePagePostHolder homePagePostHolder = new HomePagePostHolder(view, context);
        return homePagePostHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomePagePostHolder holder, int position) {
        Post post = postList.get(position);
        holder.textView.setText(post.getContext());
        holder.imageView.setImageResource(R.mipmap.defaultphoto);
    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }
}
