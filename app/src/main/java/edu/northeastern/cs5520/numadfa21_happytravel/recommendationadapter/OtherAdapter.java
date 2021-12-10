package edu.northeastern.cs5520.numadfa21_happytravel.recommendationadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.TravelHistory;


public class OtherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TravelHistory> datas = new ArrayList<>();

    public OtherAdapter(Context context, List<TravelHistory> datas) {
        mContext = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_art, parent, false);
        return new NormalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        TravelHistory commonBean = datas.get(position);
        normalHolder.tv1.setText(String.valueOf(position+1));
        normalHolder.tv2.setText(commonBean.getPlace_name());
        normalHolder.tv3.setText(commonBean.getReview_stars().substring(0,3));

        if (!TextUtils.isEmpty(commonBean.getReview_photo_path())){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(commonBean.getReview_photo_path());
            Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(normalHolder.image);
        }


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public ImageView image;

        public NormalHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            image = itemView.findViewById(R.id.image);
        }
    }
}
