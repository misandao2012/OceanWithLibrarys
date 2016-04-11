package jian.zhang.oceanwithlibrarys.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class MvpViewHolder<P extends BasePresenter> extends RecyclerView.ViewHolder {
    protected P mPresenter;

    public MvpViewHolder(View itemView) {
        super(itemView);  // notice this super
    }

    public void bindPresenter(P presenter) {
        mPresenter = presenter;
        presenter.bindView(this);
    }

    public void unbindPresenter() {
        mPresenter = null;
    }
}
