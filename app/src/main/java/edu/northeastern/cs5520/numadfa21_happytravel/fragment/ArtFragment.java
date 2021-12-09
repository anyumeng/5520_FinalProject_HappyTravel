package edu.northeastern.cs5520.numadfa21_happytravel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.recommendationadapter.ArtAdapter;
import edu.northeastern.cs5520.numadfa21_happytravel.bean.CommonBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArtFragment extends Fragment{

    public RecyclerView recyclerView;
    public ArtAdapter artAdapter;

    public ArtFragment() {
        // Required empty public constructor
    }

    public static ArtFragment newInstance(List<CommonBean> mapList) {
        ArtFragment fragment = new ArtFragment();
        Bundle args = new Bundle();
        args.putSerializable("mapList", (Serializable) mapList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_art, container, false);
        recyclerView = inflate.findViewById(R.id.recyclerView);
        Bundle bundle = getArguments();
        List<CommonBean> list = new ArrayList<>();
        list = (List<CommonBean>) bundle.getSerializable("mapList");

        //linearLayout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        artAdapter = new ArtAdapter(getActivity(), list);
        recyclerView.setAdapter(artAdapter);

        return inflate;
    }

}