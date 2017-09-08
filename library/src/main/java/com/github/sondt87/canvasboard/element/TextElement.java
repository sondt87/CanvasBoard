package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;
import android.graphics.Paint;


public class TextElement extends BaseElement {

    int color;
    float fontSize;
    Paint.Align align = Paint.Align.LEFT;
    Paint paint;
    private String[] lines;
    float lineSpacing = 4;


    public TextElement(float x, float y, int color, float fontSize, Paint.Align align, Paint paint, String... lines) {
        super(x, y);
        this.lines = lines;
        this.color = color;
        this.fontSize = fontSize;
        this.align = align;
        this.paint = paint;
        initial();
    }

    public TextElement(float x, float y, int width, int height, int color, float fontSize, Paint.Align align, Paint paint, String... lines) {
        super(x, y, width, height);
        this.lines = lines;
        this.color = color;
        this.fontSize = fontSize;
        this.align = align;
        this.paint = paint;
        initial();
    }

    public void initial() {
        if (paint == null)
            paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(fontSize);
        paint.setColor(color);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setLineSpacing(float lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    @Override
    public void onDraw(Canvas canvas) {

        final float startY = getY() + (height - (lines.length - 1) * lineSpacing - lines.length * paint.getTextSize()) / 2 + paint.getTextSize();

        //this case need to calculate x,y to draw text
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];

            final float y = startY + i * lineSpacing + i * paint.getTextSize();

            final float lineWidth = paint.measureText(line);

            if (align == Paint.Align.CENTER) {
                canvas.drawText(line, getX() + (width - lineWidth) / 2, y, paint);
            } else if (align == Paint.Align.RIGHT) {
                canvas.drawText(line, getX() + width - lineWidth, y, paint);
            } else if (align == Paint.Align.LEFT) {
                canvas.drawText(line, getX(), y, paint);
            }
        }
    }
}
