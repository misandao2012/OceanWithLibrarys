package jian.zhang.oceanwithlibrarys.stationDetail.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jian.zhang.oceanwithlibrarys.base.BasePresenter;
import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.global.OceanAPI;
import jian.zhang.oceanwithlibrarys.global.OceanApplication;
import jian.zhang.oceanwithlibrarys.manager.StationManager;
import jian.zhang.oceanwithlibrarys.network.WebService;
import jian.zhang.oceanwithlibrarys.stationDetail.model.Tide;
import jian.zhang.oceanwithlibrarys.stationDetail.model.TideType;
import jian.zhang.oceanwithlibrarys.stationDetail.view.StationDetailView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.subscriptions.CompositeSubscription;


// presenter的作用就是代表之前的activity, fragment, viewholder, 让它们只展示view, 排除和model的关系
public class StationDetailPresenter extends BasePresenter<List<Tide>, StationDetailView> {

    // 像这样Inject进来关于数据的类一定不会出现在View或Fragment中
    // presenter中一定不会出现任何的widget
    @Inject
    OceanAPI mOceanAPI;

    @Inject
    StationManager mStationManager;

    private static final String TAG = "OceanTide";
    private boolean isLoadingData = false;
    private final Station mStation;
    private final Context mContext;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public StationDetailPresenter(Station station, Context context) {
        mStation = station;
        mContext = context;
        OceanApplication.app().getOceanComponent().inject(this);
    }

    @Override
    protected void updateView() {
        view().showTideList(mModel);
        view().setupShareFeature(mModel);
    }

    @Override
    public void bindView(@NonNull StationDetailView view) {
        super.bindView(view);
        // Let's not reload data if it's already here
        if (mModel == null && !isLoadingData) {
            view().showLoading();
            loadData();
        }
    }

    public void updateFavStatus() {
        mStationManager.updateFavByStation(mStation);
    }

    @Override
    public void unbindView() {
        mCompositeSubscription.unsubscribe();
    }

    private void loadData() {
        isLoadingData = true;
        if (WebService.networkConnected(mContext)) {
            getStationDetail();
        } else {
            WebService.showNetworkDialog(mContext);
        }
    }

    // Call the api with station Id to get the station detail information, 例如 8730067
    // {"Low":[{"time":"2016-03-18 05:48:00 -0400","feet":"-0.1","tide":"Low"}],"High":[{"time":"2016-03-18 19:59:00 -0400","feet":"0.7","tide":"High"}]}
    private void getStationDetail() {
        final Call<TideType> call = mOceanAPI.getTides(mStation.getStationId());
        call.enqueue(new Callback<TideType>() {
            @Override
            public void onResponse(Call<TideType> call, Response<TideType> response) {
                int statusCode = response.code();
                TideType tideType = response.body();
                handleTideResults(tideType);
            }

            @Override
            public void onFailure(Call<TideType> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    private void handleTideResults(final TideType tideType) {
        final List<Tide> tideList = new ArrayList<>();
        if (tideType.getHigh() != null) {
            tideList.addAll(tideType.getHigh());
        }
        if (tideType.getLow() != null) {
            tideList.addAll(tideType.getLow());
        }
        for (int i = 0; i < tideList.size(); i++) {
            tideList.get(i).setId(i + "");

        }
        setModel(tideList);   // setModel时会更新view
        isLoadingData = false;
    }
}
