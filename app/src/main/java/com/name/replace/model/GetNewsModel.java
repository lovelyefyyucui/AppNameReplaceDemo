package com.name.replace.model;


import com.name.replace.domain.BaseListModel;
import com.name.replace.domain.BaseModel;
import com.name.replace.domain.DayNews;
import com.name.replace.factory.DataFactory;
import com.name.replace.service.HttpCallBack;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/3/31.
 */

public class GetNewsModel {

    public void getNews(HttpCallBack<BaseModel<ArrayList<DayNews>>> callBack){
        DataFactory.getInstance().getNews(callBack);
    }

}
