package com.example.doctruyen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ManThongTin extends AppCompatActivity {

    TextView txtThongtinapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_thong_tin);

        txtThongtinapp = findViewById(R.id.textviewthongtin);

        String thongtin = " App được phát triển bởi Nhóm 6\n"+
                " FITHOU";

        txtThongtinapp.setText(thongtin);
    }
}