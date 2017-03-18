package com.ericyl.utils.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ericyl.utils.R;


public class ActionBadgeLayout extends FrameLayout {

    @DrawableRes
    private int icon;
    @ColorRes
    private int badgeColor;

    private ImageView imgIcon;
    private TextView tvBadge;

    public ActionBadgeLayout(Context context) {
        super(context);
    }

    public ActionBadgeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.defaultStyle_ActionBadgeLayout);
    }

    public ActionBadgeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBadgeLayout, defStyleAttr, R.style.defaultStyle_ActionBadgeLayout);
        try {
            icon = a.getResourceId(R.styleable.ActionBadgeLayout_icon, -1);
            badgeColor = a.getResourceId(R.styleable.ActionBadgeLayout_badgeColor, R.attr.colorPrimary);
        } finally {
            a.recycle();
        }
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_action_badge, this);
        imgIcon = (ImageView) findViewById(R.id.img_icon);
        tvBadge = (TextView) findViewById(R.id.tv_badge);
        tvBadge.setVisibility(GONE);
        if (icon != -1)
            imgIcon.setImageResource(icon);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.tv_badge_rectangle);
        DrawableCompat.setTintList(drawable, ContextCompat.getColorStateList(context, badgeColor));
        tvBadge.setBackgroundDrawable(drawable);
    }

    public void setText(String badge) {
        if (TextUtils.isEmpty(badge)) {
            tvBadge.setVisibility(GONE);
            return;
        }
        tvBadge.setVisibility(VISIBLE);
        try {
            int value = Integer.valueOf(badge);
            if (value > 99)
                badge = "99+";
        } catch (Exception ignored) {
        }
        tvBadge.setText(badge);
    }

}
