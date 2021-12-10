package edu.northeastern.cs5520.numadfa21_happytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.northeastern.cs5520.numadfa21_happytravel.bean.CommonBean;
import edu.northeastern.cs5520.numadfa21_happytravel.fragment.ArtFragment;
import edu.northeastern.cs5520.numadfa21_happytravel.fragment.EatFragment;
import edu.northeastern.cs5520.numadfa21_happytravel.fragment.NatureFragment;
import edu.northeastern.cs5520.numadfa21_happytravel.fragment.OtherFragment;
import edu.northeastern.cs5520.numadfa21_happytravel.fragment.SportFragment;
import edu.northeastern.cs5520.numadfa21_happytravel.fragment.TotalFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationActivity extends AppCompatActivity {

    TabLayout tabLayout;

    private static final int art = 0;

    private static final int eat = 1;

    private static final int nature = 2;

    private static final int other = 3;

    private static final int sport = 4;

    private static final int total= 5;

    private ArtFragment artFragment;
    private EatFragment eatFragment;
    private NatureFragment natureFragment;
    private OtherFragment otherFragment;
    private SportFragment sportFragment;
    private TotalFragment totalFragment;

    private List<TravelHistory> list = new ArrayList<>();
    //art
    private List<TravelHistory> artList = new ArrayList<>();
    //eat
    private List<TravelHistory> eatList = new ArrayList<>();
    //nature
    private List<TravelHistory> natureList = new ArrayList<>();

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("TravelHistory");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        //Combine the background and the System UI
        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
    }


    private void initView() {
        tabLayout = findViewById(R.id.tbSelect);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        //TODO: Add data
        root.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TravelHistory travelHistory = dataSnapshot.getValue(TravelHistory.class);
                    TravelHistory history = new TravelHistory();
                    history.setPlace_name(travelHistory.getPlace_name());
                    history.setType(travelHistory.getType());
                    history.setReview_stars(travelHistory.getReview_stars());
                    history.setReview_photo_path(travelHistory.getReview_photo_path());
                    list.add(history);
                }

                for (TravelHistory travelHistory : list) {
                    //Judge the types
                    if (travelHistory.getType().contains("Art")){
                        artList.add(travelHistory);
                    }
                    if (travelHistory.getType().contains("Eat")){
                        eatList.add(travelHistory);
                    }
                    if (travelHistory.getType().contains("Nature")){
                        natureList.add(travelHistory);
                    }

                }

                initFragment(0);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }

    private final TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            initFragment(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    /**
     * Initialization fragment
     */
    private void initFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        //Hide fragment
        hideFragment(transaction);
        if (art == position) {
            if (artFragment == null) {
                artFragment = ArtFragment.newInstance(artList);
                transaction.add(R.id.frgContainerView, artFragment);
            } else {
                transaction.show(artFragment);
            }
        } else if (eat == position) {
            if (eatFragment == null) {
                eatFragment = EatFragment.newInstance(eatList);
                transaction.add(R.id.frgContainerView, eatFragment);
            } else {
                transaction.show(eatFragment);
            }

        } else if (nature == position) {
            if (natureFragment == null) {
                natureFragment = NatureFragment.newInstance(natureList);
                transaction.add(R.id.frgContainerView, natureFragment);
            } else {
                transaction.show(natureFragment);
            }
        } else if (other == position) {
            if (otherFragment == null) {
                otherFragment = OtherFragment.newInstance();
                transaction.add(R.id.frgContainerView, otherFragment);
            } else {
                transaction.show(otherFragment);
            }
        } else if (sport == position) {
            if (sportFragment == null) {
                sportFragment = SportFragment.newInstance();
                transaction.add(R.id.frgContainerView, sportFragment);
            } else {
                transaction.show(sportFragment);
            }
        } else if (total == position) {
            if (totalFragment == null) {
                totalFragment = TotalFragment.newInstance();
                transaction.add(R.id.frgContainerView, totalFragment);
            } else {
                transaction.show(totalFragment);
            }
        }
        transaction.commit();
    }

    /**
     * Hide unused fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (artFragment != null) {
            transaction.hide(artFragment);
        }
        if (eatFragment != null) {
            transaction.hide(eatFragment);
        }
        if (natureFragment != null) {
            transaction.hide(natureFragment);
        }
        if (otherFragment != null) {
            transaction.hide(otherFragment);
        }
        if (sportFragment != null) {
            transaction.hide(sportFragment);
        }
        if (totalFragment != null) {
            transaction.hide(totalFragment);
        }
    }
}