package com.github.sondt87.canvasboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.OverScroller;

import com.github.sondt87.canvasboard.camera.Camera;
import com.github.sondt87.canvasboard.element.Element;
import com.github.sondt87.canvasboard.element.Board;

import java.util.ArrayList;
import java.util.List;


public class BoardController implements View.OnTouchListener, OnGestureListener {
    private static final String TAG = BoardController.class.getName();
    private static float DEFAULT_MAX_SCALE = 3.0f;
    private static float DEFAULT_MID_SCALE = 1.75f;
    private static float DEFAULT_MIN_SCALE = 1.0f;
    private static int DEFAULT_ZOOM_DURATION = 200;

    private static final int EDGE_NONE = -1;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_RIGHT = 1;
    private static final int EDGE_BOTH = 2;
    private static int SINGLE_TOUCH = 1;
    private final Board mBoard;

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mZoomDuration = DEFAULT_ZOOM_DURATION;
    private float mMinScale = DEFAULT_MIN_SCALE;
    private float mMidScale = DEFAULT_MID_SCALE;
    private float mMaxScale = DEFAULT_MAX_SCALE;

    private final View mView;
    private final List<Element> elements = new ArrayList<Element>();
    private final CustomGestureDetector mScaleDragDetector;

    // These are set so we don't keep allocating them on the heap
    private final float[] mMatrixValues = new float[9];


    private FlingRunnable mCurrentFlingRunnable;
    private int mScrollEdge = EDGE_BOTH;
    private boolean mAllowParentInterceptOnEdge = true;
    private boolean mBlockParentIntercept = false;

    private boolean mZoomEnabled = true;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

    private Camera camera;
    private float mScale = DEFAULT_MIN_SCALE;

    Paint cameraPaint = new Paint();

    public interface IElementOnClickListener {
        public void onElementClicked(Element element);
    }

    IElementOnClickListener onElementClickListener;

    public BoardController(View view, Board board) {
        this.mView = view;
        this.mBoard = board;

        mScaleDragDetector = new CustomGestureDetector(mView.getContext(), this);

        camera = new Camera(view.getWidth(), view.getHeight());

        cameraPaint.setColor(Color.parseColor("#4400ff00"));
        cameraPaint.setStrokeWidth(10);
        Shader shader = new LinearGradient(0, 0, 0, 40, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
        cameraPaint.setShader(shader);
    }


    public void setOnElementClickListener(IElementOnClickListener onElementClickListener) {
        this.onElementClickListener = onElementClickListener;
    }

    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScale, mScale);
        mBoard.onDraw(canvas, camera);
        canvas.restore();
    }

    @Override
    public void onDrag(float dx, float dy) {
        if (mScaleDragDetector.isScaling()) {
            return; // Do not drag if we are already scaling
        }
        //recalculate base on scale value
        dx = dx / getScale();
        dy = dy / getScale();

        final float transformX = mBoard.getTransformX() + dx;
        final float transformY = mBoard.getTransformY() + dy;

        if (transformX > 0) {
            dx = 0 - mBoard.getTransformX();
        } else if (Math.abs(transformX) > (mBoard.getWidth() - camera.getZoomWidth())) {
            dx = -(mBoard.getWidth() - camera.getZoomWidth()) + Math.abs(mBoard.getTransformX());
        }


        if (transformY > 0) {
            //in case drag from top to bottom
            dy = 0 - mBoard.getTransformY();
//        }else if( Math.abs(transformY) > (mBoard.getHeight() - camera.getRect().height() /getScale())){
//            dy = -(mBoard.getHeight() - camera.getRect().height()/getScale() ) + Math.abs( mBoard.getTransformY());
        } else if (Math.abs(transformY) > (mBoard.getHeight() - camera.getZoomHeight())) {
            dy = -(mBoard.getHeight() - camera.getZoomHeight()) + Math.abs(mBoard.getTransformY());
        }

        mBoard.transform(dx, dy, camera);

        mView.invalidate();

        /*
         * Here we decide whether to let the ImageView's parent to start taking
         * over the touch event.
         *
         * First we check whether this function is enabled. We never want the
         * parent to take over if we're scaling. We then check the edge we're
         * on, and the direction of the scroll (i.e. if we're pulling against
         * the edge, aka 'overscrolling', let the parent take over).
         */
        ViewParent parent = mView.getParent();
        if (mAllowParentInterceptOnEdge && !mScaleDragDetector.isScaling() && !mBlockParentIntercept) {
            if (mScrollEdge == EDGE_BOTH
                    || (mScrollEdge == EDGE_LEFT && dx >= 1f)
                    || (mScrollEdge == EDGE_RIGHT && dx <= -1f)) {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            }
        } else {
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    @Override
    public void onFling(float startX, float startY, float velocityX, float velocityY) {
    }

    @Override
    public void onScale(float scaleFactor, float focusX, float focusY) {
        scale(mScale * scaleFactor);
    }

    private void scale(float scaleFactor) {

        if (scaleFactor < mMinScale || scaleFactor > mMaxScale) return;

        camera.zoom(1 / scaleFactor);

        float transformX = Math.abs(mBoard.getTransformX());
        float transformY = Math.abs(mBoard.getTransformY());
        float deltaX = mBoard.getWidth() - camera.getZoomWidth();
        float deltaY = mBoard.getHeight() - camera.getZoomHeight();
        float dx = 0;
        float dy = 0;

        if (transformX > deltaX) {
            dx = Math.abs(transformX) - deltaX;
        }
//
        if (transformY > deltaY) {
            dy = Math.abs(transformY) - deltaY;
        }
        mBoard.transform(dx, dy, camera);
        mScale = scaleFactor;
        mView.invalidate();
    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @return RectF - Displayed Rectangle
     */
    private RectF getDisplayRect() {
        return camera.getRect();
    }

    float[] downPosition;
    final float MOVE_THRESHOLD = 10;
    Element lastActionDownElement = null;

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        boolean handled = false;
        if (mZoomEnabled) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ViewParent parent = v.getParent();
                    // First, disable the Parent from intercepting the touch
                    // event
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    // If we're flinging, and the user presses down, cancel
                    // fling
                    cancelFling();

                    lastActionDownElement = mBoard.hasTouchedItem(ev.getX() * camera.getZoom(), ev.getY() * camera.getZoom());

                    if (ev.getPointerCount() > 1) {
                        downPosition = null;
                        lastActionDownElement = null;
                    } else {
                        downPosition = new float[]{ev.getX(), ev.getY()};
                    }

                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // If the user has zoomed less than min scale, zoom back
                    // to min scale
                    if (getScale() < mMinScale) {
                        RectF rect = getDisplayRect();
                        if (rect != null) {
                            v.post(new AnimatedZoomRunnable(getScale(), mMinScale,
                                    rect.centerX(), rect.centerY()));
                            handled = true;
                        }
                    }

                    if (downPosition != null) {
                        if (Math.abs(downPosition[0] - ev.getX()) <= MOVE_THRESHOLD
                                && Math.abs(downPosition[1] - ev.getY()) <= MOVE_THRESHOLD) {
                            if (onElementClickListener != null)
                                onElementClickListener.onElementClicked(lastActionDownElement);
                        }
                    }
                    break;
            }

            // Try the Scale/Drag detector
            if (mScaleDragDetector != null) {
                boolean wasScaling = mScaleDragDetector.isScaling();
                boolean wasDragging = mScaleDragDetector.isDragging();

                handled = mScaleDragDetector.onTouchEvent(ev);

                boolean didntScale = !wasScaling && !mScaleDragDetector.isScaling();
                boolean didntDrag = !wasDragging && !mScaleDragDetector.isDragging();

                mBlockParentIntercept = didntScale && didntDrag;
            }

        }

        return handled;
    }

    public float getScale() {
        return mScale;
    }

    /**
     * Helper method that 'unpacks' a Matrix and returns the required value
     *
     * @param matrix     Matrix to unpack
     * @param whichValue Which value from Matrix.M* to return
     * @return returned value
     */
    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    private void cancelFling() {
        if (mCurrentFlingRunnable != null) {
            mCurrentFlingRunnable.cancelFling();
            mCurrentFlingRunnable = null;
        }
    }

    public void setCamera(int width, int height) {
        camera.setSize(width, height);
        float maxScale = Math.max((float) height / mBoard.getHeight(), (float) width / mBoard.getWidth());

        if (maxScale > 1) {
            scale(maxScale);
            mMinScale = maxScale;
            mMaxScale += maxScale;
        }else {
            scale(1);
        }
    }

    private class AnimatedZoomRunnable implements Runnable {

        private final float mFocalX, mFocalY;
        private final long mStartTime;
        private final float mZoomStart, mZoomEnd;

        public AnimatedZoomRunnable(final float currentZoom, final float targetZoom,
                                    final float focalX, final float focalY) {
            mFocalX = focalX;
            mFocalY = focalY;
            mStartTime = System.currentTimeMillis();
            mZoomStart = currentZoom;
            mZoomEnd = targetZoom;
        }

        @Override
        public void run() {

            float t = interpolate();
            float scale = mZoomStart + t * (mZoomEnd - mZoomStart);
            float deltaScale = scale / getScale();

            onScale(deltaScale, mFocalX, mFocalY);

            // We haven't hit our target scale yet, so post ourselves again
            if (t < 1f) {
                Compat.postOnAnimation(mView, this);
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration;
            t = Math.min(1f, t);
            t = mInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class FlingRunnable implements Runnable {

        private final OverScroller mScroller;
        private int mCurrentX, mCurrentY;

        public FlingRunnable(Context context) {
            mScroller = new OverScroller(context);
        }

        public void cancelFling() {
            mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX,
                          int velocityY) {
            final RectF rect = getDisplayRect();
            if (rect == null) {
                return;
            }

            final int startX = Math.round(-rect.left);
            final int minX, maxX, minY, maxY;

            if (viewWidth < rect.width()) {
                minX = 0;
                maxX = Math.round(rect.width() - viewWidth);
            } else {
                minX = maxX = startX;
            }

            final int startY = Math.round(-rect.top);
            if (viewHeight < rect.height()) {
                minY = 0;
                maxY = Math.round(rect.height() - viewHeight);
            } else {
                minY = maxY = startY;
            }

            mCurrentX = startX;
            mCurrentY = startY;

            // If we actually can move, fling the scroller
            if (startX != maxX || startY != maxY) {
                mScroller.fling(startX, startY, velocityX, velocityY, minX,
                        maxX, minY, maxY, 0, 0);
            }
        }

        @Override
        public void run() {
            if (mScroller.isFinished()) {
                return; // remaining post that should not be handled
            }

            if (mScroller.computeScrollOffset()) {

                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();

//                mSuppMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);

                mCurrentX = newX;
                mCurrentY = newY;

                // Post On animation
                Compat.postOnAnimation(mView, this);
            }
        }
    }

}
