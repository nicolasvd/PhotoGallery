package be.ibad.photogallery.model;

import java.util.ArrayList;

public class RequestParam {

    private String lang;
    private int rows;
    private String format;
    private ArrayList<String> facet;
    private ArrayList<String> dataset;
    private String q;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ArrayList<String> getFacet() {
        return facet;
    }

    public void setFacet(ArrayList<String> facet) {
        this.facet = facet;
    }

    public ArrayList<String> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<String> dataset) {
        this.dataset = dataset;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
