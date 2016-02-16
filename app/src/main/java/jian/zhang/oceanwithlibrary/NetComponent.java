package jian.zhang.oceanwithlibrary;

import javax.inject.Singleton;

import dagger.Component;
import jian.zhang.oceanwithlibrary.ui.activity.StateListActivity;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(StateListActivity activity);
}