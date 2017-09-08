package com.github.sondt87.canvasboard.element.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class ArrowElement extends ShapeElement {

    public enum Direction {RIGHT_2_LEFT, BOTTOM_2_TOP, LEFT_2_RIGHT, TOP_2_BOTTOM}

    private int distance;
    Direction direction;

//    public ArrowElement(float x, float y, Paint paint, Direction direction) {
//        super(x, y, paint);
//        this.direction = direction;
//    }

    public ArrowElement(float x, float y, int distance, Paint paint, Direction direction) {
        super(x, y, paint);
        this.direction = direction;
        this.distance = distance;
        updateWidthHeight(distance);
    }

    private void updateWidthHeight(int distance) {
        int width = 0;
        int height = 0;

        switch (direction){
            case RIGHT_2_LEFT:
                width = distance;
                break;
            case LEFT_2_RIGHT:
                width = distance;
                break;
            case TOP_2_BOTTOM:
                height = distance;
                break;
            case BOTTOM_2_TOP:
                height = distance;
                break;

        }

        setSize(width,height);
    }

    public void addDistance(int distance){
        this.distance += distance;
        updateWidthHeight( this.distance);
    }


    @Override
    public void onDraw(Canvas canvas) {
        drawArrow(getPaint(), canvas, getX(), getY(), getRect().right, getRect().bottom);
    }

    /**
     * Draw an arrow
     * change internal radius and angle to change appearance
     * - angle : angle in degrees of the arrows legs
     * - radius : length of the arrows legs
     *
     * @param paint
     * @param canvas
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @author Steven Roelants 2017
     */

    private void drawArrow(Paint paint, Canvas canvas, float fromX, float fromY, float toX, float toY) {

        final float strokeWidth = paint.getStrokeWidth();
        final int arrowSize = (int) (strokeWidth * 1.5);// (int) (40 );
        final int arrowStrokeWidth = (int) (strokeWidth *1.2f);
        final int offset = arrowSize / 2;

        Path path = new Path();
        if (direction == Direction.LEFT_2_RIGHT) {
            canvas.drawLine(fromX, fromY, toX - offset, toY, paint);
            leftToRightArrow(toX, toY, arrowSize, arrowStrokeWidth, path);
        }else if(direction == Direction.RIGHT_2_LEFT){
            canvas.drawLine(fromX + offset, fromY, toX, toY, paint);
            path.moveTo(fromX + arrowSize + arrowStrokeWidth, fromY - arrowSize);
            path.lineTo(fromX + arrowSize, fromY - arrowSize);
            path.lineTo(fromX, fromY);
            path.lineTo(fromX + arrowSize , fromY + arrowSize);
            path.lineTo(fromX + arrowSize + arrowStrokeWidth, fromY + arrowSize);
            path.lineTo(fromX + arrowStrokeWidth, toY);
            path.lineTo(fromX + arrowSize + arrowStrokeWidth, fromY - arrowSize);
        }else if(direction == Direction.BOTTOM_2_TOP){
            canvas.drawLine(fromX, fromY + offset, toX, toY, paint);
            path.moveTo(fromX - arrowSize , fromY + arrowSize + arrowStrokeWidth);
            path.lineTo(fromX - arrowSize , fromY + arrowSize);
            path.lineTo(fromX , fromY );
            path.lineTo(fromX + arrowSize, fromY + arrowSize);
            path.lineTo(fromX + arrowSize , fromY + arrowSize + arrowStrokeWidth);
            path.lineTo(fromX , fromY + strokeWidth);
            path.lineTo(fromX - arrowSize , fromY + arrowSize + arrowStrokeWidth);
        }else if(direction == Direction.TOP_2_BOTTOM){
            canvas.drawLine(fromX, fromY , toX, toY - offset, paint);
            path.moveTo(toX - arrowSize , toY - arrowSize - arrowStrokeWidth);
            path.lineTo(toX - arrowSize , toY - arrowSize);
            path.lineTo(toX , toY );
            path.lineTo(toX + arrowSize, toY - arrowSize);
            path.lineTo(toX + arrowSize , toY - arrowSize - arrowStrokeWidth);
            path.lineTo(toX , toY - strokeWidth);
            path.lineTo(toX - arrowSize , toY - arrowSize - arrowStrokeWidth);
        }


        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);

    }

    private void leftToRightArrow(float toX, float toY, int arrowSize, int arrowStrokeWidth, Path path) {
        path.moveTo(toX - arrowSize - arrowStrokeWidth, toY - arrowSize);
        path.lineTo(toX - arrowSize , toY - arrowSize);
        path.lineTo(toX , toY);
        path.lineTo(toX - arrowSize , toY + arrowSize);
        path.lineTo(toX - arrowSize - arrowStrokeWidth, toY + arrowSize);
        path.lineTo(toX - arrowStrokeWidth, toY);
        path.lineTo(toX - arrowSize - arrowStrokeWidth, toY - arrowSize);
    }
}
