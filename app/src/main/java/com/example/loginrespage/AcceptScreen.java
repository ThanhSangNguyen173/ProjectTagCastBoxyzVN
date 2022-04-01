package com.example.loginrespage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class AcceptScreen extends AppCompatActivity {
    TextView animation;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_accept);

        animation = findViewById(R.id.appname);
        lottie = findViewById(R.id.lottie);

        animation.animate().translationY(-1400).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(3000);


        Bundle bundle = getIntent().getExtras();
        String TCid = bundle.getString("TCid");
        String longmap = bundle.getString("long");
        String latmap = bundle.getString("lat");
        String serial = bundle.getString("seri");
        String TCentityNumber = bundle.getString("TCentitynumber");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(AcceptScreen.this,DetailCheckin.class);
                Bundle bundle = new Bundle();
                bundle.putString("TCid", TCid);
                bundle.putString("TCentitynumber", TCentityNumber);
                bundle.putString("long",longmap);
                bundle.putString("seri",serial);
                bundle.putString("lat",latmap);
                i.putExtras(bundle);
                startActivity(i);
            }
        },5000);

    }
}