package jian.zhang.oceanwithlibrarys.stationDetail.presenter;


import android.content.Context;

import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.base.BasePresenter;
import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;
import jian.zhang.oceanwithlibrarys.stationDetail.view.TideView;
import jian.zhang.oceanwithlibrarys.utils.Utils;

public class TidePresenter extends BasePresenter<Tide, TideView> {

    private Context mContext;

    public TidePresenter(Context context) {
        mContext = context;
    }

    @Override
    protected void updateView() {
        view().setTime(Utils.parseTideTime(mModel.getTime()));
        view().setFeet(mModel.getFeet());
        view().setLowOrHigh(mContext.getString(R.string.low_or_high, mModel.getTide()));
    }

}
