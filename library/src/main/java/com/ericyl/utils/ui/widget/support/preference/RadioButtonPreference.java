package com.ericyl.utils.ui.widget.support.preference;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

import com.ericyl.utils.R;

public class RadioButtonPreference
        extends CheckBoxPreference {
    private String value;

    public RadioButtonPreference(Context paramContext) {
        super(paramContext);
        init();
    }

    public RadioButtonPreference(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public RadioButtonPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        setWidgetLayoutResource(R.layout.layout_preference_radio_button);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}