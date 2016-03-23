package com.mjtech.clan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class nfctransfer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctransfer);

        new initTransfer().execute();
    }

    private class initTransfer extends AsyncTask<Void, Void, Void> {
        JSONObject obj;

        protected Void doInBackground(Void... voided) {
            try {
                URL url = new URL("https://mjtech.cf/api/nfctransfer/init.php");
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

                obj = new JSONObject(readStream(con.getInputStream()));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voided) {
            try {
                if (obj.getInt("err") > 0) {
                    Intent intent = new Intent(nfctransfer.this, login.class);
                    finish();
                    startActivity(intent);
                } else {
                    NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(nfctransfer.this);
                    nfcAdapter.setNdefPushMessage(new NdefMessage(NdefRecord.createMime("TOKEN", obj.getString("nfcsession").getBytes())), nfctransfer.this);
                }
            } catch (Exception e) {}
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
