package com.djw.mvpbase.newwork.di;

import android.util.Log;

import com.djw.mvpbase.newwork.JsonUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class HttpModule {

    private static final String TAG=HttpModule.class.getSimpleName();

    static class HttpLogger implements HttpLoggingInterceptor.Logger{

        private StringBuilder mMessage = new StringBuilder();

        @Override
        public void log(String message) {
            // 请求或者响应开始
            if(message.startsWith("--> POST")){
                mMessage.setLength(0);
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            boolean isJson=(message.startsWith("{")&&message.endsWith("}"))||
                    (message.startsWith("[")&&message.endsWith("]"));

            if(isJson){
                message= JsonUtil.formatJson(message);
            }
            mMessage.append(message.concat("\n"));
            // 请求或者响应结束，打印整条日志
            if(message.startsWith("<-- END HTTP")){
                Log.d(TAG,mMessage.toString());
            }
        }
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient client, HttpUrl baseUrl){
        return new Retrofit.Builder().baseUrl(baseUrl)
                .client(client)
                //添加一个json的工具
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                //添加rxjava处理工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(){
        OkHttpClient.Builder httpBuilder=new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLogger());
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client=httpBuilder
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000,TimeUnit.SECONDS)
                .writeTimeout(10000,TimeUnit.SECONDS)
                .build();
        return client;
    }
}
