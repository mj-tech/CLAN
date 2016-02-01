package com.mjtech.clan;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class ViewPagerAdapter extends PagerAdapter {
    ArrayList<HashMap<String, String>> Map = new ArrayList<>();
    private List<View> views;
    private String token, cid;
    private Context context;
    public ViewPagerAdapter(Context context, List<View> views, String token, String cid) {
        this.context = context;
        this.views = views;
        this.token = token;
        this.cid = cid;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View TView = views.get(position);
        try {
            if(position==0) {
                URL url = new URL("https://canvas.cityu.edu.hk/api/v1/courses/"+cid+"/discussion_topics?only_announcements=true&access_token="+token);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                JSONArray courses = new JSONArray(readStream(con.getInputStream()));
                for(int i = 0; i < courses.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("TITLE", courses.getJSONObject(i).getString("title"));
                    SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    fdate.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = fdate.parse(courses.getJSONObject(i).getString("posted_at"));
                    map.put("DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                    map.put("AUTHOR", courses.getJSONObject(i).getString("user_name"));
                    map.put("MESSAGE", Html.fromHtml(courses.getJSONObject(i).getString("message")).toString());

                    Map.add(map);

                }
                ((ListView)TView.findViewById(R.id.announcementList)).setAdapter(new SimpleAdapter(TView.getContext(), Map, R.layout.row_announcement, new String[]{"TITLE", "DATE"}, new int[]{R.id.title, R.id.msg}));
                ((ListView)TView.findViewById(R.id.announcementList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                        Intent intent = new Intent(context, course_announcement.class);
                        intent.putExtra("TITLE", Map.get(position).get("TITLE"));
                        intent.putExtra("DATE", Map.get(position).get("DATE"));
                        intent.putExtra("AUTHOR", Map.get(position).get("AUTHOR"));
                        intent.putExtra("MESSAGE", Map.get(position).get("MESSAGE"));
                        context.startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(TView);
        return TView;
    }

    @Override
    public int getCount() {
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
                return "Announcement";
            case 1:
                return "Actualités";
            case 2:
                return "Professionnel";
            case 3:
                return "Divertissement";
        }
        return "";
    }

    public String readStream(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}

