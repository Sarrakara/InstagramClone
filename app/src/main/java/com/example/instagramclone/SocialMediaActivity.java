package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    private String[] titles = {"Profile", "Users", "Share Picture"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media App");

        toolbar = findViewById(R.id.myToolbar);
        // set this toolbar as the action bar of our social media activity
        setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.viewPager2);

        tabLayout = findViewById(R.id.tabLayout);

        tabAdapter = new TabAdapter(this);
        // to switch between tabs
        viewPager2.setAdapter(tabAdapter);
        // show the titles of our fragments
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position]))
                .attach();








    }
}