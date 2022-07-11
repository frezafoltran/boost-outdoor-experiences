package com.foltran.feature_profile.feed.presentation.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;


public class SquareConstraintLayout extends ConstraintLayout {


    public SquareConstraintLayout(Context context) {
        super(context);
    }

    public SquareConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }


}

