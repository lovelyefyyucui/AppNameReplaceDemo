package com.name.replace.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;


import com.name.replace.R;
import com.name.replace.WebviewActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class SharePopUtil implements PlatformActionListener {
    private Activity mActivity;
    private View mParent;
    private PopupWindow mPopupWindow;
    private String defaultTitle = "分享的标题";
    private String defaultContent = "分享的内容";
    private String defaultImage = "http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg";
    private String defaultUrl = "http://www.baidu.com";
    private int flag;
    public static int shareSDImg = 0;
    public static int shareNetImg = 1;

    public SharePopUtil(Activity mActivity, View mParent) {
        this.mActivity = mActivity;
        this.mParent = mParent;
//		ShareSDK.initSDK(mActivity);
        initView();
    }

    /**
     *
     * @param title
     *            分享的标题
     * @param imgUrl
     *            分享的图片LOGO
     * @param content
     *            分享的内容
     * @param mUrl
     *            分享点击路径
     */
    public void setShareParams(String title, String imgUrl, String content,
                               String mUrl, int flag) {
        this.defaultTitle = title;
        this.defaultContent = content;
        this.defaultImage = imgUrl;
        this.defaultUrl = mUrl;
        this.flag = flag;
    }

    private void initView() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
        View mView = mLayoutInflater.inflate(R.layout.shares_layout, null);
        mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        final LinearLayout mLayout = (LinearLayout) mView
                .findViewById(R.id.shares_popup_window);
        mView.findViewById(R.id.share_sinaweibo).setOnClickListener((OnClickListener)mActivity);
        mView.findViewById(R.id.share_wechat).setOnClickListener((OnClickListener)mActivity);
        mView.findViewById(R.id.share_wechatmoments).setOnClickListener((OnClickListener)mActivity);
        mView.findViewById(R.id.share_qq).setOnClickListener((OnClickListener)mActivity);
        mView.findViewById(R.id.share_qzone).setOnClickListener((OnClickListener)mActivity);
        mView.findViewById(R.id.share_sms).setOnClickListener((OnClickListener)mActivity);
        mView.findViewById(R.id.shares_cancel).setOnClickListener((OnClickListener)mActivity);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int top = mLayout.getTop();
                int bottom = mLayout.getBottom();
                int left = mLayout.getLeft();
                int right = mLayout.getRight();
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x < left || x > right) {
                        mPopupWindow.dismiss();
                    }
                    if (y > bottom || y < top) {
                        mPopupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        mPopupWindow.showAtLocation(mParent, Gravity.CENTER, 0, 0);
    }

    public void dismiss() {
        if (mPopupWindow != null)
        {
            if (mPopupWindow.isShowing())
            {
                mPopupWindow.dismiss();
            }
            mPopupWindow = null;
        }
    }


    public void share(String shareName) {
        // TODO Auto-generated method stub
        Platform platform = ShareSDK.getPlatform(shareName);
        ShareParams sp = new ShareParams();
        if (shareName.equals(SinaWeibo.NAME)) {
            defaultContent = defaultTitle +"。"+ defaultContent+defaultUrl;
            sp.setText(defaultContent);
            if (flag == shareNetImg) {
                sp.setImageUrl(defaultImage);
            } else if (flag == shareSDImg) {
                sp.setImagePath(defaultImage);
            }
        } else if(shareName.equals(Wechat.NAME)||shareName.equals(WechatMoments.NAME)){
            sp.setTitle(defaultTitle);
            sp.setText(defaultContent);
            if (flag == shareNetImg) {
                sp.setImageUrl(defaultImage);
            } else if (flag == shareSDImg) {
                sp.setImagePath(defaultImage);
            }
            sp.setUrl(defaultUrl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
        }else if(shareName.equals(QQ.NAME)||shareName.equals(QZone.NAME)){
            sp.setTitle(defaultTitle);
            sp.setText(defaultContent);
            if (flag == shareNetImg) {
                sp.setImageUrl(defaultImage);
            } else if (flag == shareSDImg) {
                sp.setImagePath(defaultImage);
            }
            sp.setTitleUrl(defaultUrl);
        }else if(shareName.equals(ShortMessage.NAME)){
            sp.setTitle(defaultTitle);
            sp.setText(defaultContent);
            if (flag == shareNetImg) {
                sp.setImageUrl(defaultImage);
            } else if (flag == shareSDImg) {
                sp.setImagePath(defaultImage);
            }
            sp.setTitleUrl(defaultUrl);
        }
        platform.setPlatformActionListener(this);
        platform.share(sp);
    }

    @Override
    public void onCancel(Platform plat, int action) {
        LogUtil.e("onCancel," + "msg=" + actionToString(action) + ",share plat=" + plat.getName());
        ((WebviewActivity) mActivity).showToast("取消了分享");

    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {
        LogUtil.e("onComplete," + "msg=" + actionToString(action) + ",share plat=" + plat.getName());
        ((WebviewActivity) mActivity).showToast("分享成功");
    }

    @Override
    public void onError(Platform plat, int action, Throwable t) {
        // TODO Auto-generated method stub
        LogUtil.e("onError," + "err=" + t.getMessage());
        t.printStackTrace();

        LogUtil.e("onError," + "msg=" + actionToString(action) + ",share plat=" + plat.getName());
        String text;
        // 失败
        if ("WechatClientNotExistException".equals(plat.getClass()
                .getSimpleName())) {
            text = "目前您的微信版本过低或未安装微信，需要安装微信才能使用";
        } else if ("WechatTimelineNotSupportedException".equals(plat.getClass().getSimpleName())) {
            text = "目前您的微信版本过低或未安装微信，需要安装微信才能使用";
        } else {
            text = "分享失败";
        }
        ((WebviewActivity) mActivity).showToast(text);
    }


    /** 将action转换为String */
    public String actionToString(int action) {
        switch (action) {
            case Platform.ACTION_AUTHORIZING:
                return "ACTION_AUTHORIZING";
            case Platform.ACTION_GETTING_FRIEND_LIST:
                return "ACTION_GETTING_FRIEND_LIST";
            case Platform.ACTION_FOLLOWING_USER:
                return "ACTION_FOLLOWING_USER";
            case Platform.ACTION_SENDING_DIRECT_MESSAGE:
                return "ACTION_SENDING_DIRECT_MESSAGE";
            case Platform.ACTION_TIMELINE:
                return "ACTION_TIMELINE";
            case Platform.ACTION_USER_INFOR:
                return "ACTION_USER_INFOR";
            case Platform.ACTION_SHARE:
                return "ACTION_SHARE";
            default: {
                return "UNKNOWN";
            }
        }
    }
}