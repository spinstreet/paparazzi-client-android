package com.spinstreet.paparazzi;


import android.content.Context;
import android.content.DialogInterface;

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
}
