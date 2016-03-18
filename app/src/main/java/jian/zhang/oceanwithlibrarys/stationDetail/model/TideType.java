package jian.zhang.oceanwithlibrarys.stationDetail.model;


import java.util.List;

public class TideType {
    private List<Tide> Low;
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
