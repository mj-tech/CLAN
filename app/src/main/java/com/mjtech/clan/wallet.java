package com.mjtech.clan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;

public class wallet extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        MaterialViewPager mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.fragment_wallet_overview, null);
        View v2 = mInflater.inflate(R.layout.fragment_wallet_transaction, null);

        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(v1);
        viewList.add(v2);

        mViewPager.getViewPager().setAdapter(new vpa_wallet(this, viewList));
        mViewPager.getViewPager().setCurrentItem(0);

        Toolbar toolbar = mViewPager.getToolbar();
        toolbar.setVisibility(View.GONE);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://img.sc.chinaz.com/upload/2015/09/01//2015090117075134.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://img.sc.chinaz.com/upload/2015/09/01//2015090117073165.jpg");

                }
                return null;
            }
        });
    }


}
