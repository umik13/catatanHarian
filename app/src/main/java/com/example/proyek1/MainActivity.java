package com.example.proyek1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;

class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_STORAGE = 100;
    ListView listView;
    private Toolbar supportActionBar;
    private int[] filename;
    private AbstractQueue<Map<String, Object>> itemDataList;
    private Object[] dateCreated;
    private Formatter simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aplikasi Catatan Proyek 1");
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InsertAndViewActivity.class);
                Map<String, Object> data = (Map<String, Object>) parent.getAdapter().getItem(position);
                intent.putExtra("filename"), data.get("name").toString();
                Toast.makeText(MainActivity.this, "You Clicked" + data.get("nama"), Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> data = (Map<String, Object>) parent.getAdapter().getItem(position);
                tampilkanDialogKonfirmasiHapusCatatan(data.get("nama").toString());
                return true;
            }
        });
    }
    @Override
    protected void onResume() {
    super.onResume();
    if (Build.VERSION.SDK_INT>= 23) {
        if (periksaIzinPenyimpanan()) {
            mengambilListFilePadaFolder();
        }
    }else {
        mengambilListFilePadaFolder();
        }
    }

    private boolean periksaIzinPenyimpanan() {
        if (Build.VERSION.SDK_INT>=23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        }else {
            return true;
            }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case REQUEST_CODE_STORAGE:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    mengambilListFilePadaFolder();
                }
                break;
        }
    }
    void mengambilListFilePadaFolder() {
        String path = Environment.getExternalStorageState().toString() + "/kominfo.proyek1";
        File directory = new File(path);

        File[] files = new File[0];
        if (directory.exists()) {
            files = directory.listFiles();
            String[] filename = new String[files.length];
            String[] dateCreated = new String[files.length];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY HH:mn:ss");
            ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();
        }
        for (int i = 0; i < files.length; i++) {
        filename[i] = Integer.parseInt(files[i].getName());
            Date lastModDate = new Date (files[i].lastModified());
            dateCreated[i] = simpleDateFormat.format(lastModDate);
            Map<String, Object> listItemMap = new HashMap<>();
            listItemMap.put("name", filename[i]);
            listItemMap.put("date", dateCreated[i]);
            itemDataList.add(listItemMap);
        }
    }

    private void tampilkanDialogKonfirmasiHapusCatatan(String nama) {
            }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
    }


};
