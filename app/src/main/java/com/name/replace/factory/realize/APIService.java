package com.name.replace.factory.realize;


import com.name.replace.domain.BaseListModel;
import com.name.replace.domain.BaseModel;
import com.name.replace.domain.DayNews;
import com.name.replace.domain.FeedbackInfo;
import com.name.replace.domain.JokeInfo;
import com.name.replace.service.Constant;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/3/30.
 */

public interface APIService {

    /**
     * 点赞
     */
    @GET(Constant.URL_JOKE_LIKE)
    Call<FeedbackInfo> setLike(@Query("id") String id);

    /**
     * 取消点赞
     */
    @GET(Constant.URL_JOKE_UNLIKE)
    Call<FeedbackInfo> cancelLike(@Query("id") String id);

    /**
     * 笑话
     */
    @GET(Constant.GetJokeList)
    Call<ArrayList<JokeInfo>> getJokeList();

    /**
     * 新闻
     */
    @GET(Constant.URL_NEWS)
    Call<BaseModel<ArrayList<DayNews>>> getNewsList();

    @FormUrlEncoded
    @POST(Constant.Login)
    Call<BaseModel<Object>> goLogin(@Field("phone") String phone, @Field("pass") String pass, @Field("type") String type, @Field("registration_id") String registration_id);

    @FormUrlEncoded
    @POST(Constant.GetCode)
    Call<BaseModel<Object>> getCode(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Constant.Login)
    Call<BaseModel<Object>> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Constant.ResetPwd)
    Call<BaseModel<Object>> setPwd(@FieldMap Map<String, String> map);

    @Multipart
    @POST(Constant.ErrorLog)
    Call<BaseModel<Object>> subErrorLog(@Part MultipartBody.Part file, @Part("platform") String platform);

    @FormUrlEncoded
    @POST(Constant.SubOrder)
    Call<okhttp3.ResponseBody> subOrder(@FieldMap Map<String, String> map);

    @Multipart
    @POST(Constant.ModifyUserInfo)
    Call<okhttp3.ResponseBody> modifyUserInfo(@PartMap Map<String, RequestBody> map);

}
