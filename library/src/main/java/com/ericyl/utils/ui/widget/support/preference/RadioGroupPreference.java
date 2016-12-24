
package com.ericyl.utils.ui.widget.support.preference;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ericyl.utils.R;

public class RadioGroupPreference extends PreferenceCategory implements Preference.OnPreferenceClickListener {

    private String radioValue;
    private CharSequence[] radioEntries;
    private CharSequence[] radioEntryValues;
    private CharSequence[] radioEntrySummaries;

    private OnRadioButtonClickListener onRadioButtonClickListener;

    public void setOnRadioButtonClickListener(OnRadioButtonClickListener onRadioButtonClickListener) {
        this.onRadioButtonClickListener = onRadioButtonClickListener;
    }

    public RadioGroupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadioGroupPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context paramContext, AttributeSet paramAttributeSet) {
        TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RadioGroupPreference, 0, 0);
        try {
            radioEntries = a.getTextArray(R.styleable.RadioGroupPreference_radioEntries);
            radioEntryValues = a.getTextArray(R.styleable.RadioGroupPreference_radioEntryValues);
            radioEntrySummaries = a.getTextArray(R.styleable.RadioGroupPreference_radioEntrySummaries);
        } finally {
            a.recycle();
        }
    }

    public String getRadioValue() {
        return radioValue;
    }

    private void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
        persistString(radioValue);
    }

    public void setPreferenceEnable(String value, boolean enabled) {
        RadioButtonPreference radioButtonPreference = getRadioButtonPreference(value);
        radioButtonPreference.setEnabled(enabled);
    }


    public void setPreferenceSummary(String value, String summary) {
        RadioButtonPreference radioButtonPreference = getRadioButtonPreference(value);
        if (radioButtonPreference != null) {
            radioButtonPreference.setSummary(summary);
        }
    }

    private void setOtherPreferenceUnChecked(RadioButtonPreference paramRadioButtonPreference) {
        for (int i = 0; i < getPreferenceCount(); i++) {
            Preference preference = getPreference(i);
            if (!(preference instanceof RadioButtonPreference))
                return;
            RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preference;
            if (radioButtonPreference != paramRadioButtonPreference) {
                radioButtonPreference.setChecked(false);
            }
        }
    }

    public RadioButtonPreference getRadioButtonPreference(String value) {
        for (int i = 0; i < getPreferenceCount(); i++) {
            Preference preference = getPreference(i);
            if (!(preference instanceof RadioButtonPreference))
                return null;
            RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preference;
            if (radioButtonPreference.getValue().equals(value)) {
                return radioButtonPreference;
            }
        }
        return null;
    }

    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        if (radioEntries != null) {
            for (int i = 0; i < radioEntries.length; i++) {
                CharSequence localCharSequence = radioEntries[i];
                if (!TextUtils.isEmpty(localCharSequence)) {
                    RadioButtonPreference radioButtonPreference = new RadioButtonPreference(getContext());
                    radioButtonPreference.setTitle(localCharSequence);
                    radioButtonPreference.setPersistent(false);
                    if ((radioEntryValues != null) && (i < radioEntryValues.length)) {
                        localCharSequence = radioEntryValues[i];
                        if (!TextUtils.isEmpty(localCharSequence))
                            radioButtonPreference.setValue(localCharSequence.toString());
                        radioButtonPreference.setChecked(radioValue != null && radioValue.equals(localCharSequence));
                    }
                    if ((radioEntrySummaries != null) && (i < radioEntrySummaries.length)) {
                        localCharSequence = radioEntrySummaries[i];
                        if (!TextUtils.isEmpty(localCharSequence))
                            radioButtonPreference.setSummary(localCharSequence);
                    }
                    addPreference(radioButtonPreference);
                }
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (!(preference instanceof RadioButtonPreference))
            return false;
        boolean bool = true;
        RadioButtonPreference paramPreference = (RadioButtonPreference) preference;
        if (onRadioButtonClickListener != null)
            bool = onRadioButtonClickListener.onPreferenceClick(this, paramPreference);
        if (bool) {
            setOtherPreferenceUnChecked(paramPreference);
            paramPreference.setChecked(true);
            setRadioValue(paramPreference.getValue());
        }

        return bool;
    }


    @Override
    protected boolean onPrepareAddPreference(Preference preference) {
        boolean bool = false;
        if (preference instanceof RadioButtonPreference) {
            if (bool = super.onPrepareAddPreference(preference))
                preference.setOnPreferenceClickListener(this);
        }
        return bool;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        radioValue = myState.value;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        final SavedState myState = new SavedState(superState);
        myState.value = radioValue;
        return myState;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if (restorePersistedValue) {
            radioValue = getPersistedString(null);
        } else {
            radioValue = (String) defaultValue;
            persistString(radioValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    static class SavedState extends BaseSavedState {
        String value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    interface OnRadioButtonClickListener {
        boolean onPreferenceClick(RadioGroupPreference radioGroupPreference, RadioButtonPreference radioButtonPreference);
    }

}
