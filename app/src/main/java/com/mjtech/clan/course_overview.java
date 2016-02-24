package com.mjtech.clan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;

public class course_overview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_overview);

        MaterialViewPager mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.fragment_course_announcement, null);
        View v2 = mInflater.inflate(R.layout.fragment_course_announcement, null);
        View v3 = mInflater.inflate(R.layout.fragment_course_announcement, null);
        View v4 = mInflater.inflate(R.layout.fragment_course_announcement, null);

        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);
        viewList.add(v4);

        mViewPager.getViewPager().setAdapter(new vpa_course(this, viewList, ((CLAN) getApplication()).TOKEN, getIntent().getStringExtra("ID")));
        mViewPager.getViewPager().setCurrentItem(0);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnJ2F6y3hTArCyLk3XlsmxniWffXSJVNZmKSPGZy_XB5U4y8FH");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS-eryu8mPuRlGRYt5AdLIekPjy3KTOgQHzEsU-dk7r2nyXbOc0");

                }
                return null;
            }
        });
    }


}

