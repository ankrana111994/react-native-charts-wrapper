    package com.github.wuxudong.rncharts.utils;

    import android.content.Context;
    import android.content.res.Resources;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;
    import android.graphics.PorterDuff;
    import android.graphics.PorterDuffXfermode;
    import android.graphics.Rect;
    import android.graphics.drawable.BitmapDrawable;
    import android.graphics.drawable.Drawable;
    import android.graphics.drawable.ShapeDrawable;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.provider.MediaStore;
    import android.util.Log;


    import com.github.wuxudong.rncharts.BuildConfig;

    import java.io.IOException;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;

    public class DrawableUtils {


        public static Drawable drawableFromUrl(Context context, String url, final int width, final int height, String color) {
            if (BuildConfig.BUILD_TYPE == "debug") {
                try {
                    return new DrawableLoadingAsyncTask().execute(url, Integer.toString(width), Integer.toString(height), color).get();
                } catch (Exception e) {
                    // draw dummy drawable when execution fail
                    e.printStackTrace();
                    return new ShapeDrawable();
                }
            } else {
                if (context!=null) {

                    int resourceId = context.getResources().getIdentifier(url, "drawable", context.getPackageName());
                    Bitmap x = BitmapFactory.decodeResource(context.getResources(), resourceId);

                    if (x == null) {
                        Log.e("ERR", "Failed to decode resource - " + resourceId + " " + context.getResources().toString());
                        return null;
                    }
                    Bitmap bitmap = x;
                    int padding = 70;
                    int strokeWidth = 15;
                    int bitmapSize = Math.max(bitmap.getWidth(), bitmap.getHeight()) + padding + strokeWidth;
                    Bitmap workingBitmap = Bitmap.createBitmap(bitmapSize, bitmapSize,
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(workingBitmap);
                    int centerCoordinate = bitmapSize / 2;

                    Paint paint1 = new Paint();
                    paint1.setAntiAlias(true);
                    paint1.setColor(Color.WHITE);
                    paint1.setStyle(Paint.Style.FILL);

                    paint1.setStrokeWidth(strokeWidth);
                    canvas.drawCircle(centerCoordinate, centerCoordinate,
                            centerCoordinate - (strokeWidth / 2.0f), paint1);
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.parseColor(color));
                    paint.setStyle(Paint.Style.STROKE);

                    paint.setStrokeWidth(strokeWidth);


                    canvas.drawBitmap(bitmap, (bitmapSize - bitmap.getWidth()) / 2.0f,
                            (bitmapSize - bitmap.getHeight()) / 2.0f, paint);

                   // int centerCoordinate = bitmapSize / 2;

                    canvas.drawCircle(centerCoordinate, centerCoordinate,
                            centerCoordinate - (strokeWidth / 2.0f), paint);

                    return new BitmapDrawable(Resources.getSystem(), Bitmap.createScaledBitmap(workingBitmap, width, height, true));
                }
                else {
                    return new ShapeDrawable();

                }
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
                    int padding = 70;
                    int strokeWidth = 15;

                    int bitmapSize = Math.max(bitmap.getWidth(), bitmap.getHeight()) + padding + strokeWidth;
                    Bitmap workingBitmap = Bitmap.createBitmap(bitmapSize, bitmapSize,
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(workingBitmap);
                    int centerCoordinate = bitmapSize / 2;

                    Paint paint1 = new Paint();
                    paint1.setAntiAlias(true);
                    paint1.setColor(Color.WHITE);
                    paint1.setStyle(Paint.Style.FILL);

                    paint1.setStrokeWidth(strokeWidth);
                    canvas.drawCircle(centerCoordinate, centerCoordinate,
                            centerCoordinate - (strokeWidth / 2.0f), paint1);

                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.parseColor(strings[3]));
                    paint.setStyle(Paint.Style.STROKE);

                    paint.setStrokeWidth(strokeWidth);

                    canvas.drawBitmap(bitmap, (bitmapSize - bitmap.getWidth()) / 2.0f,
                            (bitmapSize - bitmap.getHeight()) / 2.0f, paint);

                  //  int centerCoordinate = bitmapSize / 2;

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


    }
