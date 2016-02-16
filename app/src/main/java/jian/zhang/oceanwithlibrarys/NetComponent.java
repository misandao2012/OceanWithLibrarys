package jian.zhang.oceanwithlibrarys;

import javax.inject.Singleton;

import dagger.Component;
import jian.zhang.oceanwithlibrarys.ui.activity.StateListActivity;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(StateListActivity activity);
}