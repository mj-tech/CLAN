package com.mjtech.clan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        ((CLAN)getApplication()).SESSION = sharedPref.getString("SESSION","");

        setContentView(R.layout.activity_login);

        Button lb = (Button) findViewById(R.id.login);
        lb.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new performLogin().execute();
            }
        });

        if(!((CLAN)getApplication()).SESSION.equals("")) {
            new performLogin().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SESSION", ((CLAN) getApplication()).SESSION);
        editor.commit();
    }

    private class performLogin extends AsyncTask<Void, Void, Void> {
        String username, password;
        JSONObject obj;

        protected void onPreExecute() {
            try {
                (findViewById(R.id.login)).setEnabled(false);
                EditText in1 = (EditText) findViewById(R.id.username);
                EditText in2 = (EditText) findViewById(R.id.password);
                MessageDigest sha = MessageDigest.getInstance("SHA-256");
                username = in1.getText().toString();
                password = toHex(sha.digest(in2.getText().toString().getBytes("UTF-8")));
            } catch (Exception ignored) {
            }
        }

        protected Void doInBackground(Void... voided) {
            try {

                URL url = new URL("https://mjtech.cf/api/account/login.php");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                if (!((CLAN)getApplication()).SESSION.equals("")) {
                    builder.appendQueryParameter("session", ((CLAN) getApplication()).SESSION);
                } else {
                    builder.appendQueryParameter("username", username)
                           .appendQueryParameter("password", password);
                }
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
                    ((TextView) findViewById(R.id.errmsg)).setText(obj.getString("errmsg"));
                    (findViewById(R.id.login)).setEnabled(true);
                    ((CLAN)getApplication()).SESSION = "";
                } else {
                    ((CLAN)getApplication()).FULLNAME = obj.getString("name");
                    ((CLAN)getApplication()).TOKEN = obj.getString("canvasToken");
                    ((CLAN)getApplication()).SESSION = obj.getString("session");

                    Intent intent = new Intent(login.this, main.class);
                    finish();
                    startActivity(intent);
                }
            } catch (Exception ignored) {
            }

        }

        private String toHex(byte[] bytes) {
            StringBuffer result = new StringBuffer();
            for (byte byt : bytes)
                result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
            return result.toString();
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
