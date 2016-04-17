package jian.zhang.oceanwithlibrarys;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.gms.ShadowGooglePlayServicesUtil;
import org.robolectric.util.ActivityController;

import jian.zhang.oceanwithlibrarys.constants.Preference;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;
import jian.zhang.oceanwithlibrarys.stateList.view.StateListActivity;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class StateListActivityRoboTest {
    private ActivityController<StateListActivity> controller;
    private StateListActivity activity;
    private Toolbar toolbar;
    private RecyclerView stateList;

    @Before
    public void setUp() {
        // 不加这句的话, isGooglePlayServicesAvailable()这个函数识别不了
        ShadowGooglePlayServicesUtil.setIsGooglePlayServicesAvailable(ConnectionResult.SUCCESS);
        controller = Robolectric.buildActivity(StateListActivity.class);
        activity = controller.create().start().resume().visible().get();
        initVars(activity);
    }

    @After
    public void tearDown() {
        controller.pause().stop().destroy();
    }

    private void initVars(StateListActivity activity) {
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment stateListFragment = fragmentManager.findFragmentById(R.id.state_list_container);
        assertNotNull(stateListFragment);
        View rootView = stateListFragment.getView();
        assertThat(rootView).isNotNull();
        initFragmentVars(rootView);
    }

    private void initFragmentVars(View rootView) {
        stateList = (RecyclerView) rootView.findViewById(R.id.state_list);
    }

    @Test
    public void general() throws Exception {
        assertNotNull(activity.getSupportActionBar());
        assertEquals("Ocean Station Tides", toolbar.getTitle());
    }

    @Test
    public void loadDataFromWeb() throws Exception {
        assertNull(stateList.getAdapter());
    }

    @Test
    public void loadDataFromDatabase() throws Exception {
        restartActivityWithDataReady();
        initVars(activity);
        Robolectric.flushBackgroundThreadScheduler();
        assertNotNull(stateList.getAdapter());
    }

    @Test
    public void startService() throws Exception {
        Intent intent = new Intent(activity, LoadDataService.class);
        LoadDataService loadDataService = new LoadDataService();
        loadDataService.onStartCommand(intent, 0, 0);

    }

    private void restartActivityWithDataReady() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(Preference.PREF_FIRST_TIME_START, false);
        editor.apply();
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(StateListActivity.class)
                .create()
                .start()
                .resume();
        activity = controller.visible().get();
    }
}
