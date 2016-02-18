package jian.zhang.oceanwithlibrarys;


import java.util.List;

import jian.zhang.oceanwithlibrarys.domainobjects.Station;
import retrofit2.http.GET;
import rx.Observable;

public interface OceanAPI {
    @GET("OceanData.json")
    Observable<List<Station>> getStations();
}
