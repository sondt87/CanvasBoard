package com.github.sondt87.canvasboard.element;


import android.graphics.Canvas;
import android.graphics.RectF;

import com.github.sondt87.canvasboard.camera.Camera;

public abstract class BaseElement implements Element {
    float x,y,centerX, centerY;
    int width, height;
    private RectF rect;
    private boolean isHide;
    private int layer;
    private float rotation;

    public BaseElement(float x, float y) {
        this(x,y,0,0);
    }
    public BaseElement(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        updateProperties();
    }

    @Override
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
        updateProperties();
    }

    protected void updateProperties(){
        centerX = x + width/2f;
        centerY = y + height/2f;
        rect = new RectF(x, y, x + width, y + height);
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public RectF getRect() {
        return rect;
    }

    @Override
    public void hide(boolean isHide) {
        this.isHide = isHide;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getCenterX() {
        return rect.centerX();
    }

    @Override
    public float getCenterY() {
        return rect.centerY();
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void transform(float dx, float dy) {
        this.x+=dx;
        this.y+=dy;
        rect.offset(dx,dy);
    }

    public boolean isHide() {
        return isHide;
    }

    @Override
    public void draw(Canvas canvas, Camera camera) {
        if(isHide) return;
        if (camera.isIntersect(this)) {
            canvas.save();
            canvas.rotate(rotation, getCenterX(), getCenterY());
            onDraw(canvas);
            canvas.restore();
        }
    }
}
