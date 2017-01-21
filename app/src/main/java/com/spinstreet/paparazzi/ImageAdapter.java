package com.spinstreet.paparazzi;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

/**
 * Created by connor on 2017/01/21.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mImages;

    public ImageAdapter(Context c, String[] images) {
        mContext = c;
        mImages = images;
    }

    public int getCount() {
        return mImages.length;
//        return 20;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ScaleImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setMinimumHeight(400);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Ion.with(imageView)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .animateLoad(spinAnimation)
//                .animateIn(fadeInAnimation)
                .load(mImages[position]);

        return imageView;
    }

    // references to our images
//    private Integer[] mThumbIds = {};
}
