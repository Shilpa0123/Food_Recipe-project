package com.foodrecipe.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.foodrecipe.R;
import com.foodrecipe.fragments.bookmarkFragment;
import com.foodrecipe.fragments.historyFragment;
import com.foodrecipe.fragments.homeFragment;
import com.foodrecipe.fragments.profileFragment;

import java.util.ArrayList;
import java.util.List;

import static com.foodrecipe.Utils.guest;

public class baseActivity extends AppCompatActivity implements View.OnClickListener {
    //    List<Integer> layoutID = new ArrayList<>();
    View footer;
    private final List<Fragment> mList = new ArrayList<>();
    AppCompatImageView home, bookmark, history, profile;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        layoutID.add(R.layout.home_fragment);
//        layoutID.add(R.layout.profile_fragment);
        viewPager = findViewById(R.id.viewpager);
        viewpagerAdapter adapter = new viewpagerAdapter(getSupportFragmentManager());

        home = findViewById(R.id.home);
        bookmark = findViewById(R.id.bookmark);
        history = findViewById(R.id.history);
        profile = findViewById(R.id.profile);
        footer = findViewById(R.id.footer);

        home.setSelected(true);

        home.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        history.setOnClickListener(this);
        profile.setOnClickListener(this);

        if (guest == true) {
            footer.setVisibility(View.GONE);
            mList.add(new homeFragment());
        } else {
            mList.add(new homeFragment());
            mList.add(new bookmarkFragment());
            mList.add(new historyFragment());
            mList.add(new profileFragment());
        }

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        home.setSelected(true);
                        bookmark.setSelected(false);
                        history.setSelected(false);
                        profile.setSelected(false);
                        break;
                    case 1:
                        home.setSelected(false);
                        bookmark.setSelected(true);
                        history.setSelected(false);
                        profile.setSelected(false);
                        break;
                    case 2:
                        home.setSelected(false);
                        bookmark.setSelected(false);
                        history.setSelected(true);
                        profile.setSelected(false);
                        break;
                    case 3:
                        home.setSelected(false);
                        bookmark.setSelected(false);
                        history.setSelected(false);
                        profile.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.bookmark:
                viewPager.setCurrentItem(1);
                break;
            case R.id.history:
                viewPager.setCurrentItem(2);
                break;
            case R.id.profile:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    private class viewpagerAdapter extends FragmentPagerAdapter {

        public viewpagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

    }
}
