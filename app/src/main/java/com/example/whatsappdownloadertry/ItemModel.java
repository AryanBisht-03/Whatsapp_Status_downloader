package com.example.whatsappdownloadertry;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

public class ItemModel implements Parcelable{
    Uri uri;
    File file;
    Boolean image;

    public ItemModel(){

    }

    public ItemModel(Uri uri,File file) {
        this.uri = uri;
        this.file = file;
    }

    protected ItemModel(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        byte tmpImage = in.readByte();
        image = tmpImage == 0 ? null : tmpImage == 1;
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(uri, i);
        parcel.writeByte((byte) (image == null ? 0 : image ? 1 : 2));
    }
}
