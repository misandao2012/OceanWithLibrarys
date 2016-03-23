package jian.zhang.oceanwithlibrarys.base;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<M, V> {
    protected M mModel;
    private WeakReference<V> mView;

    public void setModel(M model) {
        resetState();;
        mModel = model;
        if(setupDone()) {
            updateView();
        }
    }

    protected void resetState() {
    }

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
            return mView.get();  // notice this get
        }
    }

    protected abstract void updateView();

    protected  boolean setupDone() {
        return view() != null && mModel != null;
    }

}
