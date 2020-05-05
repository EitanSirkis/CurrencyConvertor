package com.example.currencyconvertor;

import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.example.currencyconvertor.Adapters.ScreenSlidePagerAdapter;

public class MainActivity extends AppCompatActivity
{
    public static final String  Shared_Preferences = "Currencies App";
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

         viewPager = findViewById(R.id.viewPager);
         tabLayout = findViewById(R.id.tabLayout);

        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        if (savedInstanceState!=null)
        {
            viewPager.setCurrentItem(savedInstanceState.getInt("ACTIVE_TAB"));
        }
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        ViewGroup vg =(ViewGroup) tabLayout.getChildAt(0);
        ViewGroup vgb = (ViewGroup) vg.getChildAt(1);
        Log.e("123321123","" + vgb.getChildCount());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("ACTIVE_TAB",viewPager.getCurrentItem());


    }


}
