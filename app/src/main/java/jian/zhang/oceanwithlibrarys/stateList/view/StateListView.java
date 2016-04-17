package jian.zhang.oceanwithlibrarys.stateList.view;


import java.util.List;

import jian.zhang.oceanwithlibrarys.base2.MvpView;
import jian.zhang.oceanwithlibrarys.database.Station;

public interface StateListView extends MvpView{
    void showProgress(final boolean show);
    void updateUI(List<Station> stations);
}
