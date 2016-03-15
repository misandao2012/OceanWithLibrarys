package jian.zhang.oceanwithlibrarys.stationDetail.view;


import android.view.View;
import android.widget.TextView;

import jian.zhang.oceanwithlibrarys.R;
import jian.zhang.oceanwithlibrarys.base.MvpViewHolder;
import jian.zhang.oceanwithlibrarys.stationDetail.presenter.TidePresenter;

public class TideViewHolder extends MvpViewHolder<TidePresenter> implements TideView {

    private final TextView mTimeTextView;   // notice the final here
    private final TextView mFeetTextView;
    private final TextView mLowOrHighTextView;

    public TideViewHolder(View itemView) {
        super(itemView);   // 必须要加这一句调用基类的构造方法, 因为基类没有默认的无参数的构造函数
        mTimeTextView = (TextView) itemView.findViewById(R.id.tv_time);
        mFeetTextView = (TextView) itemView.findViewById(R.id.tv_feet);
        mLowOrHighTextView = (TextView) itemView.findViewById(R.id.tv_lowOrHigh);
    }

    @Override
    public void setTime(String name) {
        mTimeTextView.setText(name);
    }

    @Override
    public void setFeet(String feet) {
        mFeetTextView.setText(feet);
    }

    @Override
    public void setLowOrHigh(String lowOrHigh) {
        mLowOrHighTextView.setText(lowOrHigh);
    }
}
