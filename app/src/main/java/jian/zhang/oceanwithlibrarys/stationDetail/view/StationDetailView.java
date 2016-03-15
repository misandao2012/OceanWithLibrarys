package jian.zhang.oceanwithlibrarys.stationDetail.view;


import java.util.List;

import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;

public interface StationDetailView {
    void showTideList(List<Tide> tideList);
    void showLoading();
    void setupShareFeature(List<Tide> tideList);
}
