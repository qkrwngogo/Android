package com.example.originaltest;

import android.app.Dialog;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import nl.joery.animatedbottombar.AnimatedBottomBar;


public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    ImageView imageView;
    ImageButton homeBtn, menuBtn, searchBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager container = findViewById(R.id.viewPager_container);
        adapterViewPager = new PagerAdapter((getSupportFragmentManager()));

        imageView = findViewById(R.id.close);
        container.setAdapter(adapterViewPager);
        AnimatedBottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setupWithViewPager(container);
        // Home Button
        homeBtn = findViewById(R.id.home);
        menuBtn = findViewById(R.id.menu);
        searchBtn = findViewById(R.id.search);
        homeBtn.setOnClickListener(v -> bottomBar.selectTabById(R.id.nav_routine, true));
        // Menu Button



    }


}
