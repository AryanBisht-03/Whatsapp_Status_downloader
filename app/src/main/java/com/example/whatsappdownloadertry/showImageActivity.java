package com.example.whatsappdownloadertry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
            binding.imageView.setImageURI(source);
        }

        Uri finalSource = source;
        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(showImageActivity.this, "Downloading is started", Toast.LENGTH_SHORT).show();
                String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + Calendar.getInstance().getTimeInMillis()+".jpg";

                copyFile(finalSource.getPath(),destination);
            }
        });
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