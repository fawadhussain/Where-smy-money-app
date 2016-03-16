package com.example.wsmm.util;

/**
 * Created by abubaker on 17/03/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by abubaker on 9/11/15.
 */
public class ImageUtils {

    static Target target;


    public static void loadImageLocally(Context context,int width, int height, ImageView imageView, String url) {
        Picasso.with(context).load("file://" + url).resize(width,height).into(imageView);
    }

    public static void loadImageFromServer(Context context,ImageView imageView,String url){
        Picasso.with(context).load(url).into(imageView);
    }

    public static void loadImageFromServer(Context context, String url,int width,int height ,final OnImageLoadListener listener){
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                listener.onBitmapLoaded(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                listener.onError();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context).load(url).resize(width, height).into(target);

    }

    public interface OnImageLoadListener {
        public void onBitmapLoaded(Bitmap bmp);
        public void onError();
    }





}
