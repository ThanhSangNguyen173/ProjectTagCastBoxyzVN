package com.example.loginrespage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class ViewFlipperIntro extends AppCompatActivity {

    ViewFlipper viewFlipper;
    Animation imgin,imgout;
    Button btnjoin;

    private void anh_xa_a()
    {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFliper);
        btnjoin   = (Button) findViewById(R.id.buttonJoinIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flipper_intro);

        anh_xa_a();

        imgin = AnimationUtils.loadAnimation(this,R.anim.file_in);
        imgout= AnimationUtils.loadAnimation(this,R.anim.file_out);

        viewFlipper.setInAnimation(imgin);
        viewFlipper.setOutAnimation(imgout);

        viewFlipper.setFlipInterval(2500);
        viewFlipper.setAutoStart(true);

        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_JoinIn = new Intent();
                intent_JoinIn.setClass(ViewFlipperIntro.this,MainActivity.class);
                startActivity(intent_JoinIn);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });



    }


}