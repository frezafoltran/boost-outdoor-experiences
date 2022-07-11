package com.foltran.feature_profile.feed.presentation.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerViewZoomable extends RecyclerView {
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float maxWidth = 0.0f;
    private float maxHeight = 0.0f;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;
    private float width;
    private float height;


    public GridRecyclerViewZoomable(Context context) {
        super(context);
        if (!isInEditMode())
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public GridRecyclerViewZoomable(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public GridRecyclerViewZoomable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!isInEditMode())
            if (layout instanceof GridLayoutManager) {
                super.setLayoutManager(layout);
            } else {
                throw new ClassCastException("You should only use a GridLayoutManager with GridRecyclerView.");
            }
    }


    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        if (!isInEditMode())
            if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager) {

                GridLayoutAnimationController.AnimationParameters animationParams =
                        (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

                if (animationParams == null) {
                    animationParams = new GridLayoutAnimationController.AnimationParameters();
                    params.layoutAnimationParameters = animationParams;
                }

                int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

                animationParams.count = count;
                animationParams.index = index;
                animationParams.columnsCount = columns;
                animationParams.rowsCount = count / columns;

                final int invertedIndex = count - 1 - index;
                animationParams.column = columns - 1 - (invertedIndex % columns);
                animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;

            } else {
                super.attachLayoutAnimationParameters(child, params, index, count);
            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        final int action = ev.getAction();
        mScaleDetector.onTouchEvent(ev);
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                /* this line is replaced because here came below isssue
                java.lang.IllegalArgumentException: pointerIndex out of range
                 ref http://stackoverflow.com/questions/6919292/pointerindex-out-of-range-android-multitouch
                */
                //final int pointerIndex = ev.findPointerIndex(mActivePointerId);

                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                if (mPosX > 0.0f)
                    mPosX = 0.0f;
                else if (mPosX < maxWidth)
                    mPosX = maxWidth;

                if (mPosY > 0.0f)
                    mPosY = 0.0f;
                else if (mPosY < maxHeight)
                    mPosY = maxHeight;

                mLastTouchX = x;
                mLastTouchY = y;

                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        if (mScaleFactor == 1.0f) {
            mPosX = 0.0f;
            mPosY = 0.0f;
        }
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        super.dispatchDraw(canvas);
        canvas.restore();
        invalidate();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i("JVFF scaling ", " factor " + detector.getScaleFactor());

//            ProfileFeedAdapter adapter = (ProfileFeedAdapter) getAdapter();
//            if (adapter != null) {
//                adapter.setCurrentSpan(adapter.getCurrentSpan() == 50 ? 33 : 50);
//
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                }, 100);
//                //adapter.delayedNotify(adapter.i, adapter.calculateRange());
//            }
//            mScaleFactor *= detector.getScaleFactor();
//            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 3.0f));
//            maxWidth = width - (width * mScaleFactor);
//            maxHeight = height - (height * mScaleFactor);
//            invalidate();
            return true;
        }
    }
}
