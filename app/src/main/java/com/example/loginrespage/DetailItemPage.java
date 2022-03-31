package com.example.loginrespage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailItemPage extends AppCompatActivity {
    TextView txt_tenmon,txt_giamon,txt_nguyenlieu;
    ImageView img_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailitem);Button btnback = findViewById(R.id.btn_back_to_menu);
        txt_giamon = findViewById(R.id.txt_gia_item);
        txt_nguyenlieu = findViewById(R.id.txt_nguyenlieu_item);
        txt_tenmon = findViewById(R.id.txt_ten_item);
        img_item = findViewById(R.id.img_detail_item);

        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            return;
        }

        Item item = (Item) bundle.get("object_item");
        txt_giamon.setText(item.getPrice());
        txt_tenmon.setText(item.getName());
        txt_nguyenlieu.setText(item.getNguyenlieu());
        img_item.setImageResource(item.getImage());

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailItemPage.this,Menu_page.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

//        Button btnback = findViewById(R.id.btn_back_to_menu);
//        btnback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DetailItemPage.this,LoginResActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}