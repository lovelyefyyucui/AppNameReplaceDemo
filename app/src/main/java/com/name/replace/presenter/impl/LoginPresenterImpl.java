package com.name.replace.presenter.impl;

import com.name.replace.domain.BaseModel;
import com.name.replace.model.GetCodeModel;
import com.name.replace.model.LoginModel;
import com.name.replace.presenter.GetCodePresenter;
import com.name.replace.presenter.LoginPresenter;
import com.name.replace.service.HttpCallBack;
import com.name.replace.view.BaseDataView;

import java.util.Map;

/**
 * Created by Ye on 2017/9/7.
 */

public class LoginPresenterImpl implements LoginPresenter {
    BaseDataView mBaseDataView;
    LoginModel loginModel;
    String mTag;

    public LoginPresenterImpl(BaseDataView mBaseDataView, String mTag) {
        this.mBaseDataView = mBaseDataView;
        this.mTag = mTag;
        loginModel = new LoginModel();
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
    public void login(Map<String, String> map) {
//        mBaseDataView.showLoading();
          loginModel.login(map, new HttpCallBack<BaseModel<Object>>(){
            @Override
            public void onSuccess(BaseModel<Object> model) {
//                mBaseDataView.hideLoading();
                if (model != null)
                {
                    if (mBaseDataView != null)
                    {
                        mBaseDataView.setDataInfo(mTag, model.getResultCode(), model.getData(), model.getMessage());
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
