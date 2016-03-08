package com.mjtech.clan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class vpa_course_list extends PagerAdapter {
    ArrayList<HashMap<String, String>> courseMap = new ArrayList<>();
    ArrayList<HashMap<String, String>> attendanceMap = new ArrayList<>();
    private List<View> views;
    private Context context;
    public vpa_course_list(Context context, List<View> views) {
        this.context = context;
        this.views = views;

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
                String token = ((CLAN) ((Activity)context).getApplication()).TOKEN;
                try {
                    URL url = new URL("https://canvas.cityu.edu.hk/api/v1/courses?access_token="+token);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    JSONArray courses = new JSONArray(readStream(con.getInputStream()));
                    for(int i = 0; i < courses.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();

                        map.put("ID", courses.getJSONObject(i).getString("id"));
                        map.put("CODE", courses.getJSONObject(i).getString("course_code"));
                        map.put("NAME", courses.getJSONObject(i).getString("name"));

                        courseMap.add(map);

                    }
                    ((ListView)TView.findViewById(R.id.courseList)).setAdapter(new SimpleAdapter(context, courseMap, R.layout.row_course, new String[]{"CODE","NAME"}, new int[]{R.id.courseCode,R.id.courseName}));

                    ((ListView)TView.findViewById(R.id.courseList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                            Intent intent = new Intent(view.getContext(), course_overview.class);
                            intent.putExtra("ID",courseMap.get(position).get("ID"));
                            context.startActivity(intent);

                        }
                    });
                } catch (Exception ignored) {
                }
            } else if(position==1) {
                try {
                    URL url = new URL("https://mjtech.cf/api/course/listlesson.php");
                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                    con.setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("session", ((CLAN) ((Activity) context).getApplication()).SESSION);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    JSONArray courses = new JSONObject(readStream(con.getInputStream())).getJSONArray("courses");
                    for(int i = 0; i < courses.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();

                        map.put("CRN", courses.getJSONObject(i).getString("crn"));
                        map.put("CODESESSION", courses.getJSONObject(i).getString("course")+" "+courses.getJSONObject(i).getString("session")+"   | "+courses.getJSONObject(i).getString("day")+" "+ courses.getJSONObject(i).getString("time"));
                        map.put("NAME", courses.getJSONObject(i).getString("name"));

                        attendanceMap.add(map);

                    }
                    ((ListView)TView.findViewById(R.id.attendanceList)).setAdapter(new SimpleAdapter(context, attendanceMap, R.layout.row_course, new String[]{"CODESESSION","NAME"}, new int[]{R.id.courseCode,R.id.courseName}));

                    ((ListView)TView.findViewById(R.id.attendanceList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                            Intent intent = new Intent(view.getContext(), attendance.class);
                            intent.putExtra("CRN",attendanceMap.get(position).get("CRN"));
                            context.startActivity(intent);

                        }
                    });
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
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
        switch (position % 2) {
            case 0:
                return "Courses";
            case 1:
                return "Attendance";
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

