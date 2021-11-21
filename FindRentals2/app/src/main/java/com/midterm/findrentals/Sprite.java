package com.midterm.findrentals;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {

    public int nBitmaps;
    public int iBitmaps;
    public Bitmap[] bitmaps;
    public int left;
    public int top;
    public int width;
    public int height;

    public Sprite(Bitmap[] bitmaps, int left, int top, int width, int height) {
        this.bitmaps = bitmaps;
        this.nBitmaps = bitmaps.length;
        this.left = left;
        this.top = top;
        this.iBitmaps = 0;
        if (width == 0 && height == 0){
            this.width = bitmaps[0].getWidth();
            this.height = bitmaps[0].getHeight();
        }
        this.width = width;
        this.height = height;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmaps[iBitmaps], left, top, null);
    }

    public void update(){
        iBitmaps = (iBitmaps + 1) % nBitmaps;
    }
}
