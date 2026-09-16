package com.name.replace.factory;


import com.name.replace.factory.realize.APIService;
import com.name.replace.service.RetrofitClient;

/**
 * Created by Administrator on 2017/3/30.
 */

public class APIFactory{

    private static volatile APIFactory instance;

    private final APIService mAPIService;

    public APIFactory(){
        mAPIService= RetrofitClient.getInstance().getRetrofit().create(APIService.class);
    }

    public static APIFactory getInstance() {
        if (instance == null) {
            synchronized (APIFactory.class) {
                if (instance == null)
                    instance = new APIFactory();
            }
        }
        return instance;
    }

    public APIService getAPIService() {
        return mAPIService;
    }

}
