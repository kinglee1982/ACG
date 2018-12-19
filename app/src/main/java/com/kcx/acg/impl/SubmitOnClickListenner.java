package com.kcx.acg.impl;

import android.os.Parcel;
import android.os.Parcelable;


public abstract class SubmitOnClickListenner implements Parcelable {
    public abstract void onSubmitOnClick();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public SubmitOnClickListenner() {
    }

    protected SubmitOnClickListenner(Parcel in) {
    }

    public static final Creator<SubmitOnClickListenner> CREATOR = new Creator<SubmitOnClickListenner>() {
        @Override
        public SubmitOnClickListenner createFromParcel(Parcel source) {
            return new SubmitOnClickListenner(source){

                @Override
                public void onSubmitOnClick() {

                }
            };
        }

        @Override
        public SubmitOnClickListenner[] newArray(int size) {
            return new SubmitOnClickListenner[size];
        }
    };
}
