package com.mjtech.clan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
        View v2 = mInflater.inflate(R.layout.fragment_course_assignment, null);

        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(v1);
        viewList.add(v2);

        mViewPager.getViewPager().setAdapter(new vpa_course(this, viewList, ((CLAN) getApplication()).TOKEN, getIntent().getStringExtra("ID")));
        mViewPager.getViewPager().setCurrentItem(0);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        Toolbar toolbar = mViewPager.getToolbar();
        toolbar.setVisibility(View.GONE);

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://img.sc.chinaz.com/upload/2015/09/01//2015090117082937.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://img.sc.chinaz.com/upload/2015/09/01//2015090117082214.jpg");

                }
                return null;
            }
        });
    }


}

