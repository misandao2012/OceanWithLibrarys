package jian.zhang.oceanwithlibrarys.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by jian on 12/19/2015.
 */
public abstract class DataLoader<D> extends AsyncTaskLoader<D> {
    private D mData;

    public DataLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);   // 如果数据已存在,则直接deliver result
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(D data) {
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
