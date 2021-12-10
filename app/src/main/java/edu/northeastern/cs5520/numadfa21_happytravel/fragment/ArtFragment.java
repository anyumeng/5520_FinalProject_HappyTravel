package edu.northeastern.cs5520.numadfa21_happytravel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.TravelHistory;
import edu.northeastern.cs5520.numadfa21_happytravel.recommendationadapter.ArtAdapter;
import edu.northeastern.cs5520.numadfa21_happytravel.bean.CommonBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArtFragment extends Fragment {

    public RecyclerView recyclerView;
    public ArtAdapter artAdapter;

    public ArtFragment() {
        // Required empty public constructor
    }

    public static ArtFragment newInstance(List<TravelHistory> mapList) {
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
        List<TravelHistory> list = new ArrayList<>();
        list = (List<TravelHistory>) bundle.getSerializable("mapList");

        //calculate average for the same location
        Map<String, List<TravelHistory>> map = groupList(list);
        List<TravelHistory> newArrayList = new ArrayList<>();
        map.forEach((x, y) -> {
            List<TravelHistory> travelHistoryList = y;
            //put data in a list
            Double d = 0.0;
            for (int i = 0; i < travelHistoryList.size(); i++) {
                d = Double.parseDouble(travelHistoryList.get(i).getReview_stars()) + d;
            }
            TravelHistory travelHistory = new TravelHistory();
            travelHistory.setReview_stars(String.valueOf(d / travelHistoryList.size()));
            travelHistory.setPlace_name(travelHistoryList.get(0).getPlace_name());
            travelHistory.setReview_photo_path(travelHistoryList.get(0).getReview_photo_path());
            newArrayList.add(travelHistory);
        });

        Collections.sort(newArrayList, new Comparator<TravelHistory>() {
            public int compare(TravelHistory o1,
                               TravelHistory o2) {
                Double d1 = Double.valueOf(o1.getReview_stars());
                Double d2 = Double.valueOf(o2.getReview_stars());
                return d1.compareTo(d2);
            }
        });
        Collections.reverse(newArrayList);

        //linearLayout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        artAdapter = new ArtAdapter(getActivity(), newArrayList);
        recyclerView.setAdapter(artAdapter);

        return inflate;
    }


    public Map<String, List<TravelHistory>> groupList(List<TravelHistory> travelHistoryList) {
        Map<String, List<TravelHistory>> map = travelHistoryList.stream().collect(Collectors.groupingBy(TravelHistory::getPlace_name));
        return map;
    }
}