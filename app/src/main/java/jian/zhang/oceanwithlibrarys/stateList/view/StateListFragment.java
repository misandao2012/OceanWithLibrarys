package jian.zhang.oceanwithlibrarys.stateList.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.constants.IntentExtra;
import jian.zhang.oceanwithlibrarys.constants.Preference;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.global.OceanApplication;
import jian.zhang.oceanwithlibrarys.network.WebService;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;
import jian.zhang.oceanwithlibrarys.stateList.StateListAdapter;
import jian.zhang.oceanwithlibrarys.stateList.presenter.StateListPresenter;
import jian.zhang.oceanwithlibrarys.stationList.view.StationListActivity;
import jian.zhang.oceanwithlibrarys.stationList.view.StationListFragment;
import jian.zhang.oceanwithlibrarys.utils.Utils;

/**
 * Created by jian on 12/16/2015.
 */
public class StateListFragment extends Fragment implements StateListActivity.Callback, StateListView {

    @Bind(R.id.state_list)
    RecyclerView mStateRecyclerView;

    private boolean mMultiplePane;
    private Context mContext;

    @Inject
    StateListPresenter mPresenter;

    // implement callback function to open the favorite stations view
    @Override
    public void onFavButtonClicked() {
        onButtonClicked(getString(R.string.favorite_stations));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        setupCallback();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recycleCallback();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //mPresenter = new StateListPresenter();
        OceanApplication.app().getOceanComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.state_list_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter.attachView(this);
        initRecyclerView();
        EventBus.getDefault().register(this);
        initActions();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFirstTimeLoaded(LoadDataService.FirstTimeLoadedEvent event) {
        // when the data loading is finished, it will receive this event,
        // then dismiss the progress views and update the UI
        mPresenter.loadData();
    }

    private void initActions() {
        //check if it is the first time install the App
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPrefs.getBoolean(Preference.PREF_FIRST_TIME_START, true)) {
            Utils.lockOrientationPortrait(getActivity());
            startLoadingDataService();
        } else {
            mPresenter.loadData();
        }
    }

    private void setupCallback() {
        if (mContext instanceof StateListActivity) {
            ((StateListActivity) mContext).setCallback(this);
        }
    }

    private void recycleCallback() {
        if (mContext != null && mContext instanceof StateListActivity) {
            ((StateListActivity) mContext).setCallback(null);
        }
    }

    @Override
    public void updateUI(List<Station> stations) {
        StateListAdapter adapter = new StateListAdapter(stations, mOnClickListener, getActivity());
        mStateRecyclerView.setAdapter(adapter);
        Utils.unlockOrientation(getActivity());
    }


    private void startLoadingDataService() {
        if (WebService.networkConnected(getActivity())) {
            // Put the loading task in a service because it need a long time,
            // don't want to be interrupted by the UI
            Intent intent = new Intent(getActivity(), LoadDataService.class);
            getActivity().startService(intent);
        } else {
            WebService.showNetworkDialog(getActivity());
        }
    }

    private void initRecyclerView() {
        mStateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void onButtonClicked(String name) {
        if (mContext != null && mContext instanceof StateListActivity) {
            mMultiplePane = ((StateListActivity) mContext).getMultiplePane();
        }
        if (mMultiplePane) {
            // It is multiple panes, so replace the fragment, otherwise will start a new activity
            replaceStationListFragment(name);
        } else {
            startStationListActivity(name);
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String stateName = (String) v.getTag();
            onButtonClicked(stateName);
        }
    };

    private void replaceStationListFragment(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.STATE_NAME, name);
        bundle.putBoolean(IntentExtra.MULTIPLE_PANE, mMultiplePane);
        StationListFragment fragment = new StationListFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.station_list_container, fragment)
                .commit();
    }

    private void startStationListActivity(String name) {
        Intent intent = new Intent(getActivity(), StationListActivity.class);
        intent.putExtra(IntentExtra.STATE_NAME, name);
        startActivity(intent);
    }

    @Override
    public void showProgress(final boolean show) {
        if (mContext instanceof StateListActivity) {
            ((StateListActivity) mContext).getProgressBar().setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


}
