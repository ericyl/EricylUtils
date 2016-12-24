package com.ericyl.utils.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ericyl.utils.R;
import com.ericyl.utils.util.ColorUtils;

import java.util.Random;

public class CircleTextView extends TextView {


    @ColorInt
    private int backgroundColor;
    private boolean isSingleText;

    private static final int[] COLOR_RES = new int[]{
            R.color.blue, R.color.orange, R.color.green, R.color.red, R.color.grey,
            R.color.purple, R.color.pink, R.color.indigo, R.color.teal, R.color.brown,
            R.color.amber, R.color.yellow, R.color.cyan, R.color.pink, R.color.lime
    };

    public CircleTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.defaultStyle_CircleTextView);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView, defStyleAttr, R.style.defaultStyle_CircleTextView);
        try {
            backgroundColor = a.getColor(R.styleable.CircleTextView_backgroundColor, Color.TRANSPARENT);
            isSingleText = a.getBoolean(R.styleable.CircleTextView_singleText, false);
            if (isSingleText && !TextUtils.isEmpty(getText()))
                setText(getText().subSequence(0, 1).toString().toUpperCase());
        } finally {
            a.recycle();
        }
    }

    public void setSingleText(String text) {
        if (isSingleText && !TextUtils.isEmpty(text))
            setText(text.subSequence(0, 1).toString().toUpperCase());
    }

    public void setRandomBackgroundColor() {
        setBackgroundColor(ColorUtils.getRandomColor());
    }

    public void setRandomBackgroundColor(Context context) {
        setBackgroundColorRes(context, COLOR_RES[new Random().nextInt(COLOR_RES.length)]);
    }

    public void setBackgroundColorRes(Context context, @ColorRes int colorRes) {
        setBackgroundColor(ContextCompat.getColor(context, colorRes));
    }

    public void setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        invalidate();
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        float radius = Math.min(getHeight(), getWidth()) / 2;

        paint.setColor(backgroundColor);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        super.onDraw(canvas);
    }


}