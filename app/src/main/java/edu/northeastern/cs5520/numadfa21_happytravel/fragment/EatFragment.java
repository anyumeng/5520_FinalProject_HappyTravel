package edu.northeastern.cs5520.numadfa21_happytravel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.TravelHistory;
import edu.northeastern.cs5520.numadfa21_happytravel.recommendationadapter.EatAdapter;
import edu.northeastern.cs5520.numadfa21_happytravel.bean.CommonBean;

public class EatFragment extends Fragment{

    public RecyclerView recyclerView;
    public EatAdapter artAdapter;
    
    public EatFragment() {
        // Required empty public constructor
    }

    public static EatFragment newInstance(List<TravelHistory> mapList) {
        EatFragment fragment = new EatFragment();
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
        View inflate = inflater.inflate(R.layout.fragment_eat, container, false);
        recyclerView = inflate.findViewById(R.id.recyclerView);
        Bundle bundle = getArguments();
        List<TravelHistory> list = new ArrayList<>();
        list = (List<TravelHistory>) bundle.getSerializable("mapList");

        //对形同地点进行秋分均分
        Map<String, List<TravelHistory>> map = groupList(list);
        List<TravelHistory> newArrayList = new ArrayList<>();
        map.forEach((x, y) -> {
            List<TravelHistory> travelHistoryList = y;
            //对相同名字的数据就行求平均分，并放入一个list中
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
        artAdapter = new EatAdapter(getActivity(), newArrayList);
        recyclerView.setAdapter(artAdapter);

        return inflate;
    }


    public Map<String, List<TravelHistory>> groupList(List<TravelHistory> travelHistoryList) {
        Map<String, List<TravelHistory>> map = travelHistoryList.stream().collect(Collectors.groupingBy(TravelHistory::getPlace_name));
        return map;
    }

}