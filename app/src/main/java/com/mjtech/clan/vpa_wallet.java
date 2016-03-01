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
import android.widget.TextView;

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

public class vpa_wallet extends PagerAdapter {
    ArrayList<HashMap<String, String>> Map = new ArrayList<>();
    private List<View> views;
    private Context context;
    public vpa_wallet(Context context, List<View> views) {
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
                URL url = new URL("https://mjtech.cf/api/wallet/view.php");
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

                ((TextView)TView.findViewById(R.id.bal)).setText("$"+obj.getString("balance"));
            }
            if(position==1) {
                URL url = new URL("https://mjtech.cf/api/wallet/transaction.php");
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
                JSONArray rec = obj.getJSONArray("record");
                for(int i = 0; i < rec.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("SHOP", rec.getJSONObject(i).getString("shop"));
                    SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    fdate.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = fdate.parse(rec.getJSONObject(i).getString("time"));
                    map.put("MSG", "$"+rec.getJSONObject(i).getString("price")+" | At "+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));

                    Map.add(map);
                }
                ((ListView)TView.findViewById(R.id.transactionList)).setAdapter(new SimpleAdapter(TView.getContext(), Map, R.layout.row_announcement, new String[]{"SHOP", "MSG"}, new int[]{R.id.title, R.id.msg}));
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
        switch (position % 3) {
            case 0:
                return "Ovewview";
            case 1:
                return "Transactions";
            case 2:
                return "e-Shop";
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

