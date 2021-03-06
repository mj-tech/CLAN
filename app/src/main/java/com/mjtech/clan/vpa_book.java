package com.mjtech.clan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

public class vpa_book extends PagerAdapter {
    ArrayList<HashMap<String, String>> Map = new ArrayList<>();
    private List<View> views;
    private Context context;
    public vpa_book(Context context, List<View> views) {
        this.context = context;
        this.views = views;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View TView = views.get(position);
        try {
            if(position==0) {
                TView.findViewById(R.id.borrow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) context);
                        scanIntegrator.initiateScan();

                    }
                });
                URL url = new URL("https://mjtech.cf/api/book/borrowed.php");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("session", ((CLAN) ((Activity)context).getApplication()).SESSION);
                String query = builder.build().getEncodedQuery();

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                JSONObject obj = new JSONObject(readStream(con.getInputStream()));
                JSONArray books = obj.getJSONArray("borrowed");
                for(int i = 0; i < books.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("ID", books.getJSONObject(i).getString("id"));
                    map.put("TITLE", books.getJSONObject(i).getString("name"));
                    SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = fdate.parse(books.getJSONObject(i).getString("expire"));
                    map.put("EXPIRE", "Expire at "+new SimpleDateFormat("yyyy-MM-dd").format(date));

                    Map.add(map);
                }
                ((ListView)TView.findViewById(R.id.bookList)).setAdapter(new SimpleAdapter(TView.getContext(), Map, R.layout.row_announcement, new String[]{"TITLE", "EXPIRE"}, new int[]{R.id.title, R.id.msg}));
                ((ListView)TView.findViewById(R.id.bookList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                        Intent intent = new Intent(context, book_details.class);
                        intent.putExtra("ID", Map.get(position).get("ID"));
                        intent.putExtra("TITLE", Map.get(position).get("TITLE"));
                        intent.putExtra("EXPIRE", Map.get(position).get("EXPIRE"));
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
        return "My Books";
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

