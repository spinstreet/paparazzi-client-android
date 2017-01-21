package com.spinstreet.paparazzi;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class Helpers {
    public static void makeDialog(Context context, int icon, String title, String content, DialogInterface.OnClickListener listener) {
        try {
            android.app.AlertDialog.Builder ad = new android.app.AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setTitle(title);
            if (content != null) ad.setMessage(content);
            if (icon > 0) ad.setIcon(icon);
            if (listener == null) {
                ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
            } else {
                ad.setNeutralButton("OK", listener);
            }
            ad.show();
        } catch (Exception ignored) {

        }
    }


    public static String get(Context context, String prefs) {
        SharedPreferences settings = context.getSharedPreferences("defaultPreferences", 0);
        return settings.getString(prefs, "");
    }

    public static void set(Context context, String pref, String value) {
        SharedPreferences settings = context.getSharedPreferences("defaultPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(pref, value);

        // Commit the edits!
        editor.apply();
    }
}
