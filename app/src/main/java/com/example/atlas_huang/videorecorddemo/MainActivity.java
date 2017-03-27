package com.example.atlas_huang.videorecorddemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int VIDEO_CAPTURE = 0;
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_VIDEO_CAPTURE = 1;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    Button mBtnVideRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnVideRecord = (Button) findViewById(R.id.buttonVideoRecord);
        mBtnVideRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
        });
        permissionCheck(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }



    private void dispatchTakeVideoIntent() {

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            File file = new File(getFilesDir(), "video.mp4");

            Log.d(TAG, "videoUri= " + videoUri + " file=" + file);
            restoreFileFromUri(videoUri, file);

            deleteContentByUri(videoUri);
        }
    }

    private void deleteContentByUri(Uri uri) {
        Log.d(TAG, "deleteContentByUri uri=" + uri);
        getContentResolver().delete(uri, null, null);
    }

    private void restoreFileFromUri(Uri uri, File file) {

        final int chunkSize = 1024;  // We'll read in one kB at a time  <-- Need to check the bulk size
        byte[] imageData = new byte[chunkSize];

        InputStream in = null;
        OutputStream out = null;

        try {
            in = getContentResolver().openInputStream(uri);
            out = new FileOutputStream(file);  // I'm assuming you already have the File object for where you're writing to

            int bytesRead;
            while ((bytesRead = in.read(imageData)) > 0) {
                out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
            }

        } catch (Exception ex) {
            Log.e(TAG, "Something went wrong." + ex);
        } finally {
            close(in);
            close(out);
        }

    }

    public static void close (Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            //log the exception
            Log.d(TAG, "close: IOException e=" + e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void permissionCheck(String permission) {
        if (checkSelfPermission(permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(permission)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }
    }
}
