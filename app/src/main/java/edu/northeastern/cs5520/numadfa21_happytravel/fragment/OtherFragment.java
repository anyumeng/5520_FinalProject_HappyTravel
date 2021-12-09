package edu.northeastern.cs5520.numadfa21_happytravel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import edu.northeastern.cs5520.numadfa21_happytravel.R;
import edu.northeastern.cs5520.numadfa21_happytravel.recommendationadapter.ArtAdapter;
import edu.northeastern.cs5520.numadfa21_happytravel.bean.CommonBean;

public class OtherFragment extends Fragment{

    public OtherFragment() {
        // Required empty public constructor
    }

    public static OtherFragment newInstance() {
        OtherFragment fragment = new OtherFragment();
        Bundle args = new Bundle();
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
        View inflate = inflater.inflate(R.layout.fragment_other, container, false);
        return inflate;
    }

}