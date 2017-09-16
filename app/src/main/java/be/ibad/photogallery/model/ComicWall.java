package be.ibad.photogallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ComicWall implements Parcelable {

    public static final Parcelable.Creator<ComicWall> CREATOR = new Parcelable.Creator<ComicWall>() {
        @Override
        public ComicWall createFromParcel(Parcel source) {
            return new ComicWall(source);
        }

        @Override
        public ComicWall[] newArray(int size) {
            return new ComicWall[size];
        }
    };
    @SerializedName("auteur_s")
    private String auteurS;
    @SerializedName("photo")
    private Photo photo;
    @SerializedName("personnage_s")
    private String personnageS;
    @SerializedName("annee")
    private String annee;
    @SerializedName("coordonnees_geographiques")
    private List<Double> coordonneesGeographiques;

    public ComicWall() {
    }

    protected ComicWall(Parcel in) {
        this.auteurS = in.readString();
        this.photo = in.readParcelable(Photo.class.getClassLoader());
        this.personnageS = in.readString();
        this.annee = in.readString();
        this.coordonneesGeographiques = new ArrayList<Double>();
        in.readList(this.coordonneesGeographiques, Double.class.getClassLoader());
    }

    public String getAuteurS() {
        return auteurS;
    }

    public void setAuteurS(String auteurS) {
        this.auteurS = auteurS;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getPersonnageS() {
        return personnageS;
    }

    public void setPersonnageS(String personnageS) {
        this.personnageS = personnageS;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public List<Double> getCoordonneesGeographiques() {
        return coordonneesGeographiques;
    }

    public void setCoordonneesGeographiques(List<Double> coordonneesGeographiques) {
        this.coordonneesGeographiques = coordonneesGeographiques;
    }

    @Override
    public String toString() {
        return
                "ComicWall{" +
                        "auteur_s = '" + auteurS + '\'' +
                        ",photo = '" + photo + '\'' +
                        ",personnage_s = '" + personnageS + '\'' +
                        ",annee = '" + annee + '\'' +
                        ",coordonnees_geographiques = '" + coordonneesGeographiques + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.auteurS);
        dest.writeParcelable(this.photo, flags);
        dest.writeString(this.personnageS);
        dest.writeString(this.annee);
        dest.writeList(this.coordonneesGeographiques);
    }
}