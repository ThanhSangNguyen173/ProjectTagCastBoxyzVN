package com.example.loginrespage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class DetailCheckin extends AppCompatActivity {

    TextView txtID,txtEntityNumber,txtSerial;
    Button btn_go_to_menu;
    private String longmap,latmap,serial;
    private double lo,la;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_checkin_page);

        btn_go_to_menu = findViewById(R.id.btn_go_to_menu);
        txtEntityNumber = findViewById(R.id.txt_EntityNumber);
        txtID = findViewById(R.id.txtID);
        txtSerial = findViewById(R.id.txtserial);


        Bundle bundle = getIntent().getExtras();
        String TCid = bundle.getString("TCid");
        String TCentityNumber = bundle.getString("TCentitynumber");
        longmap = bundle.getString("long");
        latmap = bundle.getString("lat");
        serial = bundle.getString("seri");

        txtSerial.setText(serial);
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