package jian.zhang.oceanwithlibrarys.stationDetail.presenter;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jian.zhang.oceanwithlibrarys.base.BasePresenter;
import jian.zhang.oceanwithlibrarys.constants.Constants;
import jian.zhang.oceanwithlibrarys.domainobjects.Station;
import jian.zhang.oceanwithlibrarys.network.WebService;
import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;
import jian.zhang.oceanwithlibrarys.stationDetail.view.StationDetailView;

public class StationDetailPresenter extends BasePresenter<List<Tide>, StationDetailView> {

    private static final String TAG = "OceanTide";
    private boolean isLoadingData = false;
    private Station mStation;
    private Context mContext;

    public StationDetailPresenter(Station station, Context context) {
        mStation = station;
        mContext = context;
    }

    @Override
    protected void updateView() {
        view().showTideList(mModel);
    }

    @Override
    public void bindView(@NonNull StationDetailView view) {
        super.bindView(view);
        // Let's not reload data if it's already here
        if (mModel == null && !isLoadingData) {
            view().showLoading();
            loadData();
        }
    }

    private void loadData() {
        isLoadingData = true;
        if (WebService.networkConnected(mContext)) {
            new GetStationDetailTask().execute();
        } else {
            WebService.showNetworkDialog(mContext);
        }

    }

    // Call the api with station Id to get the station detail information
    private class GetStationDetailTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            return WebService.getJson(Constants.OCEAN_CANDY_BASE_URL + "/" + mStation.getStationId());
        }

        @Override
        protected void onPostExecute(final String jsonData) {
            super.onPostExecute(jsonData);
            List<Tide> tideList = setupTideList(jsonData);
            setModel(tideList);
            view().setupShareFeature(tideList);
            isLoadingData = false;

        }
    }

    private List<Tide> setupTideList(final String jsonData) {
        List<Tide> tideList = new ArrayList<>();
        try {
            tideList.addAll(getTideList(jsonData, "Low"));
            tideList.addAll(getTideList(jsonData, "High"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return tideList;
    }

    // get the tide info list for the low or high
    private List<Tide> getTideList(String jsonData, String lowOrHigh) throws JSONException {
        List<Tide> tideList = new ArrayList<>();
        JSONObject jTideObj = new JSONObject(jsonData);
        JSONArray jTideArr = jTideObj.getJSONArray(lowOrHigh);

        for (int i = 0; i < jTideArr.length(); i++) {
            JSONObject jTide = jTideArr.getJSONObject(i);
            Tide tide = new Tide();
            tide.setId(i + "");
            tide.setTime(jTide.getString("time"));
            tide.setFeet(jTide.getString("feet"));
            tide.setLowOrHigh(lowOrHigh);
            tideList.add(tide);
        }
        return tideList;
    }

}
