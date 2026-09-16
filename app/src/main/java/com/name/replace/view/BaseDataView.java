package com.name.replace.view;



/**
 * Created by Administrator on 2017/3/31.
 */

public interface BaseDataView {
    void showLoading();
    void hideLoading();
    void onFailure(String tag,int code,String message);
    void setDataInfo(String tag,int code,Object obj,String message);
}
