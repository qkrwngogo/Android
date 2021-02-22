package com.example.originaltest;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;


public class MainActivity extends AppCompatActivity {

    FragmentStatePagerAdapter adapterViewPager;
    ImageView imageView, logo;
    ImageButton homeBtn, menuBtn, searchBtn, logoutBtn;
    public static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_main);
        ViewPager container = findViewById(R.id.viewPager_container);
        adapterViewPager = new PagerAdapter((getSupportFragmentManager()));
        container.setOffscreenPageLimit(3);
        imageView = findViewById(R.id.close);
        logo = findViewById(R.id.logo);
        container.setAdapter(adapterViewPager);
        AnimatedBottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setupWithViewPager(container);
        // Home Button
        homeBtn = findViewById(R.id.home);
        menuBtn = findViewById(R.id.menu);
        searchBtn = findViewById(R.id.search);
        logoutBtn = findViewById(R.id.logout);

        homeBtn.setOnClickListener(v -> bottomBar.selectTabById(R.id.nav_routine, true));
        logo.setOnClickListener(v -> bottomBar.selectTabById(R.id.nav_routine, true));
        // Menu Button

    }

    public void updateViewPager() {
        ViewPager container = findViewById(R.id.viewPager_container);
        Objects.requireNonNull(container.getAdapter()).notifyDataSetChanged();
    }


}
