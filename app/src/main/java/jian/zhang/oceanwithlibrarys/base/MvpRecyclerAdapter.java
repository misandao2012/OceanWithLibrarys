package jian.zhang.oceanwithlibrarys.base;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;


// 因为本class是abstract, 所以很多的abstract method不用实现
public abstract class MvpRecyclerAdapter<M, P extends  BasePresenter, VH extends MvpViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final Map<Object, P> mPresenters;  // final, 但是可以往里面加东西

    public MvpRecyclerAdapter() {
        mPresenters = new HashMap<>();
    }

    @NonNull
    protected P getPresenter(@NonNull M model) {
        System.err.println("Getting presenter for item " + getModelId(model));
        return mPresenters.get(getModelId(model));
    }

    @NonNull
    protected abstract P createPresenter(@NonNull M model);  // 注意abstract method没有{}

    @NonNull
    protected abstract Object getModelId(@NonNull M model);

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        holder.unbindPresenter();
    }

    @Override
    public boolean onFailedToRecycleView(VH holder) {
        // Sometimes, if animations are running on the itemView's children, the RecyclerView won't
        // be able to recycle the view. We should still unbind the presenter.
        holder.unbindPresenter();
        return super.onFailedToRecycleView(holder);
    }

    // 这个abstract method可以实现也不可以不实现
    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindPresenter(getPresenter(getItem(position)));
    }

    protected abstract M getItem(int position);  // abstract method 是因为本class当中要用到, 但是却无法实现


}
