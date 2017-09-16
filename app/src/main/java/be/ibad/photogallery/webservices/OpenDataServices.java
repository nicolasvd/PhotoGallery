package be.ibad.photogallery.webservices;


import be.ibad.photogallery.model.ResponseOpenData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface OpenDataServices {

    @GET("api/records/1.0/search?dataset=bruxelles_parcours_bd")
    Call<ResponseOpenData> getProximity(@Query("rows") int nbr, @Query("geofilter.distance") String geofilters);

    @GET("api/records/1.0/search?dataset=bruxelles_parcours_bd")
    Call<ResponseOpenData> getAll(@Query("rows") int nbr);

    @GET("api/records/1.0/search?dataset=bruxelles_parcours_bd")
    Call<ResponseOpenData> getQuery(@Query("q") String names);

    @GET("api/records/1.0/search?dataset=bruxelles_parcours_bd")
    Call<ResponseOpenData> getQuery(@Query("q") String names, @Query("rows") int nbr);
}
