package jian.zhang.oceanwithlibrarys;

import android.app.Application;


public class OceanApplication extends Application {

    private OceanComponent mOceanComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        mOceanComponent = DaggerOceanComponent.builder()  // just run the app if not recognized
                .oceanModule(new OceanModule(this))
                .build();
    }

    public OceanComponent getOceanComponent() {
        return mOceanComponent;
    }
}
