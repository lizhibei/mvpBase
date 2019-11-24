package com.djw.mvpbase.di;


import android.app.Application;

import com.djw.mvpbase.App;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(App mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    public Application provideApplication(){
        return mApplication;
    }
}
