package jian.zhang.oceanwithlibrarys.stationDetail;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.base.MvpRecyclerListAdapter;
import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;
import jian.zhang.oceanwithlibrarys.stationDetail.presenter.TidePresenter;
import jian.zhang.oceanwithlibrarys.stationDetail.view.TideViewHolder;


// 本class不是abstract, 所以所有abstract method都要实现
public class TideAdapter extends MvpRecyclerListAdapter<Tide, TidePresenter, TideViewHolder> {

    private Context mContext;

    public TideAdapter(Context context) {
        mContext = context;
    }

    @Override
    public TideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // notice this parent.getContext()
        return new TideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.station_detail_item, parent, false));
    }

    @NonNull
    @Override
    protected TidePresenter createPresenter(@NonNull Tide tide) {
        TidePresenter presenter = new TidePresenter(mContext);
        presenter.setModel(tide);
        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull Tide model) {
        return model.getId(); // the model must has an Id here
    }
}
