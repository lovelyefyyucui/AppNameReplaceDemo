package com.name.replace;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.name.replace.util.LogUtil;
import com.name.replace.util.ToastUtils;


public class ProfileUpdateActivity extends Base2Activity implements OnClickListener
{
	public static final int TYPE_NICK = 0;
	public static final int TYPE_SIGN = 1;
	private String defaultStr;
	private EditText infoET;
	private int type;
	private TextView tv_update_sava;
	private TextView titleBar;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fx_activity_update_info);
		type = getIntent().getIntExtra("type", 0);
		TAG = "ProfileUpdateActivity";
		defaultStr = getIntent().getStringExtra("default");
		LogUtil.e(TAG, "type=" + type + ",str=" + defaultStr);
		titleBar = (TextView) findViewById(R.id.tv_update_title_bar);
		titleBar.setOnClickListener(this);
		tv_update_sava = (TextView) findViewById(R.id.tv_update_sava);
		tv_update_sava.setOnClickListener(this);
		infoET = (EditText) findViewById(R.id.et_info);
		getEnd();
		if (!TextUtils.isEmpty(defaultStr))
		{
			infoET.setText(defaultStr);
		}
		initView();

	}

	private void getEnd() {
		switch (type)
		{
			case TYPE_NICK:
				infoET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
				break;
			case TYPE_SIGN:
				infoET.getText();
				infoET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
				break;
		}
	}

	private void initView()
	{
		String title = "";
		switch (type)
		{
		case TYPE_NICK:
			title = "修改昵称";
			break;
		case TYPE_SIGN:
			title = "修改个人签名";
			break;
		}
		titleBar.setText(title);
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.tv_update_sava:
			String value = infoET.getText().toString().trim();
			if (!TextUtils.isEmpty(value)){
				Intent intent = new Intent();
				intent.putExtra("value", value);
				LogUtil.e(TAG, "type=" + type + ",str=" + value);
				setResult(RESULT_OK, intent);
				finish();
			}else {
				ToastUtils.show(this, "请输入内容");
			}

			break;
		case R.id.tv_update_title_bar:
			finish();
			break;
		}  
		
	}

}
