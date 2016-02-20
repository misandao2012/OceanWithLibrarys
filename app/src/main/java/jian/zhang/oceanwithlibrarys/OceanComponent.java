package jian.zhang.oceanwithlibrarys;

import javax.inject.Singleton;

import dagger.Component;
import jian.zhang.oceanwithlibrarys.loader.StationsByStateLoader;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;
import jian.zhang.oceanwithlibrarys.ui.fragment.StateListFragment;
import jian.zhang.oceanwithlibrarys.ui.fragment.StationDetailFragment;

@Singleton
@Component(modules = {OceanModule.class})
public interface OceanComponent {
    //OceanAPI oceanAPI();  // sometimes need, but don't know when yet
    void inject(LoadDataService service);
    void inject(StationsByStateLoader loader);
    void inject(StateListFragment fragment);
    void inject(StationDetailFragment fragment);
}
