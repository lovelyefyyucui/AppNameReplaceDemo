package com.name.replace.presenter.impl;

import android.app.Activity;

import com.name.replace.domain.BaseListModel;
import com.name.replace.domain.BaseModel;
import com.name.replace.domain.DayNews;
import com.name.replace.model.GetNewsModel;
import com.name.replace.presenter.GetNewsPresenter;
import com.name.replace.service.HttpCallBack;
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
public class GetNewsPresenterImpl implements GetNewsPresenter {
    BaseDataView mBaseDataView;
    GetNewsModel mGetNewsModel;
    String mTag;

    @Override
    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public GetNewsPresenterImpl(String tag, BaseDataView mBaseDataView){
        mTag=tag;
        this.mBaseDataView=mBaseDataView;
        mGetNewsModel = new GetNewsModel();
    }

    @Override
    public void getNews() {
        mBaseDataView.showLoading();
        mGetNewsModel.getNews(new HttpCallBack<BaseModel<ArrayList<DayNews>>>(){
            @Override
            public void onSuccess(BaseModel<ArrayList<DayNews>> arrayListBaseModel) {
                mBaseDataView.hideLoading();
                if (arrayListBaseModel != null)
                {
                    if (mBaseDataView != null)
                    {
                        mBaseDataView.setDataInfo(mTag,arrayListBaseModel.getResultCode(),arrayListBaseModel.getData(),arrayListBaseModel.getMessage());
                    }
                }else {
                    mBaseDataView.onFailure(mTag, 404, "暂无数据");
                }

            }

            @Override
            public void onFailure(int code, String message) {
                if (mBaseDataView != null)
                {
                    mBaseDataView.hideLoading();
                    mBaseDataView.onFailure(mTag, 404, message);
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
