package com.ericyl.utils.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.ericyl.utils.R;
import com.ericyl.utils.util.DisplayUtils;

public class CustomSearchView extends SearchView {

    private SearchAutoComplete searchAutoComplete;

    private ISearchViewController searchViewController;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};

    public CustomSearchView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.defaultStyle_CustomSearchView);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setSearchController(ISearchViewController searchViewController) {
        this.searchViewController = searchViewController;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSearchView, defStyleAttr, R.style.defaultStyle_CustomSearchView);

        try {
            float editTextSize = 16.0F;
            if (a.hasValue(R.styleable.CustomSearchView_custom_editTextSize))
                editTextSize = DisplayUtils.px2sp(context, a.getDimension(R.styleable.CustomSearchView_custom_editTextSize, DisplayUtils.sp2px(context, 16.0F, 0.0F)), 0.0F);

            final Drawable imgGo = a.getDrawable(R.styleable.CustomSearchView_custom_imgGo);

            int goPaddingLeft = (int) DisplayUtils.dp2px(context, 4.0F, 0.0F);
            if (a.hasValue(R.styleable.CustomSearchView_custom_goPaddingLeft))
                goPaddingLeft = (int) a.getDimension(R.styleable.CustomSearchView_custom_goPaddingLeft, DisplayUtils.dp2px(context, 4.0F, 0.0F));

            int goPaddingRight = (int) DisplayUtils.dp2px(context, 4.0F, 0.0F);
            if (a.hasValue(R.styleable.CustomSearchView_custom_goPaddingRight))
                goPaddingRight = (int) a.getDimension(R.styleable.CustomSearchView_custom_goPaddingRight, DisplayUtils.dp2px(context, 4.0F, 0.0F));

            int searchPlateBackground = Color.TRANSPARENT;
            if (a.hasValue(R.styleable.CustomSearchView_custom_searchPlateBackground))
                searchPlateBackground = a.getColor(R.styleable.CustomSearchView_custom_searchPlateBackground, Color.TRANSPARENT);

            int submitAreaBackground = Color.TRANSPARENT;
            if (a.hasValue(R.styleable.CustomSearchView_custom_submitAreaBackground))
                submitAreaBackground = a.getColor(R.styleable.CustomSearchView_custom_submitAreaBackground, Color.TRANSPARENT);

            searchAutoComplete = (SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchAutoComplete.setTextSize(editTextSize);

            setSubmitButtonEnabled(true);

            ImageView btnGo = (ImageView) findViewById(android.support.v7.appcompat.R.id.search_go_btn);
            if (imgGo == null)
                btnGo.setImageResource(R.drawable.ic_search_query);
            else
                btnGo.setImageDrawable(imgGo);

            btnGo.setPadding(goPaddingLeft, 0, goPaddingRight, 0);

            findViewById(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(searchPlateBackground);
            findViewById(android.support.v7.appcompat.R.id.submit_area).setBackgroundColor(submitAreaBackground);
        } finally {
            a.recycle();
        }
    }

    public void clearText() {
        searchAutoComplete.setText(null);
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


    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (searchViewController != null)
                    searchViewController.doClose();
                break;
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public interface ISearchViewController {
        void doClose();
    }
}
