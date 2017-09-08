package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.github.sondt87.canvasboard.camera.Camera;


public interface Element {
    public void draw(Canvas canvas, Camera camera);
    public void hide(boolean isHide);
    public float getX();
    public float getY();
    public float getCenterX();
    public float getCenterY();
    public int getWidth();
    public int getHeight();
    public void transform(float dx, float dy);
    public RectF getRect();
    public void onDraw(Canvas canvas);
    public void setSize(int width, int height);
    int getLayer();
    void setLayer(int layer);

    void setRotation(float rotation);
}
