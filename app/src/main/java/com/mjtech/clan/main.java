package com.mjtech.clan;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAnimation(findViewById(R.id.button1), 0, 370, 0);
        setAnimation(findViewById(R.id.button2), 60, 370, 100);
        setAnimation(findViewById(R.id.button3), 120, 370, 200);
        setAnimation(findViewById(R.id.button4), 180, 370, 300);
        setAnimation(findViewById(R.id.button5), 240, 370, 400);
        setAnimation(findViewById(R.id.button6), 300, 370, 500);

        findViewById(R.id.bg).setBackgroundColor(0xFF114477);
        findViewById(R.id.center).getBackground().setColorFilter(0xFF222222, PorterDuff.Mode.ADD);
        findViewById(R.id.button1).getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.ADD);
        findViewById(R.id.button2).getBackground().setColorFilter(0xFFFF6600, PorterDuff.Mode.ADD);
        findViewById(R.id.button3).getBackground().setColorFilter(0xFFCC9900, PorterDuff.Mode.ADD);
        findViewById(R.id.button4).getBackground().setColorFilter(0xFF006600, PorterDuff.Mode.ADD);
        findViewById(R.id.button5).getBackground().setColorFilter(0xFF0033FF, PorterDuff.Mode.ADD);
        findViewById(R.id.button6).getBackground().setColorFilter(0xFF6600CC, PorterDuff.Mode.ADD);



        findViewById(R.id.button4).setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), setting.class);
                //startActivity(intent);
            }
        });
    }

    public void setAnimation(View target, int degree, int distance, int delay){
        float tran_x=(float)Math.sin(Math.toRadians(degree))*distance;
        float tran_y=(float)Math.cos(Math.toRadians(degree))*distance;
        target.setX(target.getX()-tran_x);
        target.setY(target.getY() - tran_y);
        TranslateAnimation anim = new TranslateAnimation(tran_x,0,tran_y,0);
        anim.setStartOffset(delay);
        anim.setDuration(1250);
        anim.setFillAfter(true);
        target.startAnimation(anim);
    }
}