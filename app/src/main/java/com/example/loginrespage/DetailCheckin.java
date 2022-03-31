package com.example.loginrespage;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailCheckin extends AppCompatActivity {

    TextView txtID,txtEntityNumber;
    Button btn_go_to_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_checkin_page);

        btn_go_to_menu = findViewById(R.id.btn_go_to_menu);
        txtEntityNumber = findViewById(R.id.txt_EntityNumber);
        txtID = findViewById(R.id.txtID);


        Bundle bundle = getIntent().getExtras();
        String TCid = bundle.getString("TCid");
        String TCentityNumber = bundle.getString("TCentitynumber");

        txtID.setText(TCid);
        txtEntityNumber.setText(TCentityNumber);

        btn_go_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailCheckin.this,Menu_page.class);
                startActivity(intent);
            }
        });


    }

}