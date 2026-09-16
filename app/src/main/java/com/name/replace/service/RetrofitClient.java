package com.name.replace.service;

import com.name.replace.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/30.
 */

public class RetrofitClient {

    private Retrofit retrofit;
    private static volatile RetrofitClient instance;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(OKHttpFactory.getInstance().getOkHttpClient())
                //baseUrl
                .baseUrl(Constant.url)
                //gson转化器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null)
                    instance = new RetrofitClient();
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
