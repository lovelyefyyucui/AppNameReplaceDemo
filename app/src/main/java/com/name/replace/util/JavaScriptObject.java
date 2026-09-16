package com.name.replace.util;

/**
 * Created by YF on 2017/8/14.
 */
import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.name.replace.R;
import com.name.replace.WebviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/30 0030.
 */
public class JavaScriptObject {
    private Activity mContext;
    public JavaScriptObject(Activity context) {
        super();
        mContext = context;
    }

    @JavascriptInterface
    public void setTitle(String title){

    }
    @JavascriptInterface
    public void go2Recharge(){

    }
    @JavascriptInterface
    public void go2Register(){

    }
    @JavascriptInterface
    public void go2Share(String content){
        LogUtil.e(content);
        JSONObject jsonObject = DensityUtil.getJSONObject(content);
        if (jsonObject == null) return;
        String title = "", url = "";
        try {
            title = jsonObject.getString("title");
            url = jsonObject.getString("url");
        } catch (JSONException e) {
            LogUtil.e(e.getMessage());
            e.printStackTrace();
        }
        ((WebviewActivity) mContext).go2Share(title, url);
    }

    @JavascriptInterface
    public void getSource(String html) {
        LogUtil.e("Web" ,"html=" + html);
    }
}