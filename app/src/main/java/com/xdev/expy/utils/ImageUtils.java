package com.xdev.expy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = null;

        try {
            // Convert bitmap to byte[]
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (stream == null) return new byte[0];
        return stream.toByteArray();
    }

    public static byte[] convertUriToByteArray(Context context, Uri uri){
        Bitmap bitmap = null;

        try {
            // Convert uri to bitmap
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e){
            e.printStackTrace();
        }

        return convertBitmapToByteArray(bitmap);
    }

    public static byte[] getCompressedByteArray(byte[] image, boolean isResized){
        ByteArrayOutputStream stream = null;

        try {
            // Convert byte[] to bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(image));

            if (isResized){
                // Change bitmap size
                if (!(bitmap.getWidth() <= 1024)){
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1024,
                            (int) (bitmap.getHeight() * (1024.0/bitmap.getWidth())), true);
                }
            }

            // Compress bitmap and get byte[]
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (stream == null) return new byte[0];
        return stream.toByteArray();
    }
}
