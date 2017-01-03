package com.ericyl.example.model.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchInfo implements Parcelable {

    private int icon;
    private String title;


    public SearchInfo() {
    }

    public SearchInfo(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(title);

    }

    public static final Creator<SearchInfo> CREATOR = new Creator<SearchInfo>() {

        @Override
        public SearchInfo createFromParcel(Parcel source) {
            return new SearchInfo(source);
        }

        @Override
        public SearchInfo[] newArray(int size) {
            return new SearchInfo[0];
        }
    };

    public SearchInfo(Parcel source) {
        this.icon = source.readInt();
        this.title = source.readString();
    }

}
