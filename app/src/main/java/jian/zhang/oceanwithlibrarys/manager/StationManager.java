package jian.zhang.oceanwithlibrarys.manager;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

import jian.zhang.oceanwithlibrarys.constants.Constants;
import jian.zhang.oceanwithlibrarys.database.Station;

/**
 * Created by jian on 12/14/2015.
 */

/* Singleton Station Manager to wrap up database functions
* */
public class StationManager {

    public StationManager(Context context) {

    }

    public List<Station> getStationsGroupByState() {
        return new Select()
                .from(Station.class)
                .groupBy("stateName")
                .execute();
    }

    public List<Station> getStationsByState(final String stateName) {
        return new Select()
                .from(Station.class)
                .where("stateName = ?", stateName)
                .execute();
    }

    public List<Station> getStationsByFav() {
        return new Select()
                .from(Station.class)
                .where("favorite = ?", Constants.FAVORITE_TRUE)
                .execute();
    }

    public void clearStations(){
        new Delete().from(Station.class).execute();
    }

    public void updateFavByStation(Station station){
        new Update(Station.class)
                .set("favorite = ?", station.getFavorite())
                .where("stationId = ?", station.getStationId())   // 如果搜索Id会怎样呢?
                .execute();
    }
}
