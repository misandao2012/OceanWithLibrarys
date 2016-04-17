package jian.zhang.oceanwithlibrarys.global;

import javax.inject.Singleton;

import dagger.Component;
import jian.zhang.oceanwithlibrarys.loader.StationsByStateLoader;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;
import jian.zhang.oceanwithlibrarys.stateList.presenter.StateListPresenter;
import jian.zhang.oceanwithlibrarys.stateList.view.StateListFragment;
import jian.zhang.oceanwithlibrarys.stationDetail.presenter.StationDetailPresenter;

@Singleton
@Component(modules = {OceanModule.class})
public interface OceanComponent {
    //OceanAPI oceanAPI();  // sometimes need, but don't know when yet
    void inject(LoadDataService service);
    void inject(StationsByStateLoader loader);
    void inject(StateListPresenter presenter);
    void inject(StationDetailPresenter presenter);
    void inject(StateListFragment fragment);
    //void inject(StationListFragment fragment);
}
