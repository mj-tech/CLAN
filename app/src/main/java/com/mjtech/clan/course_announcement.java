package com.mjtech.clan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;

public class course_announcement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_announcement);
        ((TextView)findViewById(R.id.title)).setText(getIntent().getStringExtra("TITLE"));
        ((TextView)findViewById(R.id.author)).setText(getIntent().getStringExtra("AUTHOR"));
        ((TextView)findViewById(R.id.date)).setText(getIntent().getStringExtra("DATE"));
        ((TextView)findViewById(R.id.message)).setText(getIntent().getStringExtra("MESSAGE"));
        Slidr.attach(this);

    }

}
