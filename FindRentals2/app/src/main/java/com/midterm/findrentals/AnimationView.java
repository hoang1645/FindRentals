package com.midterm.findrentals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationView extends View {

    private List<Sprite> sprites;

    private Timer timer;
    private TimerTask timerTask;

    public AnimationView(Context context) {
        super(context);
        prepareContent();
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        prepareContent();
    }

    private void prepareContent() {
        sprites = new ArrayList<>();
        createHouse(10, 10);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i<sprites.size(); i++)
                    sprites.get(i).update();

                postInvalidate();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 500, 500);
    }

    private void createHouse(int left, int top) {
        Bitmap[] bitmaps = new Bitmap[4];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite1);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite2);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite3);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite4);
        Sprite houseSprite = new Sprite(bitmaps, left, top, 0, 0);
        sprites.add(houseSprite);
    }

    @Override
    protected void onDraw(Canvas canvas){
        for (int i=0; i<sprites.size(); i++)
            sprites.get(i).draw(canvas);
    }
}
