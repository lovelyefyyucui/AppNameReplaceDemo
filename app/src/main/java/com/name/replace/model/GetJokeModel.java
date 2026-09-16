package com.name.replace.model;

import com.name.replace.domain.FeedbackInfo;
import com.name.replace.domain.JokeInfo;
import com.name.replace.factory.DataFactory;
import java.util.ArrayList;

import retrofit2.Callback;

/**
 * Created by Administrator on 2017/3/31.
 */

public class GetJokeModel {

    public void like(String id, Callback<FeedbackInfo> callBack){
        DataFactory.getInstance().setLike(id, callBack);
    }

    public void unlike(String id, Callback<FeedbackInfo> callBack){
        DataFactory.getInstance().cancelLike(id, callBack);
    }

    public void getJoke(Callback<ArrayList<JokeInfo>> callBack){
        DataFactory.getInstance().getJoke(callBack);
    }

}
