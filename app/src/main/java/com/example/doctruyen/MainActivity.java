package com.example.doctruyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.doctruyen.adapter.adapterTruyen;
import com.example.doctruyen.adapter.adapterchuyenmuc;
import com.example.doctruyen.adapter.adapterthongtin;
import com.example.doctruyen.database.databasedoctruyen;
import com.example.doctruyen.model.TaiKhoan;
import com.example.doctruyen.model.Truyen;
import com.example.doctruyen.model.chuyenmuc;
import com.google.android.material.navigation.NavigationView;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    NavigationView navigationView;
    ListView listView,listViewNew,listViewThongTin;
    DrawerLayout drawerLayout;

    String email;
    String tentaikhoan;

    ArrayList<Truyen> TruyenArraylist;

    adapterTruyen adapterTruyen;

    ArrayList<chuyenmuc> chuyenmucArrayList;

    ArrayList<TaiKhoan> taiKhoanArrayList;

    databasedoctruyen databasedoctruyen;

    adapterchuyenmuc adapterchuyenmuc;
    adapterthongtin adapterthongtin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databasedoctruyen = new databasedoctruyen(this);

        // Nhận dữ liệu ở màn đăng nhập gửi
        Intent intentpq = getIntent();
        int i = intentpq.getIntExtra("phanquyen",0);
        int idd = intentpq.getIntExtra("idd",0);
        email = intentpq.getStringExtra("email");
        tentaikhoan = intentpq.getStringExtra("tentaikhoan");

        AnhXa();
        ActionBar();
        ActionViewFlipper();

        // Bắt sự kiện click item
        listViewNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ManNoiDung.class);

                String tent = TruyenArraylist.get(position).getTenTruyen();
                String noidungt = TruyenArraylist.get(position).getNoiDung();
                intent.putExtra("tentruyen", tent);
                intent.putExtra("noidung", noidungt);
                startActivity(intent);
            }
        });

        // bắt click item cho listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Đăng bài
                if (position == 0) {
                    if (i == 3) {
                        Toast.makeText(MainActivity.this, "Bạn không có quyền đăng bài", Toast.LENGTH_SHORT).show();
                        Log.e("Đăng bài : ", "Bạn không có quyền");
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this,ManAdmin.class);
                        //Gửi id tài khoản qua màn admin
                        intent.putExtra("Id",idd);
                        startActivity(intent);
                    }
                }
                // Nếu vị trí ấn vào là thông tin thì sẽ chuyển qua màn thông tin app
                else if(position == 1){
                    Intent intent = new Intent(MainActivity.this,ManThongTin.class);
                    startActivity(intent);
                }
                else if(position == 2){
                    finish();
                }
            }
        });

        HwAds.init(this);

        // Obtain BannerView configured in the XML layout file.
        BannerView bottomBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);

        // Add BannerView through coding.
        BannerView topBannerView = new BannerView(this);
        topBannerView.setAdId("testw6vs28auh3");
        topBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_SMART);
        topBannerView.loadAd(adParam);


    }

    // Thanh action với toolbar
    private void ActionBar() {
        // Hàm hỗ trợ toolbar
        setSupportActionBar(toolbar);

        // set nút cho actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Tạo icon cho toolbar
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);

        // bắt sự kiện click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    // Phương thức cho chạy quảng cáo với ViewFlipper
    private void ActionViewFlipper() {
        // Mảng chứa tấm ảnh cho quảng cáo
        ArrayList<String> mangquangcao = new ArrayList<>();
        // Add ảnh vào mảng
        mangquangcao.add("https://toplist.vn/images/800px/rua-va-tho-230179.jpg");
        mangquangcao.add("https://toplist.vn/images/800px/deo-chuong-cho-meo-230180.jpg");
        mangquangcao.add("https://toplist.vn/images/800px/cu-cai-trang-230181.jpg");
        mangquangcao.add("https://toplist.vn/images/800px/de-den-va-de-trang-230182.jpg");

        // Thực hiện vòng lặp for gán ảnh vào ImageView, rồi từ imgView lên app
        for (int i=0; i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            // Sử dụng hàm thư viện Picasso
            Picasso.get().load(mangquangcao.get(i)).into(imageView);

            // Phương thức chỉnh tấm hình vừa khung quảng cáo
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            // Thêm ảnh từ imageview vào ViewFlipper
            viewFlipper.addView(imageView);
        }
        // Thiết bị tự động chạy cho viewFlipper chạy trong 4 giây
        viewFlipper.setFlipInterval(4000);
        // run auto viewFlipper
        viewFlipper.setAutoStart(true);

        // Gọi Animation cho vào và ra
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);

        // Gọi Animation vào viewFlipper
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setInAnimation(animation_slide_out);

    }

    // Phương thức ánh xạ
    private void AnhXa(){
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        listViewNew = findViewById(R.id.listViewNew);
        listView = findViewById(R.id.listviewmanhinhchinh);
        listViewThongTin = findViewById(R.id.listviewthongtin);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerlayout);

        TruyenArraylist = new ArrayList<>();

        Cursor cursor1 = databasedoctruyen.getData1();
        while (cursor1.moveToNext()){

            int id = cursor1.getInt(0);
            String tentruyen = cursor1.getString(1);
            String noidung = cursor1.getString(2);
            String anh = cursor1.getString(3);
            int id_tk = cursor1.getInt(4);

            TruyenArraylist.add(new Truyen(id,tentruyen,noidung,anh,id_tk));

            adapterTruyen = new adapterTruyen(getApplicationContext(),TruyenArraylist);
            listViewNew.setAdapter(adapterTruyen);
        }
        cursor1.moveToFirst();
        cursor1.close();

        // Thông tin
        taiKhoanArrayList = new ArrayList<>();
        taiKhoanArrayList.add(new TaiKhoan(tentaikhoan,email));

        adapterthongtin = new adapterthongtin(this,R.layout.navigation_thong_tin,taiKhoanArrayList);
        listViewThongTin.setAdapter(adapterthongtin);

        // chuyên mục
        chuyenmucArrayList = new ArrayList<>();
        chuyenmucArrayList.add(new chuyenmuc("Đăng bài",R.drawable.ic_baseline_post_add_24));
        chuyenmucArrayList.add(new chuyenmuc("Thông tin",R.drawable.ic_baseline_face_24));
        chuyenmucArrayList.add(new chuyenmuc("Đăng xuất",R.drawable.ic_baseline_login_24));

        adapterchuyenmuc = new adapterchuyenmuc(this,R.layout.chuyenmuc,chuyenmucArrayList);
        listView.setAdapter(adapterchuyenmuc);

    }

    // Nạp một menu tìm kiếm vào ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Nếu click vào icon tìm kiếm chuyển qua icon tìm kiếm
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent(MainActivity.this, ManScan.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}