package com.mandeep.profileui.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    Bitmap imageUrl;
    String uri;
    boolean isNew;
    boolean isVideo;

    public Image(Bitmap imageUrl, String uri, boolean isNew, boolean isVideo) {
        this.imageUrl = imageUrl;
        this.isNew = isNew;
        this.uri = uri;
        this.isVideo = isVideo;
    }

    protected Image(Parcel in) {
        imageUrl = in.readParcelable(Bitmap.class.getClassLoader());
        uri = in.readString();
        isNew = in.readByte() != 0;
        isVideo = in.readByte() != 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageUrl, flags);
        dest.writeString(uri);
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeByte((byte) (isVideo ? 1 : 0));
    }

    public Bitmap getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Bitmap imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageUrl=" + imageUrl +
                ", uri='" + uri + '\'' +
                ", isNew=" + isNew +
                ", isVideo=" + isVideo +
                '}';
    }
}
