package com.name.replace;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.name.replace.domain.LoginEntity;
import com.name.replace.service.Constant;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.util.RegularUtils;
import com.name.replace.util.ToastUtils;

import java.util.LinkedHashMap;

public class RegisterActivity extends Base2Activity implements OnClickListener {
	private TextView title;
	private ImageView titleimgLeft;
	private TextInputLayout mLayoutPhone, mLayoutPwd, mLayoutCode, mLayoutRecommendPhone;
	private EditText et_username;
	private EditText et_recommend_phoe;
	private EditText et_password;
	private EditText mCode;
	private CheckBox chb;
	private String username = "", recommend_phone;
	private String password = "", code = "";
	private boolean isChecked = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		TAG = "RegisterActivity";
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.title_top);
		title.setText("注册");
		titleimgLeft = (ImageView) findViewById(R.id.title_left);
		titleimgLeft.setImageResource(R.mipmap.top_back);
		titleimgLeft.setOnClickListener(this);
		mLayoutPhone = (TextInputLayout) findViewById(R.id.layout_phone);
		mLayoutRecommendPhone = (TextInputLayout) findViewById(R.id.layout_phone_recommend);
		mLayoutPwd = (TextInputLayout) findViewById(R.id.layout_pwd);
		mLayoutCode = (TextInputLayout) findViewById(R.id.layout_code);
		et_username = (EditText) findViewById(R.id.register_input_phone);
		et_password = (EditText) findViewById(R.id.et_password);
		et_recommend_phoe = (EditText) findViewById(R.id.recommend_input_phone);
		mCode = (EditText) findViewById(R.id.et_code);
		findViewById(R.id.sure_tv).setOnClickListener(this);
		et_username.addTextChangedListener(new MyTextWatcher(et_username));
		et_password.addTextChangedListener(new MyTextWatcher(et_password));
		et_recommend_phoe.addTextChangedListener(new MyTextWatcher(et_recommend_phoe));
		mCode.addTextChangedListener(new MyTextWatcher(mCode));
		chb = (CheckBox)this.findViewById(R.id.chb_agree);
		chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				isChecked = arg1;
			}
		});

	}

	private String tip;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.sure_tv:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
			if (!isNameValid() || !isPasswordValid() || !isCodeValid()) { // || !isPhoneValid()
				ToastUtils.show(this,tip);
				return;
			}
			if (!isChecked)
			{
				ToastUtils.show(this, "请勾选用户注册协议");
				return;
			}
			registerService(username, password);
			break;

		case R.id.title_left:
			finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (MyApplication.getInstance().getLoginEntity() != null)
			{
				setResult(RESULT_OK);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	

	
	private void registerService(final String username, final String password) {
		LinkedHashMap<String, String> postPairs = new LinkedHashMap<String, String>();
		postPairs.put("username", username);
		postPairs.put("password", password);
		LoginEntity loginEntity = new LoginEntity();
		loginEntity.setUsername(username);
		loginEntity.setPassword(password);
		MyApplication.getInstance().setLoginEntity(loginEntity);
		setResult(RESULT_OK);
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
				case R.id.register_input_phone:
					isNameValid();
					break;
				case R.id.et_password:
					isPasswordValid();
					break;
				case R.id.et_code:
					isCodeValid();
					break;
				case R.id.recommend_input_phone:
					isPhoneValid();
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
			mLayoutPhone.setErrorEnabled(true);
			tip = getString(R.string.empty_phone);
			mLayoutPhone.setError(tip);
			et_username.requestFocus();
			return false;
		} else if (!RegularUtils.isPhone(username))
		{
			mLayoutPhone.setErrorEnabled(true);
			tip = getString(R.string.error_phone);
			mLayoutPhone.setError(tip);
			et_username.requestFocus();
			return false;
		}
		mLayoutPhone.setErrorEnabled(false);
		return true;
	}

	/**
	 * 检查输入的手机号码是否为空以及格式是否正确
	 *
	 * @return
	 */
	public boolean isPhoneValid() {
		recommend_phone = et_recommend_phoe.getText().toString().trim();
		if (TextUtils.isEmpty(recommend_phone)) {
			mLayoutRecommendPhone.setErrorEnabled(true);
			tip = getString(R.string.empty_phone);
			mLayoutRecommendPhone.setError(tip);
			et_recommend_phoe.requestFocus();
			return false;
		} else if (!RegularUtils.isPhone(recommend_phone))
		{
			mLayoutRecommendPhone.setErrorEnabled(true);
			tip = getString(R.string.error_phone);
			mLayoutRecommendPhone.setError(tip);
			et_recommend_phoe.requestFocus();
			return false;
		}
		mLayoutRecommendPhone.setErrorEnabled(false);
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
			mLayoutPwd.setErrorEnabled(true);
			tip = getString(R.string.empty_pwd);
			mLayoutPwd.setError(tip);
			et_password.requestFocus();
			return false;
		}
		mLayoutPwd.setErrorEnabled(false);
		return true;
	}

	/**
	 * 检查输入的验证码是否为空
	 *
	 * @return
	 */
	public boolean isCodeValid() {
		code = mCode.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			mLayoutCode.setErrorEnabled(true);
			tip = getString(R.string.empty_code);
			mLayoutCode.setError(tip);
			mCode.requestFocus();
			return false;
		}
		mLayoutCode.setErrorEnabled(false);
		return true;
	}

}
