package jian.zhang.oceanwithlibrarys.loader;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.global.OceanApplication;
import jian.zhang.oceanwithlibrarys.manager.StationManager;

/**
 * Created by jian on 12/19/2015.
 */
public class StationsByStateLoader extends DataLoader<List<Station>> {

    @Inject
    StationManager mStationManager;

    private String mStateName;

    public StationsByStateLoader(Context context, String stateName) {
        super(context);
        OceanApplication.app().getOceanComponent().inject(this);
        mStateName = stateName;
    }

    @Override
    public List<Station> loadInBackground() {
        if (mStateName.equals(getContext().getString(R.string.favorite_stations))) {
            return mStationManager.getStationsByFav();
        }
        return mStationManager.getStationsByState(mStateName);
    }
}
