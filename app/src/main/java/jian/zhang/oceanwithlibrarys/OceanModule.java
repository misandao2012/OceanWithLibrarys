package jian.zhang.oceanwithlibrarys;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jian.zhang.oceanwithlibrarys.constants.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class OceanModule {
    Context context;

    public OceanModule(Context context) {
        this.context = context;
    }


    @Singleton
    @Provides
    public OceanAPI provideOceanAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.OCEAN_CANDY_BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(OceanAPI.class);

    }
}
