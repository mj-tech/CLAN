package com.mjtech.clan;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class attendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        new loadRecord().execute(getIntent().getStringExtra("CRN"));
    }

    private class loadRecord extends AsyncTask<String, Void, Void> {
        ArrayList<HashMap<String, String>> Map = new ArrayList<>();

        protected Void doInBackground(String... param) {
            try {
                URL url = new URL("https://mjtech.cf/api/attendance/view.php");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("session", ((CLAN) getApplication()).SESSION)
                                                       .appendQueryParameter("crn", param[0]);

                String query = builder.build().getEncodedQuery();

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                JSONArray obj = new JSONObject(readStream(con.getInputStream())).getJSONArray("attendance");
                for(int i = 0; i < obj.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();

                    SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = fdate.parse(obj.getJSONObject(i).getString("time"));
                    map.put("DATE", new SimpleDateFormat("yyyy-MM-dd").format(date));
                    map.put("TIME", new SimpleDateFormat("HH:mm").format(date));

                    Map.add(map);

                }

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voided) {
            ((ListView)findViewById(R.id.attendanceRecordList)).setAdapter(new SimpleAdapter(attendance.this, Map, R.layout.row_course, new String[]{"DATE","TIME"}, new int[]{R.id.courseCode,R.id.courseName}));
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
}
