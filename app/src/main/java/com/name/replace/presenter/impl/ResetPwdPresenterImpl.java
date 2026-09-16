package com.name.replace.presenter.impl;

import com.name.replace.domain.BaseModel;
import com.name.replace.model.GetCodeModel;
import com.name.replace.model.ResetPwdModel;
import com.name.replace.presenter.GetCodePresenter;
import com.name.replace.presenter.ResetPwdPresenter;
import com.name.replace.service.HttpCallBack;
import com.name.replace.view.BaseDataView;

import java.util.Map;

/**
 * Created by Ye on 2017/9/7.
 */

public class ResetPwdPresenterImpl implements ResetPwdPresenter {
    BaseDataView mBaseDataView;
    ResetPwdModel resetPwdModel;
    String mTag;

    public ResetPwdPresenterImpl(BaseDataView mBaseDataView, String mTag) {
        this.mBaseDataView = mBaseDataView;
        this.mTag = mTag;
        resetPwdModel = new ResetPwdModel();
    }

    @Override
    public void detachView() {
        if (mBaseDataView != null)
            mBaseDataView = null;
    }

    @Override
    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    @Override
    public void setPwd(Map<String, String> map) {
//        mBaseDataView.showLoading();
        resetPwdModel.setPwd(map, new HttpCallBack<BaseModel<Object>>(){
            @Override
            public void onSuccess(BaseModel<Object> arrayListBaseModel) {
//                mBaseDataView.hideLoading();
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
//                    mBaseDataView.hideLoading();
                    mBaseDataView.onFailure(mTag, 404, message);
                }

            }
        });
    }
}
