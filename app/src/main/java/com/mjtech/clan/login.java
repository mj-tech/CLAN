package com.mjtech.clan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, main.class);
        finish();
        startActivity(intent);

        setContentView(R.layout.activity_login);
    }
}
