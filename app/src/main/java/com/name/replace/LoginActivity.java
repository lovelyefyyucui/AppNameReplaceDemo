package com.name.replace;

import java.util.TreeMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.name.replace.domain.LoginEntity;
import com.name.replace.presenter.LoginPresenter;
import com.name.replace.presenter.impl.LoginPresenterImpl;
import com.name.replace.service.Constant;
import com.name.replace.util.BaseUtils;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.util.RegularUtils;
import com.name.replace.util.ToastUtils;
import com.name.replace.view.BaseDataView;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends Base2Activity implements OnClickListener, BaseDataView {
	private TextView title;
	private ImageView titleimgLeft;
	private EditText et_username;
	private EditText et_password;
	private String username = "";
	private String password = "";
	private boolean first;
	private String registration_id = "";
	private LoginPresenter loginPresenter;
	private ImageView img_nameClear;
	private ImageView img_passWordClear;
	private ImageView img_passWordType;
	private boolean inputType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TAG = "LoginActivity";
		initUI();
		username = PreferencesUtils.getString(this, Constant.USERNAME);
		password = PreferencesUtils.getString(this, Constant.PASSWORD);
		if (!TextUtils.isEmpty(username)) {
			et_username.setText(username);
		}
		if (!TextUtils.isEmpty(password)) {
			et_password.setText(password);
		}
		first = getIntent().getBooleanExtra(Constant.IS_FIRST, false);
		registration_id = PreferencesUtils.getString(this, Constant.DEVICE_ID);
		if (TextUtils.isEmpty(registration_id)) {
			registration_id = JPushInterface.getRegistrationID(this);
			PreferencesUtils.putString(this, Constant.DEVICE_ID, registration_id);
		}
		LogUtil.e(TAG, "device_id=" + registration_id);
		loginPresenter = new LoginPresenterImpl(this, "login");
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.title_top);
		title.setText("登录");
		titleimgLeft = (ImageView) findViewById(R.id.title_left);
		titleimgLeft.setImageResource(R.mipmap.top_back);
		titleimgLeft.setOnClickListener(this);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		img_nameClear = (ImageView) findViewById(R.id.img_name_text_delete);
		img_passWordClear = (ImageView) findViewById(R.id.img_password_text_delete);
		img_passWordType = (ImageView) findViewById(R.id.img_eye_password_type);
		findViewById(R.id.tv_forget_pwd).setOnClickListener(this);
		findViewById(R.id.tv_regist).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		et_username.addTextChangedListener(new MyTextWatcher(et_username));
		et_password.addTextChangedListener(new MyTextWatcher(et_password));
		img_nameClear.setOnClickListener(this);
		img_passWordClear.setOnClickListener(this);
		img_passWordType.setOnClickListener(this);
	}

	private String tip;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_login:
			if (!isNameValid() || !isPasswordValid()) {
				ToastUtils.show(this,tip);
				return;
			}
			loginService(username, password);
			break;

		case R.id.tv_forget_pwd:
			openActivity(ForgotPasswordActivity.class);
			break;
		case R.id.tv_regist:
			openActivity(RegisterActivity.class);
			break;
		case R.id.title_left:
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
			break;
		case R.id.img_name_text_delete:
			et_username.setText(null);
			break;
		case R.id.img_password_text_delete:
			et_password.setText(null);
			break;
		case R.id.img_eye_password_type:
			if(!inputType){
				et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				img_passWordType.setImageResource(R.drawable.icon_eye_pre);
			} else {
				et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				img_passWordType.setImageResource(R.drawable.icon_eye);
			}
			inputType = !inputType;
			break;
		}
	}

	public void back(View view) {
		if (first)
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
		super.back(view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (first)
			{
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	

	
	private void loginService(final String username, final String password) {
//		TreeMap<String, String> postPairs = new TreeMap<String, String>();
//		postPairs.put("mobile", username);
//		postPairs.put("password", password);
//		postPairs.put("androidPushToken", registration_id);
//		loginPresenter.login(BaseUtils.getMap(postPairs));

		LoginEntity mLoginEntity = new LoginEntity();
		mLoginEntity.setUsername(username);
		mLoginEntity.setPassword(password);
		MyApplication.getInstance().setLoginEntity(mLoginEntity);

		PreferencesUtils.putString(LoginActivity.this, Constant.USERNAME,
				username);
		PreferencesUtils.putString(LoginActivity.this, Constant.PASSWORD,
				password);
		if (!first)
		{
			setResult(RESULT_OK);
		} else {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
		}
		finish();
	}


	//动态监听输入过程
	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			switch (view.getId()) {
				case R.id.et_username:
					isNameValid();
					break;
				case R.id.et_password:
					isPasswordValid();
					break;
			}
		}
	}


	/**
	 * 检查输入的手机号码是否为空以及格式是否正确
	 *
	 * @return
	 */
	public boolean isNameValid() {
		username = et_username.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			tip = getString(R.string.empty_phone);
			et_username.requestFocus();
			return false;
		} else if (!RegularUtils.isPhone(username))
		{
			tip = getString(R.string.error_phone);
			et_username.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * 检查输入的密码是否为空
	 *
	 * @return
	 */
	public boolean isPasswordValid() {
		password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			tip = getString(R.string.empty_pwd);
			et_password.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void showLoading() {

	}

	@Override
	public void hideLoading() {

	}

	@Override
	public void onFailure(String tag, int code, String message) {
		LogUtil.e("message:" + message + ",code:" + code);
		ToastUtils.show(this, message);
	}

	@Override
	public void setDataInfo(String tag, int code, Object obj, String message) {
		LogUtil.e("message:" + message + ",code:" + code);
		if (code == 1)
		{
			LoginEntity mLoginEntity = new LoginEntity();
			mLoginEntity.setUsername(username);
			mLoginEntity.setPassword(password);
			MyApplication.getInstance().setLoginEntity(mLoginEntity);

			PreferencesUtils.putString(LoginActivity.this, Constant.USERNAME,
					username);
			PreferencesUtils.putString(LoginActivity.this, Constant.PASSWORD,
					password);
			if (!first)
			{
				setResult(RESULT_OK);
			} else {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
			finish();
		} else {
			ToastUtils.show(this, message);
		}

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		loginPresenter.detachView();
	}

}
