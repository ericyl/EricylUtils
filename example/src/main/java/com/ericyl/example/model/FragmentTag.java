package com.ericyl.example.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FragmentTag implements Parcelable {

    private CharSequence title;
    private String fragmentName;

    public FragmentTag() {
    }

    public FragmentTag(CharSequence title, String fragmentName) {
        this.title = title;
        this.fragmentName = fragmentName;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    protected FragmentTag(Parcel in) {
        this.title = in.readString();
        this.fragmentName = in.readString();
    }

    public static final Creator<FragmentTag> CREATOR = new Creator<FragmentTag>() {
        @Override
        public FragmentTag createFromParcel(Parcel in) {
            return new FragmentTag(in);
        }

        @Override
        public FragmentTag[] newArray(int size) {
            return new FragmentTag[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title.toString());
        parcel.writeString(fragmentName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FragmentTag)) return false;

        FragmentTag that = (FragmentTag) o;

        return title != null ? title.equals(that.title) : that.title == null && (fragmentName != null ? fragmentName.equals(that.fragmentName) : that.fragmentName == null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (fragmentName != null ? fragmentName.hashCode() : 0);
        return result;
    }
}
