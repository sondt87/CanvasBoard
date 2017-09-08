package com.github.sondt87.canvasboard.element.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Administrator on 6/12/2017.
 */

public class RoundElement extends ShapeElement {
    private final int radius;

    public RoundElement(float x, float y, int radius) {
        this(x,y,radius, new Paint());
    }

    public RoundElement(float x, float y, int radius, Paint paint) {
        super(x, y, (int)x + radius * 2, (int)y + radius * 2, paint);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRoundRect(getRect(), radius, radius, getPaint());
    }
}
