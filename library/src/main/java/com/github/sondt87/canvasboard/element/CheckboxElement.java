package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;


public class CheckboxElement extends DrawableElement {
    Drawable checkedDrawable;
    boolean isChecked;
    DrawableElement checkedElement;

    public CheckboxElement(float x, float y, Drawable normal, Drawable checked, boolean isChecked) {
        super(x, y, normal);
        checkedElement = new DrawableElement(x,y, checked);
        this.checkedDrawable = checked;
        this.isChecked = isChecked;
    }

    public CheckboxElement(float x, float y, int width, int height, Drawable normal, Drawable checked, boolean isChecked) {
        super(x, y, width, height, normal);
        checkedElement = new DrawableElement(x,y, width,height,checked);
        this.checkedDrawable = checked;
        this.isChecked = isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        checkedElement.setSize(width, height);
    }

    @Override
    public void transform(float dx, float dy) {
        super.transform(dx, dy);
        checkedElement.transform(dx, dy);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(isChecked){
            checkedElement.onDraw(canvas);
        }else super.onDraw(canvas);

    }
}
