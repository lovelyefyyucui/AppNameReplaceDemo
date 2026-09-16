package com.name.replace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.name.replace.service.Constant;
import com.name.replace.domain.BaseModel;
import com.name.replace.domain.JudgeInfo;
import com.name.replace.service.HttpMethod;
import com.name.replace.service.HttpUser;
import com.name.replace.util.GsonUtils;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.R;

import org.apache.http.Header;

import cn.jpush.android.api.JPushInterface;


public class WelcomeActivity extends Activity {
	private static Handler handler;
	private static LoadMainTabTask task;
	private boolean first;
	private String registration_id = "";
	private final String TAG = "WelcomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
		first = PreferencesUtils.getBoolean(this, Constant.IS_FIRST, true);
		registration_id = PreferencesUtils.getString(this, Constant.DEVICE_ID);
		if (TextUtils.isEmpty(registration_id)) {
			registration_id = JPushInterface.getRegistrationID(this);
			PreferencesUtils.putString(this, Constant.DEVICE_ID, registration_id);
		}
		LogUtil.e(TAG, "device_id=" + registration_id);
		handler = new Handler();
		task = new LoadMainTabTask();
		handler.postDelayed(task, 1000);
	}

	/**
	 * 延迟2秒进入主界面
	 * @author Administrator
	 */
	private class LoadMainTabTask implements Runnable {

		public void run()
		{
			Intent intent = new Intent();
			if (first)
			{
				intent.setClass(WelcomeActivity.this, GuideActivity.class);
			}else {
				intent.setClass(WelcomeActivity.this, MainActivity.class);
			}
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		}
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

}
