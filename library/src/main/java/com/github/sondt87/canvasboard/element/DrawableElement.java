package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;


public class DrawableElement extends BaseElement {
    private static final String TAG = "";
    Drawable drawable;

    RectF ninePatchRect = new RectF();
    boolean isNinePatch = false;

    public DrawableElement(float x, float y, Drawable drawable) {
        this(x, y, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable);
    }

    public DrawableElement(float x, float y, int width, int height, Drawable drawable) {
        super(x, y, width, height);
        this.drawable = drawable;
        loadBounds();
    }

    private void loadBounds() {

        if(drawable instanceof NinePatchDrawable){
            isNinePatch = true;
            NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) drawable;
            Rect padding = new Rect();
            ninePatchDrawable.getPadding(padding);

            Rect npdBounds = new Rect((int) getX() - padding.left, (int)getY() - padding.top, (int)getX() + getWidth() + padding.right, (int)getY() +getHeight() + padding.bottom);
            drawable.setBounds(npdBounds);
            ninePatchRect.set(npdBounds);
        }else {
            Rect npdBounds = new Rect((int) getX(), (int)getY(), (int)(getX() + getWidth()), (int)(getY() + getHeight()));
            getRect().roundOut(npdBounds);
            drawable.setBounds(npdBounds);
        }
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        loadBounds();
    }

    @Override
    public void transform(float dx, float dy) {
        super.transform(dx, dy);
        Rect bounds = new Rect();
        if(isNinePatch) {
            ninePatchRect.offset(dx, dy);
            ninePatchRect.roundOut(bounds);
        }else{
            getRect().roundOut(bounds);
        }
        drawable.setBounds(bounds);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawable.draw(canvas);
    }
}
