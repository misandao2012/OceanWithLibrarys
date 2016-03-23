package jian.zhang.oceanwithlibrarys.stationDetail.model;


import com.google.gson.annotations.Expose;

import java.util.List;

public class TideType {
    @Expose
    private List<Tide> Low;

    @Expose
    private List<Tide> High;

    public List<Tide> getLow() {
        return Low;
    }

    public void setLow(List<Tide> low) {
        Low = low;
    }

    public List<Tide> getHigh() {
        return High;
    }

    public void setHigh(List<Tide> high) {
        High = high;
    }
}
