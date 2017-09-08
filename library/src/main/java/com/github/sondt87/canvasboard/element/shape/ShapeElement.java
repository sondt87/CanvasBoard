package com.github.sondt87.canvasboard.element.shape;

import android.graphics.Paint;

import com.github.sondt87.canvasboard.element.BaseElement;

/**
 * Created by Administrator on 6/12/2017.
 */

public abstract class ShapeElement extends BaseElement {
    private Paint paint;
    public ShapeElement(float x, float y, Paint paint) {
        super(x, y);
        this.paint = paint;
        paint.setAntiAlias(true);
    }

    public ShapeElement(float x, float y, int width, int height, Paint paint) {
        super(x, y, width, height);
        this.paint = paint;
        paint.setAntiAlias(true);
    }


    public Paint getPaint() {
        return paint;
    }
}
