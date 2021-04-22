package com.xdev.expy.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xdev.expy.R;

public class AppUtils {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void loadImage(Context context, ImageView imageView, Object source) {
        Glide.with(context)
                .asBitmap()
                .load(source)
                .apply(getGlideOptions())
                .centerCrop()
                .into(imageView);
    }

    private static RequestOptions getGlideOptions(){
        return RequestOptions.placeholderOf(R.drawable.ic_no_avatar)
                .error(R.drawable.ic_no_avatar);
    }
}
