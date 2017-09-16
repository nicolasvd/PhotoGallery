package be.ibad.photogallery.model;

import java.util.ArrayList;

public class ResponseOpenData {

    private int nhits;
    private RequestParam parameters;
    private ArrayList<Record> records;
    private String geofilterdistance;
    private String errorcode;
    private String error;


    public int getNhits() {
        return nhits;
    }

    public void setNhits(int nhits) {
        this.nhits = nhits;
    }

    public RequestParam getParameters() {
        return parameters;
    }

    public void setParameters(RequestParam parameters) {
        this.parameters = parameters;
    }

    public String getGeofilterdistance() {
        return geofilterdistance;
    }

    public void setGeofilterdistance(String geofilterdistance) {
        this.geofilterdistance = geofilterdistance;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResponseOpenData{" +
                "nhits=" + nhits +
                ", parameters=" + parameters +
                ", records=" + records +
                ", geofilterdistance='" + geofilterdistance + '\'' +
                ", errorcode='" + errorcode + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
