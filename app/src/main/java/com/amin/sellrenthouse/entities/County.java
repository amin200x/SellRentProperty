package com.amin.sellrenthouse.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class County implements Parcelable {
    private int id;
    private String name;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public County(int id, String name){
        this.id = id;
        this.name = name;

    }

    protected County(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<County> CREATOR = new Creator<County>() {
        @Override
        public County createFromParcel(Parcel in) {
            return new County(in);
        }

        @Override
        public County[] newArray(int size) {
            return new County[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
