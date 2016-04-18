package jian.zhang.oceanwithlibrarys.base2;


public abstract class MyBasePresenter<V extends MyMvpView> {
    private V mMvpView;

    public void attachView(V mvpView) {
        mMvpView = mvpView;
    }

    public void detachView() {  //如果没有这个函数会怎样
        mMvpView = null;
    }

    public V getView() {
        return mMvpView;
    }
}
