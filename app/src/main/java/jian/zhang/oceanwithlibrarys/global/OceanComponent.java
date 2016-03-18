package jian.zhang.oceanwithlibrarys.global;

import javax.inject.Singleton;

import dagger.Component;
import jian.zhang.oceanwithlibrarys.loader.StationsByStateLoader;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;
import jian.zhang.oceanwithlibrarys.stationDetail.presenter.StationDetailPresenter;
import jian.zhang.oceanwithlibrarys.ui.fragment.StateListFragment;
import jian.zhang.oceanwithlibrarys.stationDetail.view.StationDetailFragment;

@Singleton
@Component(modules = {OceanModule.class})
public interface OceanComponent {
    //OceanAPI oceanAPI();  // sometimes need, but don't know when yet
    void inject(LoadDataService service);
    void inject(StationsByStateLoader loader);
    void inject(StateListFragment fragment);
    void inject(StationDetailFragment fragment);
    void inject(StationDetailPresenter presenter);
}
