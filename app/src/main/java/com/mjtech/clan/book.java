package com.mjtech.clan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class book extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refresh(0);
    }

    private void refresh(int at) {
        MaterialViewPager mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v = mInflater.inflate(R.layout.fragment_book_borrowed, null);

        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(v);

        mViewPager.getViewPager().setAdapter(new vpa_book(this, viewList));
        mViewPager.getViewPager().setCurrentItem(at);

        Toolbar toolbar = mViewPager.getToolbar();
        toolbar.setVisibility(View.GONE);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndUrl(
                        R.color.blue,
                        "http://img.sc.chinaz.com/upload/2015/09/01//2015090117073600.jpg");

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            new borrowBook().execute(scanningResult.getContents());
        }
    }

    private class borrowBook extends AsyncTask<String, Void, Void> {
        JSONObject obj;

        protected Void doInBackground(String... pms) {
            try {

                URL url = new URL("https://mjtech.cf/api/book/borrow.php");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("id", pms[0])
                                                       .appendQueryParameter("session", ((CLAN) getApplication()).SESSION);
                String query = builder.build().getEncodedQuery();

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                obj = new JSONObject(readStream(con.getInputStream()));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voided) {
            try {
                if (obj.getInt("err") > 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),obj.getString("errmsg"), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"You borrowed a book", Toast.LENGTH_SHORT);
                    toast.show();

                    refresh(0);
                }
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
    }
}
