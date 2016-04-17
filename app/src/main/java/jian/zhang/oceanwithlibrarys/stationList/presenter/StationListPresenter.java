package jian.zhang.oceanwithlibrarys.stationList.presenter;


import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

import jian.zhang.oceanwithlibrarys.base2.MyBasePresenter;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.stationList.view.StationListFragment;
import jian.zhang.oceanwithlibrarys.stationList.view.StationListView;

public class StationListPresenter extends MyBasePresenter<StationListView>{

    private StationListLoaderCallbacks mStationListLoaderCallbacks;
    private StationListFragment mFragment;


    public StationListPresenter(StationListFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void attachView(StationListView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void restartLoader() {
        mFragment.getLoaderManager().restartLoader(1, null, mStationListLoaderCallbacks);
    }

    public void initLoader() {
        mStationListLoaderCallbacks = new StationListLoaderCallbacks();
        mFragment.getLoaderManager().initLoader(1, null, mStationListLoaderCallbacks);
    }

    public class StationListLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Station>> {
        @Override
        public Loader<List<Station>> onCreateLoader(int id, Bundle bundle) {
            return getView().createLoader();
        }

        @Override
        public void onLoadFinished(Loader<List<Station>> loader, List<Station> stationList) {
            getView().setupRecyclerViewAdapter(stationList);
        }

        // 在Fragment的onDestroy时调用, 其实可以不用setAdapterNull, 本来是用来关掉cursor的, 这里无所谓
        @Override
        public void onLoaderReset(Loader<List<Station>> loader) {
            getView().setAdapterNull();
        }
    }

}
