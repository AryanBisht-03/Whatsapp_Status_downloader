package com.example.whatsappdownloadertry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappdownloadertry.databinding.ActivityShowImageBinding;

import java.io.File;
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
                Toast.makeText(showImageActivity.this, "Downloading is started", Toast.LENGTH_SHORT).show();
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri uri = startCopyFiles(finalSource);

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(intent, "Share App Link Via :"));
            }
        });
    }

    public static Uri startCopyFiles(Uri finalSource)
    {
        String lastName = Calendar.getInstance().getTimeInMillis()+".jpg";
        String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + lastName;
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