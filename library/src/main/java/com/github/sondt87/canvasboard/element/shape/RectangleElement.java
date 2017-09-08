package com.github.sondt87.canvasboard.element.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Administrator on 6/12/2017.
 */

public class RectangleElement extends ShapeElement {
    public RectangleElement(float x, float y, Paint paint) {
        super(x, y, paint);
    }

    public RectangleElement(float x, float y, int width, int height, Paint paint) {
        super(x, y, width, height, paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(getRect(), getPaint());
    }
}
