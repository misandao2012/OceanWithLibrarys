package jian.zhang.oceanwithlibrarys.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;


@Table(name = "Stations")
public class Station extends Model implements Parcelable {
    @Expose
    @Column(name = "stateName")
    private String state_name;

    @Expose
    @Column(name = "stationId")
    private String id;

    @Expose
    @Column(name = "stationName")
    private String name;

    @Expose
    @Column(name = "favorite")
    private String mFavorite;


    public Station() {
        super();
    }

    public Station(Parcel in) {
        super();
        readFromParcel(in);
    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String stateName) {
        state_name = stateName;
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
        dest.writeString(state_name);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mFavorite);
    }

    private void readFromParcel(Parcel in) {
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


