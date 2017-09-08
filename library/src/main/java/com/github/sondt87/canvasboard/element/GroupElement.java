package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;

import com.github.sondt87.canvasboard.camera.Camera;

import java.util.ArrayList;
import java.util.List;


public abstract class GroupElement extends BaseElement {
    List<Element> elements = new ArrayList<Element>();

    public GroupElement(float x, float y) {
        super(x, y);
    }

    public GroupElement(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    public void addElement(Element element){
        elements.add(element);
    }

    public void removeElement(Element element){
        elements.remove(element);
    }

    @Override
    public void draw(Canvas canvas, Camera camera) {
        if(isHide()) return;
        if (camera.isIntersect(this)) {
            final int size = elements.size();
            for (int i = 0; i < size; i++) {
                Element element = elements.get(i);
                element.draw(canvas, camera);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    @Override
    public void transform(float dx, float dy) {
        super.transform(dx, dy);
        final int size = elements.size();
        for (int i = 0; i < size; i++) {
            Element element = elements.get(i);
            element.transform(dx,dy);
        }
    }
}
