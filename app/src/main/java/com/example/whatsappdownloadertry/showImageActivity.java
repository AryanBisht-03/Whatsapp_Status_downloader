package com.example.whatsappdownloadertry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappdownloadertry.databinding.ActivityShowImageBinding;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class showImageActivity extends AppCompatActivity {

    ActivityShowImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Uri source = null;

        if(intent.hasExtra("Data"))
        {
            source = (Uri) intent.getExtras().get("Data");
            Glide.with(this).load(source).into(binding.imageView);
        }

        if(source.toString().endsWith(".mp4"))
            binding.playButtonFull.setVisibility(View.VISIBLE);
        else
            binding.playButtonFull.setVisibility(View.INVISIBLE);

        Uri finalSource = source;
        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(showImageActivity.this, "Downloading is started", Toast.LENGTH_SHORT).show();
                startCopyFiles(finalSource);
            }
        });

        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri uri = finalSource;

                try {
                    if(uri.toString().endsWith(".jpg"))
                        shareImage(uri.getPath());
                    else
                        shareVideo(uri.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void shareImage(String filepath) throws FileNotFoundException {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/*");

        File filesDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filepath);
        String mediaPath = MediaStore.Images.Media.insertImage(getContentResolver(), filepath, filesDir.getName(), null);
        Uri uri = Uri.parse(mediaPath);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(shareIntent);
        }
        catch (android.content.ActivityNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void shareVideo(String filepath){
        Intent shareintent=new Intent("android.intent.action.SEND");
        shareintent.setType("video/mp4");
        shareintent.putExtra("android.intent.extra.STREAM", Uri.parse(filepath));
        startActivity(Intent.createChooser(shareintent,"share"));

    }

    public static Uri startCopyFiles(@NonNull Uri finalSource)
    {
        String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File file = new File(destination);

        String lastName = (new File(finalSource.getPath())).getName();

        if(!file.exists())
            file.mkdir();

        destination = destination + lastName;
        File[] allfiles = file.listFiles();
        boolean found=true;
        for(File data:allfiles)
        {
            if(data.getName().equals(lastName)){
                found = false;
                break;
            }
        }
        if(found)
            copyFile(finalSource.getPath(),destination);

        return Uri.fromFile(new File(destination));
    }
    public static void copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);


            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            Log.d("Aryan","Error ");
        } catch (Exception e) {
            Log.d("Aryan", e.getMessage());
        }
    }
}