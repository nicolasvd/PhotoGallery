package be.ibad.photogallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Photo implements Parcelable {

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    @SerializedName("thumbnail")
    private boolean thumbnail;
    @SerializedName("filename")
    private String filename;
    @SerializedName("format")
    private String format;
    @SerializedName("width")
    private int width;
    @SerializedName("id")
    private String id;
    @SerializedName("height")
    private int height;

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.thumbnail = in.readByte() != 0;
        this.filename = in.readString();
        this.format = in.readString();
        this.width = in.readInt();
        this.id = in.readString();
        this.height = in.readInt();
    }

    public boolean isThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return
                "Photo{" +
                        "thumbnail = '" + thumbnail + '\'' +
                        ",filename = '" + filename + '\'' +
                        ",format = '" + format + '\'' +
                        ",width = '" + width + '\'' +
                        ",id = '" + id + '\'' +
                        ",height = '" + height + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.thumbnail ? (byte) 1 : (byte) 0);
        dest.writeString(this.filename);
        dest.writeString(this.format);
        dest.writeInt(this.width);
        dest.writeString(this.id);
        dest.writeInt(this.height);
    }
}