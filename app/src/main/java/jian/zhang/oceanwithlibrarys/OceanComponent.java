package jian.zhang.oceanwithlibrarys;

import javax.inject.Singleton;

import dagger.Component;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;

@Singleton
@Component(modules = {OceanModule.class})
public interface OceanComponent {
    //OceanAPI oceanAPI();  // sometimes need, but don't know when yet
    void inject(LoadDataService service);
}
