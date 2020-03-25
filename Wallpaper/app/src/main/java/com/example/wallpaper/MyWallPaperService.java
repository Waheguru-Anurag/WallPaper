package com.example.wallpaper;

import java.nio.channels.AsynchronousChannel;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MyWallPaperService extends WallpaperService {

    static int hexcode = 0x0000ff;
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
            paint.setColor(Color.WHITE);
            paint.setTextSize(200);
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
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawtext(canvas, hexcode);
                    if(hexcode == 0xffffff)
                        hexcode = 0;
                    else
                        hexcode++;
                    onDestroy();
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 5000);
            }
        }

        public void drawtext(Canvas canvas, int hexcode) {
            canvas.drawARGB(100, Color.red(hexcode), Color.green(hexcode), Color.blue(hexcode));
            canvas.drawText(Integer.toHexString(hexcode),width/2,height/2,paint);
        }
    }

}
