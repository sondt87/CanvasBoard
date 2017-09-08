package com.github.sondt87.canvasboard.element.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 6/22/2017.
 */

public class CircleElement extends ShapeElement {
    private final int radius;
    private String metterQuantity = "";

    public CircleElement(float x, float y, Paint paint, int radius) {
        super(x, y, radius * 2, radius * 2, paint);
        this.radius = radius;

    }

    public CircleElement(float x, float y, Paint paint, int radius, Drawable drawable, String metterQuantity) {
        super(x, y, radius * 2, radius * 2, paint);
        this.radius = radius;
        this.metterQuantity = metterQuantity;

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(getCenterX(), getCenterY(), radius, getPaint());
    }
}
