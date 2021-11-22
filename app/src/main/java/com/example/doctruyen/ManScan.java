package com.example.doctruyen;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doctruyen.adapter.adapterTruyen;
import com.example.doctruyen.database.databasedoctruyen;
import com.example.doctruyen.model.Truyen;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.ArrayList;

public class ManScan extends AppCompatActivity {

    ListView listView;

    EditText edt;

    ArrayList<Truyen> TruyenArrayList;

    ArrayList<Truyen> arrayList;

    adapterTruyen adapterTruyen;

    databasedoctruyen databasedoctruyen;

    Button btnScan;


    private static final int DEFAULT_VIEW = 111;
    private static final int REQUEST_CODE_SCAN = 0X01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_scan);

        listView = findViewById(R.id.listviewTimKiem);
        edt = findViewById(R.id.timkiem);

        btnScan = findViewById(R.id.btnScan);

        initList();

        newViewBtnClick();

        // Bắt click cho item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManScan.this,ManNoiDung.class);
                String tent = arrayList.get(position).getTenTruyen();
                String noidungt = arrayList.get(position).getNoiDung();
                intent.putExtra("tentruyen",tent);
                intent.putExtra("noidung",noidungt);
                startActivity(intent);
            }
        });

        // editText search
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the scanning UI in Default View mode.
                int result = ScanUtil.startScan(ManScan.this, REQUEST_CODE_SCAN,
                        new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
            }

        });
    }


    // search
    private void filter(String text){

        // Xóa dữ liệu mảng
        arrayList.clear();

        ArrayList<Truyen> filteredList = new ArrayList<>();

        for (Truyen item : TruyenArrayList){
            if (item.getTenTruyen().toLowerCase().contains(text.toLowerCase())){

                // Thêm item vào filteredList
                filteredList.add(item);

                // Thêm vào mảng
                arrayList.add(item);
            }
        }
        adapterTruyen.filterList(filteredList);
    }

    // Phương thức lấy dữ liệu, gán vào listview
    private void initList() {
        TruyenArrayList = new ArrayList<>();

        arrayList = new ArrayList<>();

        databasedoctruyen = new databasedoctruyen(this);

        Cursor cursor = databasedoctruyen.getData2();

        while (cursor.moveToNext()){

            int id = cursor.getInt(0);
            String tentruyen = cursor.getString(1);
            String noidung = cursor.getString(2);
            String anh = cursor.getString(3);
            int id_tk = cursor.getInt(4);

            TruyenArrayList.add(new Truyen(id,tentruyen,noidung,anh,id_tk));

            arrayList.add(new Truyen(id,tentruyen,noidung,anh,id_tk));

            adapterTruyen = new adapterTruyen(getApplicationContext(),TruyenArrayList);

            listView.setAdapter(adapterTruyen);
        }
        cursor.moveToFirst();
        cursor.close();
    }
    public void newViewBtnClick() {
        // Replace DEFAULT_VIEW with the code that you customize for receiving the permission verification result.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults.length < 2 ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Process the result after the scanning is complete.
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        // Use ScanUtil.RESULT as the key value to obtain the return value of HmsScan from data returned by the onActivityResult method.
        if (requestCode == REQUEST_CODE_SCAN) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    Toast.makeText(this, ((HmsScan) obj).getOriginalValue(),
                            Toast.LENGTH_SHORT).show();
                    edt.setText(((HmsScan) obj).getOriginalValue());
                }
                return;
            }
        }

    }


}