package com.mjtech.clan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.MessageDigest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class main extends Activity {
    DisplayMetrics displaymetrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int dis = displaymetrics.widthPixels / 3; //370

        setAnimation(findViewById(R.id.button1), 0, dis, 0);
        setAnimation(findViewById(R.id.button2), 60, dis, 100);
        setAnimation(findViewById(R.id.button3), 120, dis, 200);
        setAnimation(findViewById(R.id.button4), 180, dis, 300);
        setAnimation(findViewById(R.id.button5), 240, dis, 400);
        setAnimation(findViewById(R.id.button6), 300, dis, 500);

        findViewById(R.id.center).getBackground().setColorFilter(0xFF222222, PorterDuff.Mode.ADD);
        findViewById(R.id.button1).getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.ADD);
        findViewById(R.id.button2).getBackground().setColorFilter(0xFFFF6600, PorterDuff.Mode.ADD);
        findViewById(R.id.button3).getBackground().setColorFilter(0xFFCC9900, PorterDuff.Mode.ADD);
        findViewById(R.id.button4).getBackground().setColorFilter(0xFF006600, PorterDuff.Mode.ADD);
        findViewById(R.id.button5).getBackground().setColorFilter(0xFF0033FF, PorterDuff.Mode.ADD);
        findViewById(R.id.button6).getBackground().setColorFilter(0xFF6600CC, PorterDuff.Mode.ADD);

        findViewById(R.id.button1).setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), course.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), book.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), wallet.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.center).setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), nfctransfer.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                new performLogout().execute();
            }
        });
    }

    public void setAnimation(View target, int degree, int distance, int delay) {
        float tran_x = (float) Math.sin(Math.toRadians(degree)) * distance;
        float tran_y = (float) Math.cos(Math.toRadians(degree)) * distance;
        target.setX(target.getX() - tran_x);
        target.setY(target.getY() - tran_y);
        TranslateAnimation anim = new TranslateAnimation(tran_x, 0, tran_y, 0);
        anim.setStartOffset(delay);
        anim.setDuration(1250);
        anim.setFillAfter(true);
        target.startAnimation(anim);
    }

    private class performLogout extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... voided) {
            try {
                URL url = new URL("https://mjtech.cf/api/account/logout.php");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("session", ((CLAN) getApplication()).SESSION);

                String query = builder.build().getEncodedQuery();

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                con.getInputStream();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voided) {
            ((CLAN) getApplication()).SESSION = "";
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("SESSION", ((CLAN) getApplication()).SESSION);
            editor.commit();
            Intent intent = new Intent(main.this, login.class);
            finish();
            startActivity(intent);
        }
    }
}
