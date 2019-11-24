package com.djw.mvpbase.newwork.di;

import com.self.dagger2andmvp.network.api.WanAndroidApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;

@Module
public class ApiServiceModule<T> {

    @Provides
    HttpUrl provideBaseUrl(){
        return HttpUrl.parse("https://www.wanandroid.com/");
    }

    @Provides
    T provideUserService(Retrofit retrofit){
        return retrofit.create();
    }
}
