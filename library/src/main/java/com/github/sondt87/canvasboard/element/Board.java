package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.sondt87.canvasboard.camera.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Board implements IBoard {

    private static final String TAG = Board.class.getName();
    private static final int DEFAULT_BG_COLOR = Color.parseColor("#E0ECF0");
    private int width, height;
    Paint backgroundPaint = new Paint();
    private final List<Element> elements = new ArrayList<Element>();
    private final List<Element> touchableElements = new ArrayList<Element>();

    private float transformX, transformY;

    int backgroundColor = DEFAULT_BG_COLOR;

    public Board(int width, int height) {
        backgroundPaint.setColor(backgroundColor);
        this.width = width;
        this.height = height;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
    }

    public void addElement(Element element) {
        addElement(element, false);
    }

    public void addElement(Element element, boolean touchable) {
        elements.add(element);
        if (touchable)
            touchableElements.add(element);
        sortElementByLayer();
    }

    Comparator<? super Element> coparator = new Comparator<Element>() {
        @Override
        public int compare(Element o1, Element o2) {
            return o1.getLayer() - o2.getLayer();
        }
    };

    private void sortElementByLayer() {
        Collections.sort(elements, coparator);
    }

    @Override
    public void onDraw(Canvas canvas, Camera camera) {
        canvas.drawRect(transformX, transformY, transformX + width, transformY + height, backgroundPaint);

        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.draw(canvas, camera);
        }
    }

    public float getTransformX() {
        return transformX;
    }

    public float getTransformY() {
        return transformY;
    }

    @Override
    public void transform(float dx, float dy, Camera camera) {
        this.transformX += dx;
        this.transformY += dy;

        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.transform(dx, dy);
        }
    }

    public Element hasTouchedItem(float x, float y) {
        for (int i = touchableElements.size() - 1; i >= 0; i--) {
            Element element = touchableElements.get(i);
            if (element.getRect().contains(x, y))
                return element;
        }
        return null;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
