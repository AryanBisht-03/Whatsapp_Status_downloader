package com.example.whatsappdownloadertry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappdownloadertry.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final int WRITE_EXTERNAL_STORAGE=200;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Whatsapp status ");

        // providing subtitle for the ActionBar
        actionBar.setSubtitle("Aryan Bisht");
        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if(Environment.isExternalStorageManager())
                checkForLocation();
            else
            {
                try
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 101);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 101);
                }
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                checkForLocation();
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE);
        }
        else
            checkForLocation();
    }

    private void checkForLocation()
    {
        String address = "/Whatsapp/Media/.Statuses";

        String source = Environment.getExternalStorageDirectory().toString()+address;

        File checkAddress = new File(source);
        if(!checkAddress.exists()){
            Log.d("Aryan","First one is not present");
            address = "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
            source = Environment.getExternalStorageDirectory().toString()+address;
            checkAddress = new File(source);
        }

        if(!checkAddress.exists())
        {
            Log.d("Aryan",address);
            Toast.makeText(this,"There is some error try again",Toast.LENGTH_SHORT).show();
            return;
        }

        File sourceFile = new File(source);
        File[] name = sourceFile.listFiles();
        ArrayList<ItemModel> items = new ArrayList<>();

        for(File child:name){
            if(child.getName().equals(".nomedia"))
                continue;
            ItemModel data = new ItemModel(Uri.fromFile(child),child);
            data.image= child.getName().contains(".jpg");
            items.add(data);
        }

        Log.d("Aryan","Size = " + items.size());
        RecycleAdapter adapter = new RecycleAdapter(this,items);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            checkForLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){
            case R.id.b2:
                Intent intent2 = new Intent(Intent.ACTION_SEND);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                intent2.putExtra(Intent.EXTRA_TEXT,"https://github.com/AryanBisht-03/Whatsapp_Status_downloader");
                intent2.setType("text/plain");
                startActivity(Intent.createChooser(intent2, "Share APP"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}