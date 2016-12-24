/*
 * Copyright (C) 2011 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ericyl.utils.ui.widget.support.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.ericyl.utils.R;



/*
 * @author Danesh
 * @author nebkat
 * @author ericyl update
 */

public class NumberPickerPreference extends DialogPreference {
    private int min, max, defaultValue;
    private boolean textEnable;
    private String textDigits;

    private String maxExternalKey, minExternalKey;

    private NumberPicker mNumberPicker;

    private static final int MIN_VALUE = 1;
    private static final int DEFAULT_VALUE = 1;
    private static final int MAX_VALUE = 1;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.defaultStyle_NumberPickerPreference);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
//        com.android.internal.R.styleable.DialogPreference
//        int id = Resources.getSystem().getIdentifier("DialogPreference", "attr", "android");
//
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                new int[]{android.R.attr.dialogPreferenceStyle}, 0, 0);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NumberPickerPreference, defStyleAttr, R.style.defaultStyle_NumberPickerPreference);
        try {
            maxExternalKey = a.getString(R.styleable.NumberPickerPreference_maxExternal);
            minExternalKey = a.getString(R.styleable.NumberPickerPreference_minExternal);
            max = a.getInt(R.styleable.NumberPickerPreference_max, MAX_VALUE);
            min = a.getInt(R.styleable.NumberPickerPreference_min, MIN_VALUE);
            textEnable = a.getBoolean(R.styleable.NumberPickerPreference_textEnable, false);
            textDigits = a.getString(R.styleable.NumberPickerPreference_textDigits);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if (restorePersistedValue) {
            this.defaultValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            this.defaultValue = (Integer) defaultValue;
            persistInt(this.defaultValue);
        }
    }

    @Override
    protected View onCreateDialogView() {
        int max = this.max;
        int min = this.min;
//      External values
        if (maxExternalKey != null)
            max = getSharedPreferences().getInt(maxExternalKey, max);
        if (minExternalKey != null)
            min = getSharedPreferences().getInt(minExternalKey, min);

        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_number_picker_dialog, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

//      Initialize state
        mNumberPicker.setMaxValue(max);
        mNumberPicker.setMinValue(min);
        mNumberPicker.setValue(getPersistedInt(defaultValue));
        mNumberPicker.setWrapSelectorWheel(false);

//      No keyboard popup
//      com.android.internal.R.id.numberpicker_input
        EditText textInput = (EditText) mNumberPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
        textInput.setEnabled(true);
        if (textDigits != null)
            textInput.setKeyListener(DigitsKeyListener.getInstance(textDigits));
        textInput.setCursorVisible(false);
        textInput.setFocusable(textEnable);
        textInput.setFocusableInTouchMode(textEnable);

        return view;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mNumberPicker.clearFocus();
            persistInt(mNumberPicker.getValue());
        }
    }

}