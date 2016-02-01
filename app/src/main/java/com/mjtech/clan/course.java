package com.mjtech.clan;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class course extends AppCompatActivity {

    ArrayList<HashMap<String, String>> courseMap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_course);

        String token = ((CLAN)this.getApplication()).TOKEN;
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
            ((ListView)findViewById(R.id.courseList)).setAdapter(new SimpleAdapter(this, courseMap, R.layout.row_course, new String[]{"CODE","NAME"}, new int[]{R.id.courseCode,R.id.courseName}));

            ((ListView)findViewById(R.id.courseList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                    Intent intent = new Intent(view.getContext(), course_overview.class);
                    intent.putExtra("ID",courseMap.get(position).get("ID"));
                    startActivity(intent);

                }
            });
        } catch (Exception ignored) {
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
