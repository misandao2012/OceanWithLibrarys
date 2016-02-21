package jian.zhang.oceanwithlibrarys.global;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jian.zhang.oceanwithlibrarys.constants.Constants;
import jian.zhang.oceanwithlibrarys.manager.StationManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
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
    public StationManager provideStationManager(){
        return new StationManager(context);
    }

    @Singleton
    @Provides
    public OceanAPI provideOceanAPI() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache =  new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.OCEAN_CANDY_BASE_URL2)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(OceanAPI.class);
    }
}
