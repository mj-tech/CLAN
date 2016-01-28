package com.mjtech.clan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.List;

public class course_overview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_overview);

        MaterialViewPager mViewPager = (MaterialViewPager)findViewById(R.id.materialViewPager);

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

        mViewPager.getViewPager().setAdapter(new ViewPagerAdapter(viewList));
        mViewPager.getViewPager().setCurrentItem(0);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
    }

}

class ViewPagerAdapter extends PagerAdapter {
    /*
    理解：PagerAdapter
    一，调用 getCount() 获取需要初始化的 ViewGroup 数量
    二，调用 instantiateItem() 实例化页卡，按顺序
    三，调用 destroyItem() 销毁，按顺序
     */
    // 所有 View
    private List<View> views;
    // 构造
    public ViewPagerAdapter(List<View> views) {
        this.views = views;
    }
    // 销毁时被调用
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // ViewGroup 所有的View
        // position 位置,第几个
        // 销毁时删除 View
        container.removeView(views.get(position));
        // super.destroyItem(container, position, object);
    }
    // 实例化页卡
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 添加 View
        container.addView(views.get(position));
        // 返回添加的 View 对象
        return views.get(position);
        // return super.instantiateItem(container, position);
    }
    // 所包含的 Item 总个数
    @Override
    public int getCount() {
        // 返回 views 总数
        return views.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == o);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position % 4) {
            case 0:
                return "Selection";
            case 1:
                return "Actualités";
            case 2:
                return "Professionnel";
            case 3:
                return "Divertissement";
        }
        return "";
    }
}