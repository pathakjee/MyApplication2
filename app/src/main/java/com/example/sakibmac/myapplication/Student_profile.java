package com.example.sakibmac.myapplication;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Student_profile extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    Realm myreal;
    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_profile, container, false);
        myreal = Realm.getInstance(getActivity());

        String image = getArguments().getString("image");
        String id = getArguments().getString("id");
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        img = v.findViewById(R.id.imageView5);
        tabLayout = v.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        viewPager = v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        Glide.with(getActivity())
                .load(image)
                .placeholder(R.drawable.correct)
                .error(R.drawable.error)
                .into(img);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProfileFragment(), "Profile");
        // adapter.addFragment(new MarksFragment(), "Marks");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}