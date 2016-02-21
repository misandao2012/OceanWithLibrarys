package jian.zhang.oceanwithlibrarys.ui.activity;

/**
 * Created by jian on 12/14/2015.
 */

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.constants.IntentExtra;
import jian.zhang.oceanwithlibrarys.constants.Preference;
import jian.zhang.oceanwithlibrarys.service.RegistrationIntentService;
import jian.zhang.oceanwithlibrarys.ui.fragment.StateListFragment;

public class StateListActivity extends AppCompatActivity{

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private static final String TAG = "StateListActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean mMultiplePane;
    private ProgressDialog mProgressDialog;
    private Callback mCallback;


    public ProgressBar getProgressBar(){
        return mProgressBar;
    }

    public boolean getMultiplePane(){
        return mMultiplePane;
    }

    public interface Callback {
        void onFavButtonClicked();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_list_activity);
        ButterKnife.bind(this);

        initVariables();
        registerDataLoadedReceiver();
        initViews();
        initActions();

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onDataLoaded);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void initActions(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPrefs.getBoolean(Preference.PREF_FIRST_TIME_START, true)) {
            setupProgressDialogs();
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void registerDataLoadedReceiver() {
        IntentFilter filter = new IntentFilter(IntentExtra.FIRST_TIME_DATA_LOADED);
        LocalBroadcastManager.getInstance(this).registerReceiver(onDataLoaded, filter);
    }

    private BroadcastReceiver onDataLoaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // when the data loading is finished, it will receive this broadcast,
            // then dismiss the progress views
            dismissProgressDialogs();
        }
    };

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences
                    .getBoolean(Preference.SENT_TOKEN_TO_SERVER, false);
            if (sentToken) {
                Toast.makeText(StateListActivity.this, "Token retrieved and sent to server!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StateListActivity.this, "Token Error", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void startStateListFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.state_list_container);
        // it is possible two fragments overlap together
        if (fragment == null) {
            fragment = new StateListFragment();
            manager.beginTransaction()
                    .add(R.id.state_list_container, fragment)
                    .commit();
        }
    }

    private void initViews() {
        setupToolbar();
        startStateListFragment();
    }

    @OnClick(R.id.fab)
    void onFabClicked(){
        // Go to the station list view but only show all the favorite stations
        if (mCallback != null) {
            mCallback.onFavButtonClicked();
        }
        Toast.makeText(StateListActivity.this, getString(R.string.got_favorite_stations), Toast.LENGTH_SHORT).show();
    }

    private void initVariables() {
        // If it is the tablet with landscape orientation, it will have three panes
        if (findViewById(R.id.station_list_container) != null) {
            mMultiplePane = true;
        }
    }

    private void setupProgressDialogs() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading_message), true, true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    private void dismissProgressDialogs(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_title));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Preference.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
