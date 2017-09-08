package com.github.sondt87.canvasboard.camera;

import android.graphics.RectF;

import com.github.sondt87.canvasboard.element.Element;

/**
 * Created by Administrator on 6/8/2017.
 */

public class Camera {
    private static final String TAG = Camera.class.getName();
    float x,y;
    int  width, height;
    private final RectF rect;
    private final RectF zoomRect;
    //this object store camera position
    private float zoom = 1;

    public Camera(int width, int height) {
        this.x = 0;
        this.y = 0;
        this.width = width;
        this.height = height;
        rect = new RectF(x, y, x + width, y + height);
        zoomRect = new RectF(x, y, x + width, y + height);
        init();
    }

    private void init(){
        zoom(1);
    }

    public boolean isIntersect(Element element){
        return RectF.intersects(rect, element.getRect());
    }

    public void zoom(float zoom){
        this.zoom = zoom;
        zoomRect.bottom = rect.bottom * zoom;
        zoomRect.right = rect.right * zoom;
    }

    public RectF getRect() {
        return rect;
    }

    public RectF getZoomRect() {
        return zoomRect;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getCenterX() {
        return rect.centerX();
    }

    public float getCenterY() {
        return rect.centerY();
    }

    public float getHeight() {
        return rect.height();
    }

    public float getWidth() {
        return rect.width();
    }
    public float getZoomWidth() {
        return zoomRect.width();
    }

    public float getZoomHeight() {
        return zoomRect.height();
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        refresh();
    }

    private void refresh() {
        rect.set(x, y, x + width, y + height);
    }

    public float getZoom() {
        return zoom;
    }

//    public void transform(float dx, float dy){
//        transformRect.offset(dx,dy);
//    }
}
