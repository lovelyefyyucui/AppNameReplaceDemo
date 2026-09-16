package com.name.replace.service;

import com.name.replace.BuildConfig;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/3/30.
 */

public class OKHttpFactory {

    private OkHttpClient okHttpClient;

    private static final int TIMEOUT_READ = 25;
    private static final int TIMEOUT_CONNECTION = 25;

    private static volatile OKHttpFactory instance;

    private OKHttpFactory() {

        //缓存目录
//        Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), 10 * 1024 * 1024);
        okHttpClient = new OkHttpClient.Builder()
                //stetho,可以在chrome中查看请求
//                .addNetworkInterceptor(new StethoInterceptor())
//                //添加UA
//                .addInterceptor(new UserAgentInterceptor(HttpHelper.getUserAgent()))
//                //必须是设置Cache目录
//                .cache(cache)
                //走缓存，两个都要设置
//                .addInterceptor(new OnOffLineCachedInterceptor())
//                .addNetworkInterceptor(new OnOffLineCachedInterceptor())
                //失败重连
                .retryOnConnectionFailure(true)
                //time out
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build();
        if (BuildConfig.DEBUG)
        {
            //打印请求Log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    //打印请求log
                    .addInterceptor(interceptor).build();
        }
    }

    public static OKHttpFactory getInstance() {
        if (instance == null) {
            synchronized (OKHttpFactory.class) {
                if (instance == null)
                    instance = new OKHttpFactory();
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
