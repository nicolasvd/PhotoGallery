package be.ibad.photogallery.model;


import android.os.Parcel;
import android.os.Parcelable;


public class Record implements Parcelable {

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
    private String datasetid;
    private String recordid;
    private ComicWall fields;
    private Geometry geometry;

    private Record(Parcel in) {
        this.datasetid = in.readString();
        this.recordid = in.readString();
        this.fields = in.readParcelable(ComicWall.class.getClassLoader());
        this.geometry = in.readParcelable(Geometry.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        return !(recordid != null ? !recordid.equals(record.recordid) : record.recordid != null);

    }

    @Override
    public int hashCode() {
        return recordid != null ? recordid.hashCode() : 0;
    }

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public ComicWall getFields() {
        return fields;
    }

    public void setFields(ComicWall fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "{" + fields + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.datasetid);
        dest.writeString(this.recordid);
        dest.writeParcelable(this.fields, 0);
        dest.writeParcelable(this.geometry, 0);
    }


}
