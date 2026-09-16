package com.name.replace.presenter.impl;

import android.app.Activity;

import com.name.replace.domain.FeedbackInfo;
import com.name.replace.domain.JokeInfo;
import com.name.replace.model.GetJokeModel;
import com.name.replace.presenter.GetJokePresenter;
import com.name.replace.util.LogUtil;
import com.name.replace.view.BaseDataView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/6.
 */
public class GetJokePresenterImpl implements GetJokePresenter {
    BaseDataView mBaseDataView;
    GetJokeModel mGetJokeModel;
    String mTag;

    public GetJokePresenterImpl(String tag, BaseDataView mBaseDataView){
        mTag = tag;
        this.mBaseDataView = mBaseDataView;
        mGetJokeModel = new GetJokeModel();
    }

    @Override
    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    @Override
    public void like(String id) {
        mGetJokeModel.like(id, new Callback<FeedbackInfo>() {

            @Override
            public void onResponse(Call<FeedbackInfo> call, Response<FeedbackInfo> response) {
                if (response.body() != null)
                {
                    //再使用Retrofit自带的JSON解析（或者别的什么）
                    FeedbackInfo info = response.body();
                    LogUtil.e(info.toString());
                    if (mBaseDataView != null)
                        mBaseDataView.setDataInfo(mTag,response.code(),info,response.message());
                } else {
                    mBaseDataView.onFailure(mTag, 404, "暂无数据");
                }
            }

            @Override
            public void onFailure(Call<FeedbackInfo> call, Throwable t) {
                if (mBaseDataView != null)
                    mBaseDataView.onFailure(mTag, 404, t.getMessage());
            }

        });
    }

    @Override
    public void unlike(String id) {
        mGetJokeModel.unlike(id, new Callback<FeedbackInfo>() {

            @Override
            public void onResponse(Call<FeedbackInfo> call, Response<FeedbackInfo> response) {
                if (response.body() != null)
                {
                    //再使用Retrofit自带的JSON解析（或者别的什么）
                    FeedbackInfo info = response.body();
                    LogUtil.e(info.toString());
                    if (mBaseDataView != null)
                        mBaseDataView.setDataInfo(mTag,response.code(),info,response.message());
                } else {
                    mBaseDataView.onFailure(mTag, 404, "no data");
                }
            }

            @Override
            public void onFailure(Call<FeedbackInfo> call, Throwable t) {
                if (mBaseDataView != null)
                    mBaseDataView.onFailure(mTag, 404, t.getMessage());
            }

        });
    }

    @Override
    public void getJoke() {
        mBaseDataView.showLoading();
        mGetJokeModel.getJoke(new Callback<ArrayList<JokeInfo>>() {

            @Override
            public void onResponse(Call<ArrayList<JokeInfo>> call, Response<ArrayList<JokeInfo>> response) {
                mBaseDataView.hideLoading();
                if (response.body() != null)
                {
                    //再使用Retrofit自带的JSON解析（或者别的什么）
                    List<JokeInfo> info = response.body();
                    LogUtil.e(info.toString());
                    if (mBaseDataView != null)
                    {
                        mBaseDataView.setDataInfo(mTag,response.code(),info,response.message());
                    }
                } else {
                    mBaseDataView.onFailure(mTag, 404, "no data");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JokeInfo>> call, Throwable t) {
                if (mBaseDataView != null)
                {
                    mBaseDataView.hideLoading();
                    mBaseDataView.onFailure(mTag, 404, t.getMessage());
                }
            }

        });
    }

    @Override
    public void detachView() {
        if (mBaseDataView != null)
        mBaseDataView = null;
    }
}
