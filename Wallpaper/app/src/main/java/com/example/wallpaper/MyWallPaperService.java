package com.example.wallpaper;

import java.nio.channels.AsynchronousChannel;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

public class MyWallPaperService extends WallpaperService {

    float x,y;
    public Clock clock;
    String curTime;
    int hexcode = 255;

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        private Paint paint = new Paint();
        int width;
        int height;
        private boolean visible = true;

        public MyWallpaperEngine() {

            paint.setColor(Color.parseColor("#ffffff"));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(120);
            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }


        private void draw() {
            Date date = new Date();
            int hours = date.getHours();
            int minutes = date.getMinutes();
            int seconds = date.getSeconds();
            String h = hours + "";
            if (hours/10 == 0) h = "0" + hours;
            String m = minutes + "";
            if (minutes/10 == 0) m = "0" + minutes;
            String s = seconds + "";
            if (seconds/10 == 0) s = "0" + seconds;

            curTime = h+":"+m+":"+s;
            int color = Color.parseColor("#" + h + m + s);

            //String hexa = Integer.toHexString(hexcode);
            //char ch[] = new char[6];
            //for(int i=0; i<6; i++)
                //ch[i] = '0';
            //for(int i=0; i<Math.min(hexa.length(),6); i++)
              //  ch[5-i]=hexa.charAt(Math.min(hexa.length(),6)-i-1);
            //String q = new String(ch);
            //curTime = "#"+ q;

            //int color = Color.parseColor("#" + q);


            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = holder.lockCanvas();
            if (canvas != null){
                canvas.drawColor(color);
                x = (canvas.getWidth() / 2) - (paint.measureText(curTime) / 2);
                y = (canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2);
                canvas.drawText(curTime,x,y,paint);
                holder.unlockCanvasAndPost(canvas);
                hexcode++;
            }
            handler.removeCallbacks(drawRunner);
            if (visible){
                handler.postDelayed(drawRunner,1000);
            }
        }

    }

}
