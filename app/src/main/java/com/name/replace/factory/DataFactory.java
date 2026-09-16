package com.name.replace.factory;


import com.name.replace.domain.BaseListModel;
import com.name.replace.domain.BaseModel;
import com.name.replace.domain.DayNews;
import com.name.replace.domain.FeedbackInfo;
import com.name.replace.domain.JokeInfo;
import com.name.replace.service.HttpCallBack;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

/**
 * Created by Administrator on 2017/3/30.
 */

public class DataFactory {

    private static volatile DataFactory instance;

    public static DataFactory getInstance() {
        if (instance == null) {
            synchronized (DataFactory.class) {
                if (instance == null)
                    instance = new DataFactory();
            }
        }
        return instance;
    }


    /**
     * 笑话
     */
    public void getJoke(Callback<ArrayList<JokeInfo>> callBack)
    {
        APIFactory.getInstance().getAPIService().getJokeList().enqueue(callBack);
    }

    /**
     * 点赞
     */
    public void setLike(String id, Callback<FeedbackInfo> callBack)
    {
        APIFactory.getInstance().getAPIService().setLike(id).enqueue(callBack);
    }

    /**
     * 取消点赞
     */
    public void cancelLike(String id, Callback<FeedbackInfo> callBack)
    {
        APIFactory.getInstance().getAPIService().cancelLike(id).enqueue(callBack);
    }

    /**
     * 新闻
     */
    public void getNews(HttpCallBack<BaseModel<ArrayList<DayNews>>> callBack)
    {
        APIFactory.getInstance().getAPIService().getNewsList().enqueue(callBack);
    }

    /**
     * 验证码
     */
    public void getCode(Map<String, String> map, HttpCallBack<BaseModel<Object>> callBack)
    {
        APIFactory.getInstance().getAPIService().getCode(map).enqueue(callBack);
    }

    /**
     * 登录
     */
    public void login(Map<String, String> map, HttpCallBack<BaseModel<Object>> callBack)
    {
        APIFactory.getInstance().getAPIService().login(map).enqueue(callBack);
    }

    /**
     * 重设密码
     */
    public void setPwd(Map<String, String> map, HttpCallBack<BaseModel<Object>> callBack)
    {
        APIFactory.getInstance().getAPIService().setPwd(map).enqueue(callBack);
    }
}
