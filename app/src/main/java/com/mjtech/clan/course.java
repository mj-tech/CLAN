package com.mjtech.clan;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class course extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_course);

        String token = "1839~QImhwV3SaWPXxEEoZ2rkemDYLip1nz5TYDrRXdnuqDLO2EX3WVm4rqT2VVLsAgnW";
        try {
            ArrayList<HashMap<String, String>> courseMap = new ArrayList<>();
            URL url = new URL("https://canvas.cityu.edu.hk/api/v1/courses?access_token="+token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            JSONArray courses = new JSONArray(readStream(con.getInputStream()));
            for(int i = 0; i < courses.length(); i++) {
                HashMap<String, String> map = new HashMap<>();

                map.put("CODE", courses.getJSONObject(i).getString("course_code"));
                map.put("NAME", courses.getJSONObject(i).getString("name"));

                courseMap.add(map);
            }
            ((ListView)findViewById(R.id.courseList)).setAdapter(new SimpleAdapter(this, courseMap, R.layout.row_course, new String[]{"CODE","NAME"}, new int[]{R.id.courseCode,R.id.courseName}));
        } catch (Exception e) {
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
