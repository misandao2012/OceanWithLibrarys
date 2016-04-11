package jian.zhang.oceanwithlibrarys.base;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<M, V> {
    protected M mModel;
    private WeakReference<V> mView;  // 这里的V就是一些接口,可是为什么这么用?

    public void setModel(M model) {
        resetState();
        mModel = model;
        if(setupDone()) {
            updateView();
        }
    }

    protected void resetState() {
    }

    // bind view的时候,如果setup完成,则更新view
    public void bindView(@NonNull V view) {
        mView = new WeakReference<>(view);
        if(setupDone()) {
            updateView();
        }
    }

    public void unbindView() {
        mView = null;
    }

    protected V view() {
        if(mView == null) {
            return null;
        } else {
            return mView.get();  // 这里的get是和WeakReference相关的
        }
    }

    protected abstract void updateView();

    // 数据和view都不空时, setup就完成了
    protected  boolean setupDone() {
        return view() != null && mModel != null;
    }

}
