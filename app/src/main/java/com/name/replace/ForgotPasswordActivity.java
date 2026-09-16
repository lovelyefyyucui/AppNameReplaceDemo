package com.name.replace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.name.replace.presenter.GetCodePresenter;
import com.name.replace.presenter.impl.GetCodePresenterImpl;
import com.name.replace.service.Constant;
import com.name.replace.util.BaseUtils;
import com.name.replace.util.LogUtil;
import com.name.replace.util.RegularUtils;
import com.name.replace.util.ToastUtils;
import com.name.replace.view.BaseDataView;

import java.util.TreeMap;

public class ForgotPasswordActivity extends Base2Activity implements OnClickListener, BaseDataView {
	private EditText mPhone;
	private EditText mCode;
	private TextView mNext;
	private TextView title;
	private ImageView titleimgLeft;
	private String tip;
	private String codeString;
	private String phone;
	private GetCodePresenter getCodePresenter;
	private ImageView img_nameClear;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		TAG = "ForgotPasswordActivity";
		initView();
		getCodePresenter = new GetCodePresenterImpl(this, "getCode");
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_top);
		title.setText("找回密码");
		titleimgLeft = (ImageView) findViewById(R.id.title_left);
		titleimgLeft.setImageResource(R.mipmap.top_back);
		titleimgLeft.setOnClickListener(this);
		mPhone = (EditText) findViewById(R.id.forgot_input_phone);
		mCode = (EditText) findViewById(R.id.et_code);
		mNext = (TextView) findViewById(R.id.forgot_next);
		img_nameClear = (ImageView) findViewById(R.id.img_phone_delete_retrieve);
		mNext.setOnClickListener(this);
		findViewById(R.id.imageView1).setOnClickListener(this);
		mPhone.addTextChangedListener(new MyTextWatcher(mPhone));
		mCode.addTextChangedListener(new MyTextWatcher(mCode));
		img_nameClear.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forgot_next:
			if (!isNameValid() || !isCodeValid()) {
				ToastUtils.show(this,tip);
				return;
			}
			getNext();
			break;
		case R.id.imageView1:
			if (!isNameValid()) {
				ToastUtils.show(this,tip);
				return;
			}
			getCode();
			break;
		case R.id.title_left:
			finish();
			break;
		case R.id.img_phone_delete_retrieve:
			mPhone.setText(null);
			break;
		}
	}

	private void getNext() {
		if (!isNameValid() || !isCodeValid()) {
			ToastUtils.show(this,tip);
			return;
		}
		Intent intent = new Intent(this, SetPasswordActivity.class);
		intent.putExtra(Constant.KEY_mobile, phone);
		intent.putExtra(Constant.KEY_qr_code, codeString);
		startActivityForResult(intent, 0);
	}


	private void getCode()
	{
		TreeMap<String, String> postPairs = new TreeMap<String, String>();
		postPairs.put("mobile", phone);
		postPairs.put("verifyType", "resetPwd");
		getCodePresenter.getCode(BaseUtils.getMap(postPairs));
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0)
		{
			if (resultCode == RESULT_OK) {
				finish();
			}
		}

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
				case R.id.forgot_input_phone:
					isNameValid();
					break;
				case R.id.et_code:
					isCodeValid();
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
		phone = mPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			tip = getString(R.string.empty_phone);
			mPhone.requestFocus();
			return false;
		} else if (!RegularUtils.isPhone(phone))
		{
			tip = getString(R.string.error_phone);
			mPhone.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * 检查输入的验证码是否为空
	 *
	 * @return
	 */
	public boolean isCodeValid() {
		codeString = mCode.getText().toString().trim();
		if (TextUtils.isEmpty(codeString)) {
			tip = getString(R.string.empty_code);
			mCode.requestFocus();
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
		ToastUtils.show(this, message);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getCodePresenter.detachView();
	}
}
