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

import javax.inject.Inject;

import jian.zhang.oceanwithlibrarys.base.BasePresenter;
import jian.zhang.oceanwithlibrarys.domainobjects.Station;
import jian.zhang.oceanwithlibrarys.global.OceanAPI;
import jian.zhang.oceanwithlibrarys.global.OceanApplication;
import jian.zhang.oceanwithlibrarys.network.WebService;
import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;
import jian.zhang.oceanwithlibrarys.stationDetail.model.TideType;
import jian.zhang.oceanwithlibrarys.stationDetail.view.StationDetailView;
import retrofit2.Call;
import rx.subscriptions.CompositeSubscription;

public class StationDetailPresenter extends BasePresenter<List<Tide>, StationDetailView> {

    @Inject
    OceanAPI mOceanAPI;

    private static final String TAG = "OceanTide";
    private boolean isLoadingData = false;
    private Station mStation;
    private Context mContext;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private void loadData2() {
        /*isLoadingData = true;
        mCompositeSubscription.add(
                mOceanAPI.getTides(mStation.getStationId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Tide>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                String a = e.getMessage();
                                System.out.print(a);
                            }

                            @Override
                            public void onNext(List<Tide> tides) {
                                List<Tide> tideList = tides;
                                setModel(tideList);
                                view().setupShareFeature(tideList);
                                isLoadingData = false;
                            }

                        }));*/
    }

    public StationDetailPresenter(Station station, Context context) {
        mStation = station;
        mContext = context;
        OceanApplication.app().getOceanComponent().inject(this);
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

    @Override
    public void unbindView() {
        mCompositeSubscription.unsubscribe();
    }

    private void loadData() {
        isLoadingData = true;
        if (WebService.networkConnected(mContext)) {
            final Call<TideType> call = mOceanAPI.getTides(mStation.getStationId());  //without rxJava
            new GetStationDetailTask().execute(call);
            //new GetStationDetailTask().execute();
        } else {
            WebService.showNetworkDialog(mContext);
        }

    }

    // Call the api with station Id to get the station detail information
    private class GetStationDetailTask extends AsyncTask<Call, Void, Boolean> {

        TideType mTideType;

        // {"Low":[{"time":"2016-03-18 05:48:00 -0400","feet":"-0.1","tide":"Low"}],"High":[{"time":"2016-03-18 19:59:00 -0400","feet":"0.7","tide":"High"}]}
        @Override
        protected Boolean doInBackground(Call... calls) {   // 8730067
            try {
                mTideType = (TideType) calls[0].execute().body();
                return true;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
            //return WebService.getJson(Constants.OCEAN_CANDY_BASE_URL + "/stations/" + mStation.getStationId());
        }

        @Override
        protected void onPostExecute(final Boolean jsonData) {
            super.onPostExecute(jsonData);
            //List<Tide> tideList = setupTideList(jsonData);
            List<Tide> tideList = new ArrayList<>();
            tideList.addAll(mTideType.getHigh());
            tideList.addAll(mTideType.getLow());
            for (int i = 0; i < tideList.size(); i++) {
                tideList.get(i).setId(i + "");

            }
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
            //tide.setLowOrHigh(lowOrHigh);
            tideList.add(tide);
        }
        return tideList;
    }

}
