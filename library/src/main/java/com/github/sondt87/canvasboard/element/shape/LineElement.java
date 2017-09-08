package com.github.sondt87.canvasboard.element.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Administrator on 7/6/2017.
 */

public class LineElement extends ShapeElement {

    public LineElement(float startX, float startY, float endX, float endY, int strokeWidth, int color) {
        super(startX, startY, new Paint());

        setSize((int)(endX - startX), (int)(endY - startY));
        Paint paint = getPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(getX(), getY(), getRect().right, getRect().bottom, getPaint());
    }
}
