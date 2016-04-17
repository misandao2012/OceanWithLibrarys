package jian.zhang.oceanwithlibrarys.stationList.view;


import android.support.v4.content.Loader;

import java.util.List;

import jian.zhang.oceanwithlibrarys.base2.MvpView;
import jian.zhang.oceanwithlibrarys.database.Station;

public interface StationListView extends MvpView { //接口也是可以继承的

    void setupRecyclerViewAdapter(List<Station> stationList);
    void setAdapterNull();
    Loader<List<Station>> createLoader();
}
