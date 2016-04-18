package jian.zhang.oceanwithlibrarys.stationList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.constants.Constants;
import jian.zhang.oceanwithlibrarys.constants.IntentExtra;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.stationDetail.view.StationDetailActivity;
import jian.zhang.oceanwithlibrarys.stationDetail.view.StationDetailFragment;

public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.StationHolder>{
    private List<Station> mStations;
    private FragmentActivity mContext;
    private boolean mMultiplePane;

    public StationListAdapter(List<Station> stations, FragmentActivity context, boolean multiplePane) {
        mStations = stations;
        mContext = context;
        mMultiplePane = multiplePane;
    }

    @Override
    public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.station_list_item, parent, false);
        return new StationHolder(view);
    }

    @Override
    public void onBindViewHolder(StationHolder holder, int position) {
        Station station = mStations.get(position);
        holder.bindStation(station);
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    class StationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_station_name)
        TextView mStationNameTextView;
        @Bind(R.id.tv_favorite_label)
        TextView mFavoriteLabelTextView;
        private View mItemView;

        public StationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mItemView = itemView;
        }

        public void bindStation(Station station) {
            mStationNameTextView.setText(station.getName());
            if (station.getFavorite().equals(Constants.FAVORITE_TRUE)) {
                // If the station is favorite, then mark it as RED "FAV"
                mFavoriteLabelTextView.setVisibility(View.VISIBLE);
            } else {
                mFavoriteLabelTextView.setVisibility(View.INVISIBLE);
            }
            mItemView.setTag(station);
        }

        @Override
        public void onClick(View view) {
            Station station = (Station) mItemView.getTag();
            if (mMultiplePane) {
                // If multiple panes, then replace the fragment
                replaceStationDetailFragment(station);
            } else {
                // If single pane, then start a new activity
                startStationDetailActivity(station);
            }
        }
    }

    private void replaceStationDetailFragment(Station station) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentExtra.STATION_PARCELABLE, station);
        bundle.putBoolean(IntentExtra.SHOW_STATION_SUBTITLE, true);
        StationDetailFragment fragment = new StationDetailFragment();
        fragment.setArguments(bundle);
        mContext.getSupportFragmentManager().beginTransaction()
                .replace(R.id.station_detail_container, fragment)
                .commit();
    }

    private void startStationDetailActivity(Station station) {
        Intent intent = new Intent(mContext, StationDetailActivity.class);
        intent.putExtra(IntentExtra.STATION_PARCELABLE, station);
        intent.putExtra(IntentExtra.SHOW_STATION_SUBTITLE, false);
        mContext.startActivity(intent);
    }
}
