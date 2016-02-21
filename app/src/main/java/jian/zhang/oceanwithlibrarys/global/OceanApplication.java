package jian.zhang.oceanwithlibrarys.global;

import android.app.Application;


public class OceanApplication extends Application {

    private OceanComponent mOceanComponent;
    private static OceanApplication mOceanApplication;

    public static OceanApplication app(){
        return mOceanApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OceanApplication.mOceanApplication = this;

        mOceanComponent = DaggerOceanComponent.builder()  // just run the app if not recognized
                .oceanModule(new OceanModule(this))
                .build();
    }

    public OceanComponent getOceanComponent() {
        return mOceanComponent;
    }
}