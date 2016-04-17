package jian.zhang.oceanwithlibrarys.stationDetail.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.constants.Constants;
import jian.zhang.oceanwithlibrarys.constants.IntentExtra;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.stationDetail.TideAdapter;
import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;
import jian.zhang.oceanwithlibrarys.stationDetail.presenter.StationDetailPresenter;

public class StationDetailFragment extends Fragment implements StationDetailView{

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Bind(R.id.list_tide)
    RecyclerView mTideRecyclerView;

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    private static final String TAG = StationDetailFragment.class.getSimpleName();

    private Station mStation;
    private TideAdapter mAdapter;
    private StationDetailPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.station_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setupStationNameTextView(rootView);
        setupFavCheckBoxFeature(rootView);

        mTideRecyclerView.setHasFixedSize(true);
        mTideRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TideAdapter(getActivity());
        mTideRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void initVariables(){
        mStation = getArguments().getParcelable(IntentExtra.STATION_PARCELABLE);
        mPresenter = new StationDetailPresenter(mStation, getActivity());
    }

    private void setupStationNameTextView(View rootView) {
        TextView stationNameTextView = (TextView) rootView.findViewById(R.id.tv_station_name);
        // If it is multiple panes, do not show the station subtitle
        if (getArguments().getBoolean(IntentExtra.SHOW_STATION_SUBTITLE)) {
            stationNameTextView.setVisibility(View.VISIBLE);
            stationNameTextView.setText(mStation.getName());
        } else {
            stationNameTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.bindView(this);   //注意这里的this是Fragment,还可以是什么类型?
    }

    @Override
    public void onPause() {
        mPresenter.unbindView();
        super.onPause();
    }

    @Override
    public void showTideList(List<Tide> tideList) {
        mAdapter.clearAndAddAll(tideList);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    // If the favorite check box is checked, the favorite feature will be updated to the database
    private void setupFavCheckBoxFeature(View rootView) {
        CheckBox favoriteCheck = (CheckBox) rootView.findViewById(R.id.favorite_check);
        initFavCheckBox(favoriteCheck);
        favoriteCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavCheckClicked(view);
            }
        });
    }

    private void initFavCheckBox(CheckBox favoriteCheck) {
        String ifFavorite = mStation.getFavorite();
        if (ifFavorite != null && ifFavorite.equals(Constants.FAVORITE_TRUE)) {
            favoriteCheck.setChecked(true);
        } else {
            favoriteCheck.setChecked(false);
        }
    }

    private void onFavCheckClicked(View view) {
        if (((CheckBox) view).isChecked()) {
            mStation.setFavorite(Constants.FAVORITE_TRUE);
        } else {
            mStation.setFavorite(Constants.FAVORITE_FALSE);
        }
        // The favorite status changed, then send the broadcast
        sendFavChangedBroadcast();
    }

    private void sendFavChangedBroadcast() {
        Intent intent = new Intent(IntentExtra.FAVORITE_CHANGED);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        mPresenter.updateFavStatus();
    }

    @Override
    public void setupShareFeature(@NonNull final List<Tide> tides) {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareStationTideInformation(tides, mStation);
            }
        });
    }

    /*
    * Share the tide information to other apps
    * */
    public void shareStationTideInformation(List<Tide> tides, Station station) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I got " + tides.size() + " Tide Information from " + station.getName());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_tide_infomation)));
    }

}
