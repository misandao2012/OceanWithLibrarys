package jian.zhang.oceanwithlibrarys.stationList.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.constants.IntentExtra;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.loader.StationsByStateLoader;
import jian.zhang.oceanwithlibrarys.stationList.StationListAdapter;
import jian.zhang.oceanwithlibrarys.stationList.presenter.StationListPresenter;

/**
 * Created by jian on 12/14/2015.
 */
public class StationListFragment extends Fragment implements StationListView {


    @Bind(R.id.station_list)
    RecyclerView mRecyclerView;

    private boolean mMultiplePane;
    private String mStateName;

    private StationListPresenter mPresenter;

    @Inject
    public StationListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //OceanApplication.app().getOceanComponent().inject(this);
        initVariables();
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.station_list_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setupStateNameTextView(rootView);
        mPresenter.initLoader();
        registerFavChangedReceiver();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onFavoriteChanged);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // 注意这里,如果把super.onDestroy()放到后面, Loader的onReset中getView()会为null
        // 因为detachView会把view变为null, 而且Loader中的onReset是在super.onDestroy()中调用的
        super.onDestroy();
        mPresenter.detachView();
    }

    /*
    * If the favorite status changed, then restart the loader and refresh the recyclerView
    * */
    private BroadcastReceiver onFavoriteChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // reload the data when the favorite status changed
            mPresenter.restartLoader();
        }
    };

    private void initVariables() {
        mStateName = getArguments().getString(IntentExtra.STATE_NAME);
        mMultiplePane = getArguments().getBoolean(IntentExtra.MULTIPLE_PANE);
        mPresenter = new StationListPresenter(this);
    }

    @Override
    public Loader<List<Station>> createLoader() {
        return new StationsByStateLoader(getActivity(), mStateName);  // 注意这个构造函数返回的值类型
    }

    @Override
    public void setAdapterNull(){
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void setupRecyclerViewAdapter(List<Station> stationList){
        StationListAdapter adapter = new StationListAdapter(stationList, getActivity(), mMultiplePane);
        // if there is no favorite stations yet
        if (stationList.size() == 0 && mStateName.equals(getString(R.string.favorite_stations))) {
            Toast.makeText(getActivity(), getString(R.string.no_favorite_message), Toast.LENGTH_SHORT).show();
        }
        mRecyclerView.setAdapter(adapter);
    }

    private void registerFavChangedReceiver() {
        IntentFilter filter = new IntentFilter(IntentExtra.FAVORITE_CHANGED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onFavoriteChanged, filter);
    }

    private void setupStateNameTextView(View rootView) {
        TextView stateNameTextView = (TextView) rootView.findViewById(R.id.tv_state_name);
        // If it is multiple panes, do not show the station subtitle
        if (mMultiplePane) {
            stateNameTextView.setVisibility(View.VISIBLE);
            stateNameTextView.setText(mStateName);
        } else {
            stateNameTextView.setVisibility(View.INVISIBLE);
        }
    }
}
