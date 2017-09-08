package com.github.sondt87.canvasboard.element;

import android.graphics.Canvas;

import com.github.sondt87.canvasboard.camera.Camera;

/**
 * Created by Administrator on 6/8/2017.
 */

public interface IBoard {
    void onDraw(Canvas canvas, Camera camera);
    void transform(float dx, float dy, Camera camera);
    void resize(int width, int i);
}
