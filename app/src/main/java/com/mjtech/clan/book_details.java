package com.mjtech.clan;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class book_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        ((TextView)findViewById(R.id.title)).setText(getIntent().getStringExtra("TITLE"));
        ((TextView)findViewById(R.id.expire)).setText("Expire at: " + getIntent().getStringExtra("EXPIRE"));
        findViewById(R.id.renew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new renewbook().execute(getIntent().getStringExtra("ID"));
            }
        });

        Slidr.attach(this);

    }

    private class renewbook extends AsyncTask<String, Void, Void> {
        JSONObject obj;

        protected Void doInBackground(String... pms) {
            try {

                URL url = new URL("https://mjtech.cf/api/book/renew.php");
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
                    Toast toast = Toast.makeText(getApplicationContext(), obj.getString("errmsg"), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You renewed this book.", Toast.LENGTH_SHORT);
                    toast.show();

                    finish();
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