package jian.zhang.oceanwithlibrarys.global;


import java.util.List;

import jian.zhang.oceanwithlibrarys.domainobjects.Station;
import jian.zhang.oceanwithlibrarys.stationDetail.model.TideType;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface OceanAPI {
    //@GET("/OceanData.json")   // this one is for OCEAN_CANDY_BASE_URL2
    @GET("/stations")
    Observable<List<Station>> getStations();

    @GET("/stations/{id}")
    Call<TideType> getTides(@Path("id") String id);
}
