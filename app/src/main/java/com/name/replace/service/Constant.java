package com.name.replace.service;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.shishicai.R;

import com.name.replace.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Constant {

//	public static final String url = "http://khd.funnypicsbox.com/jokes/";//福岛笑话

	public static final String url = "http://api.xmdadao.com/appapi/";

//	public static final String url = "http://192.168.0.99";

//	public static final String url = "http://888.shof789.com/Home/Outs/";//新闻

	public static final String AppKey = "204cb81c46a1a";

	public static final String AppSecret = "5685f2fbc03b3366af0f6c84224f0399";

	public static final String AccessKey = "yQOmiZuo9U3WDm3M";

	public static final String TOKEN = "token";

	public static final String NickName = "NickName";// 是否第三方登录

	public static final String USERNAME = "username";// 用户名

	public static final String PASSWORD = "password";// 密码

	// 自己的资料
	public final static String HEAD_IMG = "head_img";// 用户头像
	public static final String KEY_NICK = "nickname";// 昵称
	public static final String KEY_mobile = "mobile";// 手机号
	public static final String KEY_qr_code = "qr_code";// 二维码
	public static final String KEY_SEX = "sex";// 性别
	public static final String KEY_ADDRESS = "address";// 城市
	public static final String KEY_AREA = "area";// 地区
	public static final String KEY_SIGN = "sign";// 个性签名

	public static final String IS_FIRST = "isFirst";// 是否第一次启动

	//性别
	public final static int MAN = 0;
	public final static int WOMAN = 1;

	/**
	 * 请求条数
	 */
	public static final int PAGESIZE = 10;
	/**
	 * 请求失败
	 */
	public static final int RESPONSE_CODE_FAILED = -100;

	/**
	 * joke
	 */
	public static final String GetJokeList = "0_4247.json";

	public static final String URL_JOKE_LIKE = "xihuan_nr.asp";

	public static final String URL_JOKE_UNLIKE = "xihuan_nr_qx.asp";

	public static final String IS_OPEN = "is_open";


	public static final String URL_NEWS = "article.html";

	/**
	 * 登录
	 */
	public static final String Login = "user/login";

	/**
	 * 获取验证码
	 */
	public static final String GetCode = "sms/getVerifyCode";

	/**
	 * 重设密码
	 */
	public static final String ResetPwd = "user/forgetLoginPassword";

	/**
	 * 提交错误日志
	 */
	public static final String ErrorLog = "error_logs";

	/**
	 * 提交订单
	 */
	public static final String SubOrder = "submitOrders";

	/**
	 * 修改用户信息
	 */
	public static final String ModifyUserInfo = "modifyTheUserInformation";


	public static String URL_SHARE = "http://888.shof789.com/Home/Outs/index/mchid/59103170cf3b5.html";//北京赛车pk10分享  5910363b9e811  彩票 59103170cf3b5

	public static String TULING_ROBOT = "http://www.tuling123.com/openapi/api";//图灵机器人

	public static String TULING_KEY = "5a4b5c8bbf2c8a9dd02861999fa0d45c";

	public static String DEVICE_ID = "device_id";

	public static String URL_SAVE = "url";

	public static String URL_COOKIE = "cookie";

	public static String EXIT_TIME = "time";

	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
	 .imageScaleType(com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY)
     .showStubImage(R.drawable.empty_photo)    //在ImageView加载过程中显示图片
     .showImageForEmptyUri(R.drawable.empty_photo)  //image连接地址为空时
     .showImageOnFail(R.drawable.empty_photo)  //image加载失败
     .cacheInMemory(true)  //加载图片时会在内存中加载缓存
     .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
      .bitmapConfig(Bitmap.Config.RGB_565)
     .build();

	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	/**图片加载监听事件**/
  private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); //设置image隐藏动画500ms
                    displayedImages.add(imageUri); //将图片uri添加到集合中
                }
            }
        }
    }
}
