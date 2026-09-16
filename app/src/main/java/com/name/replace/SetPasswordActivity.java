package com.name.replace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.name.replace.presenter.ResetPwdPresenter;
import com.name.replace.presenter.impl.ResetPwdPresenterImpl;
import com.name.replace.service.Constant;
import com.name.replace.util.LogUtil;
import com.name.replace.util.ToastUtils;
import com.name.replace.view.BaseDataView;

import java.util.TreeMap;

public class SetPasswordActivity extends Base2Activity implements OnClickListener, BaseDataView {
	private Activity mActivity;
	private EditText mPwd;
	private TextView mSure;
	private TextView title;
	private ImageView titleimgLeft;
	private String tip;
	private String password;
	private ImageView img_passWordClear;
	private ImageView img_passWordType;
	private boolean inputType;
	private String phone;
	private String codeString;
	private ResetPwdPresenter resetPwdPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_password);
		TAG = "SetPasswordActivity";
		mActivity = SetPasswordActivity.this;
		Intent intent = getIntent();
		phone = intent.getStringExtra(Constant.KEY_mobile);
		codeString = intent.getStringExtra(Constant.KEY_qr_code);
		LogUtil.e("phone:" + phone + ",code:" + codeString);
		initView();
		resetPwdPresenter = new ResetPwdPresenterImpl(this, "reset");
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_top);
		title.setText("重设密码");
		titleimgLeft = (ImageView) findViewById(R.id.title_left);
		titleimgLeft.setImageResource(R.mipmap.top_back);
		titleimgLeft.setOnClickListener(this);
		mPwd = (EditText) findViewById(R.id.et_password);
		img_passWordClear = (ImageView) findViewById(R.id.img_password_text_delete);
		img_passWordType = (ImageView) findViewById(R.id.img_eye_password_type);
		mPwd.addTextChangedListener(new MyTextWatcher(mPwd));
		mSure = (TextView) findViewById(R.id.sure_tv);
		mSure.setOnClickListener(this);
		img_passWordClear.setOnClickListener(this);
		img_passWordType.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure_tv:
			if (!isPasswordValid()) {
				ToastUtils.show(this,tip);
				return;
			}
			setNewPwd();
			break;
		case R.id.title_left:
			finish();
			break;
		case R.id.img_password_text_delete:
			mPwd.setText(null);
			break;
		case R.id.img_eye_password_type:
			if(!inputType){
				mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				img_passWordType.setImageResource(R.drawable.icon_eye_pre);
			} else {
				mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				img_passWordType.setImageResource(R.drawable.icon_eye);
			}
			inputType = !inputType;
			break;
		}
	}

	private void setNewPwd() {
		TreeMap<String, String> postPairs = new TreeMap<String, String>();
		postPairs.put("mobile", phone);
		postPairs.put("code", codeString);
		postPairs.put("password", password);
		resetPwdPresenter.setPwd(postPairs);

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
				case R.id.et_password:
					isPasswordValid();
					break;
			}
		}
	}

	/**
	 * 检查输入的密码是否为空
	 *
	 * @return
	 */
	public boolean isPasswordValid() {
		password = mPwd.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			tip = getString(R.string.empty_pwd);
			mPwd.requestFocus();
			return false;
		} else if (password.length() < 6 || password.length() > 20)
		{
			tip = "请输入6-20位数字和字母组合的密码";
			mPwd.requestFocus();
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
			setResult(RESULT_OK);
			finish();
		} else {
			ToastUtils.show(this, message);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		resetPwdPresenter.detachView();
	}
}
