package com.name.replace.model;


import com.name.replace.domain.BaseModel;
import com.name.replace.domain.DayNews;
import com.name.replace.factory.DataFactory;
import com.name.replace.service.HttpCallBack;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Administrator on 2017/9/7.
 */

public class GetCodeModel {

    public void getCode(Map<String, String> map, HttpCallBack<BaseModel<Object>> callBack){
        DataFactory.getInstance().getCode(map, callBack);
    }

}
