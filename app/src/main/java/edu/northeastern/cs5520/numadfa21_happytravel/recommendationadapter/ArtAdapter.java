package edu.northeastern.cs5520.numadfa21_happytravel.recommendationadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.bean.CommonBean;

import java.util.ArrayList;
import java.util.List;


public class ArtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    List<CommonBean> datas = new ArrayList<>();

    public ArtAdapter(Context context, List<CommonBean> datas) {
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
        CommonBean commonBean = datas.get(position);
        normalHolder.tv1.setText(commonBean.getCode());
        normalHolder.tv2.setText(commonBean.getText());
        normalHolder.tv3.setText(commonBean.getTest());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        public TextView tv1;
        public TextView tv2;
        public TextView tv3;

        public NormalHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
        }
    }
}