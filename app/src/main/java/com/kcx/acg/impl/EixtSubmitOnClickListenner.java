package com.kcx.acg.impl;

import android.os.Parcel;
import android.os.Parcelable;


public abstract class EixtSubmitOnClickListenner implements Parcelable {
    public abstract void onEixtSubmitOnClick();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EixtSubmitOnClickListenner() {
    }

    protected EixtSubmitOnClickListenner(Parcel in) {
    }

    public static final Creator<EixtSubmitOnClickListenner> CREATOR = new Creator<EixtSubmitOnClickListenner>() {
        @Override
        public EixtSubmitOnClickListenner createFromParcel(Parcel source) {
            return new EixtSubmitOnClickListenner(source){

                @Override
                public void onEixtSubmitOnClick() {

                }
            };
        }

        @Override
        public EixtSubmitOnClickListenner[] newArray(int size) {
            return new EixtSubmitOnClickListenner[size];
        }
    };
}
