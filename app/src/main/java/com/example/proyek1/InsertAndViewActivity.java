package com.example.proyek1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

class InsertAndViewActivity extends Activity {
    ListView listView;
    LinearLayout lineKosong;
    public static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> data = (Map<String, Object>) adapterView.getAdapter().getItem(i);
                startActivity(new Intent(InsertAndViewActivity.this,MainActivity.class)
                        .putExtra("filename",data.get("name").toString()));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> data = (Map<String, Object>) adapterView.getAdapter().getItem(i);
                openDialogHapus(data.get("name").toString());
                return true;
            }
        });
    }

    private void setContentView(int activity_list_note) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT>=23){
            if (periksaPenyimpanan()){
                ambilFile();
            }
        }else{
            ambilFile();
        }
    }
    public boolean periksaPenyimpanan(){
        if (Build.VERSION.SDK_INT>=23){
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)){
                return true;
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
                return false;
            }
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ambilFile();
            }
        }
    }
    public void ambilFile(){
        String path= Environment.getExternalStorageDirectory().toString()+"/kominfo.proyek";
        File file = new File(path);
        if (file.exists()){
            lineKosong.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            File[] files = file.listFiles();
            String [] filename = new String[files.length];
            String [] dateCreated = new String[files.length];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
            ArrayList<Map<String, Object>> itemData = new ArrayList<Map<String, Object>>();
            for (int i =0;i<files.length;i++ ){
                filename[i] = files[i].getName();
                Date lastModDate = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lastModDate);
                Map<String,Object> listItem = new HashMap<>();
                listItem.put("name", filename[i]);
                listItem.put("date", dateCreated[i]);
                itemData.add(listItem);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(this,itemData, android.R.layout.simple_list_item_1,new String[]{"name","date"},new int[]{android.R.id.text1,android.R.id.text1 });
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }else{
            lineKosong.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_tambah) {
            startActivity(new Intent(InsertAndViewActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public void openDialogHapus(final String file){
        new AlertDialog.Builder(this)
                .setTitle("Hapus Note Ini?")
                .setMessage("Apakah Anda yakin ingin menghapus Note"+file+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusFile(file);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
    public void hapusFile(String file){
        String path= Environment.getExternalStorageDirectory().toString()+"/kominfo.proyek";
        File files = new File(path, file);
        if (files.exists()){
            files.delete();
        }
        ambilFile();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

