package jian.zhang.oceanwithlibrarys.global;

import android.content.Context;

import com.google.gson.GsonBuilder;

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

// Module就是提供注入函数的地方, 相当于iOS当中的global文件
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
                .baseUrl(Constants.OCEAN_CANDY_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()  // 加上这个,否则active android Station不好用
                        .create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(OceanAPI.class);
    }
}
