package jian.zhang.oceanwithlibrarys.stateList;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jian.zhang.oceanwithlibrarys.database.Station;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.StateHolder> {
    private List<Station> mStations;
    private View.OnClickListener mOnClickListener;
    private Context mContext;

    public StateListAdapter(List<Station> stations, View.OnClickListener onClickListener, Context context) {
        mStations = stations;
        mOnClickListener = onClickListener;
        mContext = context;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new StateHolder(view);
    }

    @Override
    public void onBindViewHolder(StateHolder holder, int position) {
        Station station = mStations.get(position);
        holder.bindStation(station);
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    class StateHolder extends RecyclerView.ViewHolder {

        private TextView mStateNameTextView;
        private View mItemView;

        public StateHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
            mStateNameTextView = (TextView) itemView;
            mItemView = itemView;
        }

        public void bindStation(Station station) {
            mStateNameTextView.setText(station.getStateName());
            mItemView.setTag(station.getStateName());
        }
    }
}
