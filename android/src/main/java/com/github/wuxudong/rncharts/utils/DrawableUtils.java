package com.github.wuxudong.rncharts.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DrawableUtils {
    public static Drawable drawableFromUrl(String url, final int width, final int height, String color) {
        try {
            return new DrawableLoadingAsyncTask().execute(url, Integer.toString(width), Integer.toString(height), color).get();
        } catch (Exception e) {
            // draw dummy drawable when execution fail
            e.printStackTrace();
            return new ShapeDrawable();
        }
    }

    static class DrawableLoadingAsyncTask extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... strings) {
            try {
                Bitmap x;

                int width = Integer.parseInt(strings[1]);
                int height = Integer.parseInt(strings[2]);

                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
                Bitmap bitmap = x;


                int padding = 40;
                int strokeWidth = 6;

                int bitmapSize = Math.max(bitmap.getWidth(), bitmap.getHeight()) + padding + strokeWidth;
                Bitmap workingBitmap = Bitmap.createBitmap(bitmapSize, bitmapSize,
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(workingBitmap);

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.parseColor(strings[3]));
                paint.setStyle(Paint.Style.STROKE);

                paint.setStrokeWidth(strokeWidth);

                canvas.drawBitmap(bitmap, (bitmapSize - bitmap.getWidth()) / 2.0f,
                        (bitmapSize - bitmap.getHeight()) / 2.0f, paint);

                int centerCoordinate = bitmapSize / 2;

                canvas.drawCircle(centerCoordinate, centerCoordinate,
                        centerCoordinate - (strokeWidth / 2.0f), paint);

                return new BitmapDrawable(Resources.getSystem(), Bitmap.createScaledBitmap(workingBitmap, width, height, true));

            } catch (IOException e) {
                e.printStackTrace();
                // draw dummy drawable when connection fail
                return new ShapeDrawable();
            }
        }
    }

    ;
}
