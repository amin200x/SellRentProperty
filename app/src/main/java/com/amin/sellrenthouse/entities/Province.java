package com.amin.sellrenthouse.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Province implements Parcelable {
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
    public Province(int id, String name){
        this.id = id;
        this.name = name;

    }

    protected Province(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
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
