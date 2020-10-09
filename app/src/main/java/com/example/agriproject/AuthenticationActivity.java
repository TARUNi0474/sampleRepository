package com.example.agriproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class AuthenticationActivity extends AppCompatActivity {
    TabLayout tl;
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        tl = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);
        vp.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tl.setupWithViewPager(vp);


    }
    public class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position==0){
                return new LoginFragment();
            }
            if (position==1){
                return new RegisterFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0){
                return "Login";
            }
            if (position==1){
                return "Register";
            }
            return super.getPageTitle(position);
        }
    }

}