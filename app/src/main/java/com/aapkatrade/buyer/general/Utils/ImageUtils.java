package com.aapkatrade.buyer.general.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by PPC09 on 31-Jan-17.
 */

public class ImageUtils {
    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount();
        } else {
            return data.getAllocationByteCount();
        }
    }

    public static Bitmap resize(Bitmap image, int maxHeight, int maxWidth) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static File getFile(Context context, Bitmap photo) {
        Uri tempUri = null;
        if (photo != null) {
            tempUri = getImageUri(context, photo);
        }
        File finalFile = new File(getRealPathFromURI(context, tempUri));
        AndroidUtils.showErrorLog(context, "getFile data", getRealPathFromURI(context, tempUri));

        return finalFile;
    }

    public static Uri getImageUri(Context context, Bitmap bitmapImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmapImage, "Title", null);
        return Uri.parse(path);
    }




    public static String copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
        byte[] byteArray = new byte[1024];

        while (fileInputStream.read(byteArray) > 0) {
            fileOutputStream.write(byteArray, 0, fileInputStream.read(byteArray));
        }
        fileInputStream.close();
        fileOutputStream.close();
        return destinationFile.getAbsolutePath();
    }


    public static String getRealPathFromURI(Context context, Uri uri) {
        String path = "";
        if (uri != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int idx = 0;
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }
        return path;
    }

    public static String getVideoPath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Video.Media.DATA}, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    public static String getRealPathFromURIVideo(Context context, Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

}
