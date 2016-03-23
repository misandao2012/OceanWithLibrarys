package jian.zhang.oceanwithlibrarys.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import jian.zhang.oceanwithlibrarys.constants.Constants;
import jian.zhang.oceanwithlibrarys.constants.Preference;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.global.OceanAPI;
import jian.zhang.oceanwithlibrarys.global.OceanApplication;
import jian.zhang.oceanwithlibrarys.manager.StationManager;
import retrofit2.Call;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jian on 12/15/2015.
 */
public class LoadDataService extends Service {

    @Inject
    OceanAPI mOceanAPI;

    @Inject
    StationManager mStationManager;


    private static final String TAG = LoadDataService.class.getSimpleName();
    private List<Station> mStationList;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();


    private void loadData() {
        mCompositeSubscription.add(
                mOceanAPI.getStations()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Station>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<Station> stations) {
                                mStationList = stations;
                                new SetUpDatabaseTask().execute();
                            }

                        }));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //final Call<List<Station>> call = networkAPI.getStations();  //without rxJava
        //new GetStationsTask().execute(call);
        OceanApplication.app().getOceanComponent().inject(this);
        loadData();
        return START_NOT_STICKY;
    }

    private class GetStationsTask extends AsyncTask<Call, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Call... calls) {
            try {
                //mStationList = (List<Station>) calls[0].execute().body();
                return true;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                new SetUpDatabaseTask().execute();
            }
        }
    }

    private class SetUpDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                setupStationDatabase();
                return true;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                setFirstTimeStartFalse();
                // The loading is finish, then send the event to the activity
                EventBus.getDefault().post(new FirstTimeLoadedEvent());
            }
            // stop the service after data loaded
            mCompositeSubscription.unsubscribe();
            stopSelf();
        }
    }

    private void setFirstTimeStartFalse() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(Preference.PREF_FIRST_TIME_START, false);
        editor.apply();
    }


    public class FirstTimeLoadedEvent{

    }

    private void setupStationDatabase(){
        // if the task is interrupted in the middle, the first time start is not set false yet,
        // so the database maybe set multiple times, so clean the database first
        mStationManager.clearStations();
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < mStationList.size(); i++) {
                Station station = mStationList.get(i);
                station.setFavorite(Constants.FAVORITE_FALSE);
                station.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
