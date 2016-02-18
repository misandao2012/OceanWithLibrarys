package jian.zhang.oceanwithlibrarys.domainobjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable {
    private long mId;
    private String state_name;
    private String id;
    private String name;
    private String mFavorite;

    public Station(){}

    public Station(Parcel in) {
        readFromParcel(in);
    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String stateName) {
        state_name = stateName;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationId() {
        return id;
    }

    public void setStationId(String stationId) {
        id = stationId;
    }

    public String getFavorite() {
        return mFavorite;
    }

    public void setFavorite(String favorite) {
        this.mFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(state_name);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mFavorite);
    }

    private void readFromParcel(Parcel in) {
        mId = in.readLong();
        state_name = in.readString();
        id = in.readString();
        name = in.readString();
        mFavorite = in.readString();
    }

    public static final Parcelable.Creator<Station> CREATOR =
            new Parcelable.Creator<Station>() {
                @Override
                public Station createFromParcel(Parcel in) {
                    return new Station(in);
                }

                @Override
                public Station[] newArray(int size) {
                    return new Station[size];
                }
            };

}
