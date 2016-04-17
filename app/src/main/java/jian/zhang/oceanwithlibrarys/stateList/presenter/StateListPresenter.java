package jian.zhang.oceanwithlibrarys.stateList.presenter;


import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;

import jian.zhang.oceanwithlibrarys.base2.MyBasePresenter;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.global.OceanApplication;
import jian.zhang.oceanwithlibrarys.manager.StationManager;
import jian.zhang.oceanwithlibrarys.stateList.view.StateListView;

public class StateListPresenter extends MyBasePresenter<StateListView>{

    @Inject
    StationManager mStationManager;

    @Inject
    public StateListPresenter() {
        OceanApplication.app().getOceanComponent().inject(this);
    }

    @Override
    public void attachView(StateListView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void loadData() {
        new LoadDatabaseTask().execute();
    }

    // Load the data use a task
    private class LoadDatabaseTask extends AsyncTask<Void, Void, List<Station>> {  //把这个改成rxJava怎么弄?

        @Override
        protected void onPreExecute() {
            getView().showProgress(true);
        }

        @Override
        protected List<Station> doInBackground(Void... params) {
            return mStationManager.getStationsGroupByState();
        }

        @Override
        protected void onPostExecute(List<Station> stations) {
            super.onPostExecute(stations);
            getView().showProgress(false);
            getView().updateUI(stations);
        }
    }
}
