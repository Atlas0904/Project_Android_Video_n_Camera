package com.example.atlas_huang.videorecorddemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Created by atlas_huang on 2017/3/27.
 */

public class FileManipulateHelper {

    private final static String IMAGE_DIR = "img";
    private final static String IMAGE_PREFIX = "img";
    private final static String IMAGE_SUB_FILENAME = "jpg";
    private static int count = 0;

    private Context mContext;


    public static int saveToExternalStorage(String filename, Uri data) {
        return 0;
    }

    public static int saveToInternalStorage(String filename, Uri data) {
        return 0;
    }


//    public static int saveToExternalStorage(Context context, String filename, Uri uri) {
//        FileOutputStream outputStream = null;
//        try {
//
//            InputStream in = context.getContentResolver().openInputStream(uri);
//            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
//
//            int b;
//            white (b - in.red)
//
//            outputStream.write(data.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }


    public static int saveToExternalStorage(Context context, String filename, String data) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String saveToInternalStorage(Context context, Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);

        // Compose filename
        String filename = IMAGE_PREFIX + "_" + (count++) + IMAGE_SUB_FILENAME;
        File mypath=new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    public static String getTimeFormat() {
        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        return timeStamp;
    }

}
