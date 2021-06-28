package com.xdev.expy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xdev.expy.R;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import static com.xdev.expy.utils.Constants.NO_AVATAR;
import static com.xdev.expy.utils.Constants.NUMBER_OF_DEFAULT_AVATARS;

public class AppUtils {

    public static int getAvatarFromResource(Uri avatar) {
        if (avatar == null) avatar = Uri.parse("");
        switch (avatar.toString()) {
            case "default_1": return R.drawable.ic_avatar_default_01;
            case "default_2": return R.drawable.ic_avatar_default_02;
            case "default_3": return R.drawable.ic_avatar_default_03;
            case "default_4": return R.drawable.ic_avatar_default_04;
            case "default_5": return R.drawable.ic_avatar_default_05;
            case "default_6": return R.drawable.ic_avatar_default_06;
            case "default_7": return R.drawable.ic_avatar_default_07;
            case "default_8": return R.drawable.ic_avatar_default_08;
            case "default_9": return R.drawable.ic_avatar_default_09;
            default: return NO_AVATAR;
        }
    }

    public static String getRandomAvatar() {
        return String.format(Locale.getDefault(),
                "default_%d", new Random().nextInt(NUMBER_OF_DEFAULT_AVATARS));
    }

    public static boolean isNetworkAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void loadImage(Context context, ImageView imageView, Object source) {
        if (source == null) source = "";
        Glide.with(context)
                .asBitmap()
                .load(source)
                .apply(myGlideOptions())
                .centerCrop()
                .into(imageView);
    }

    @NonNull
    private static RequestOptions myGlideOptions(){
        return RequestOptions.placeholderOf(R.drawable.ic_no_avatar)
                .error(R.drawable.ic_no_avatar);
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
