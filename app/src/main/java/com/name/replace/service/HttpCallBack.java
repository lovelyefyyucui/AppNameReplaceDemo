package com.name.replace.service;

import com.name.replace.MyApplication;
import com.name.replace.R;
import com.name.replace.domain.BaseModel;
import com.name.replace.util.LogUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class HttpCallBack<T extends BaseModel> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (null!=response && response.isSuccessful()) {
            onSuccess(response.body());
        }else{
            int code = response.raw().code();
            String message = response.raw().message();   //code 和 message 都是http Raw 数据，你抓包就能看见的
            onFailure(code, message);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        String errorMessage = MyApplication.getInstance().getString(R.string.getdata_failed);
        if (t instanceof SocketTimeoutException) {
            errorMessage = MyApplication.getInstance().getString(R.string.sockettimeoutexception);
        } else if (t instanceof ConnectException) {
            errorMessage = MyApplication.getInstance().getString(R.string.connectexception);
        } else if (t instanceof RuntimeException) {
            LogUtil.e(t, t.getMessage());
            errorMessage = MyApplication.getInstance().getString(R.string.runtimeexception);
        } else if (t instanceof UnknownHostException) {
            errorMessage = MyApplication.getInstance().getString(R.string.unknownhostexception);
        } else if (t instanceof UnknownServiceException) {
            errorMessage = MyApplication.getInstance().getString(R.string.unknownserviceexception);
        }
        onFailure(Constant.RESPONSE_CODE_FAILED, errorMessage);
    }

    public abstract void onSuccess(T t);
    public abstract void onFailure(int code, String message);
}
