package com.ericyl.utils.ui.widget.translucent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.ericyl.utils.R;
import com.ericyl.utils.util.DisplayUtils;
import com.ericyl.utils.util.OSInfoUtils;

/**
 * @hide
 */
public class TransNavigationView extends ScrimInsetsFrameLayout {

    private NavigationView navigationView;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};

    public TransNavigationView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TransNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TransNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_trans_navigation_view, this, true);
        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransNavigationView, defStyleAttr, 0);

        try {
            final ColorStateList itemIconTint;
            if (a.hasValue(R.styleable.TransNavigationView_transItemIconTint))
                itemIconTint = a.getColorStateList(R.styleable.TransNavigationView_transItemIconTint);
            else
                itemIconTint = createDefaultColorStateList(android.R.attr.textColorSecondary);

            final ColorStateList itemTextColor;
            if (a.hasValue(R.styleable.TransNavigationView_transItemTextColor))
                itemTextColor = a.getColorStateList(R.styleable.TransNavigationView_transItemTextColor);
            else
                itemTextColor = createDefaultColorStateList(android.R.attr.textColorPrimary);

            final Drawable itemBackground = a.getDrawable(R.styleable.TransNavigationView_transItemBackground);

            navigationView.setItemIconTintList(itemIconTint);
            navigationView.setItemTextColor(itemTextColor);
            navigationView.setItemBackground(itemBackground);

            if (a.hasValue(R.styleable.TransNavigationView_transHeaderLayout))
                navigationView.inflateHeaderView(a.getResourceId(R.styleable.TransNavigationView_transHeaderLayout, 0));

            if (a.hasValue(R.styleable.TransNavigationView_transMenu))
                navigationView.inflateMenu(a.getResourceId(R.styleable.TransNavigationView_transMenu, 0));

            if (OSInfoUtils.checkHasNavigationBar(context) && !DisplayUtils.isLandscapeOrientation(context))
                navigationView.setPadding(navigationView.getPaddingLeft(), navigationView.getPaddingTop(), navigationView.getPaddingRight(), navigationView.getPaddingBottom() + OSInfoUtils.getNavigationBarHeight(context));

        } finally {
            a.recycle();
        }

    }

    public View inflateHeaderView(@LayoutRes int res) {
        return navigationView.inflateHeaderView(res);
    }

    public void inflateMenu(int res) {
        navigationView.inflateMenu(res);
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        final TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = AppCompatResources.getColorStateList(
                getContext(), value.resourceId);
        if (!getContext().getTheme().resolveAttribute(
                android.support.v7.appcompat.R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{
                DISABLED_STATE_SET,
                CHECKED_STATE_SET,
                EMPTY_STATE_SET
        }, new int[]{
                baseColor.getColorForState(DISABLED_STATE_SET, defaultColor),
                colorPrimary,
                defaultColor
        });
    }

}
