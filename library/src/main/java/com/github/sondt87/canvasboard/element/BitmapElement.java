package com.github.sondt87.canvasboard.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BitmapElement extends BaseElement {
    Bitmap bitmap;

    public BitmapElement(float x, float y, Bitmap bitmap) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight());
        this.bitmap = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,getX(), getY(), null);
    }
}
