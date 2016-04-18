package jian.zhang.oceanwithlibrarys.stateList.view;


import java.util.List;

import jian.zhang.oceanwithlibrarys.base2.MyMvpView;
import jian.zhang.oceanwithlibrarys.database.Station;

public interface StateListView extends MyMvpView {
    void showProgress(final boolean show);
    void updateUI(List<Station> stations);
}
