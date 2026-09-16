package com.name.replace.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.name.replace.LoginActivity;
import com.name.replace.MainActivity;
import com.name.replace.MyApplication;
import com.name.replace.ProfileActivity;
import com.name.replace.R;
import com.name.replace.SetActivity;
import com.name.replace.domain.LoginEntity;
import com.name.replace.service.Constant;
import com.name.replace.ui.CircleImageView;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.util.StringUtils;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener{
	private Activity context;
	private View view;
	private String TAG = "ProfileFragment";
	private TextView name, rank, focus, integral;
	private TextView tv_history;
	private CircleImageView person_circleImageView;
	private String username = "";
	private String password = "";

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_profilt, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
	    ViewGroup parent = (ViewGroup) view.getParent();
	    if (parent != null)  
	    {  
	        parent.removeView(view);
	    }
		username = PreferencesUtils.getString(context, Constant.USERNAME);
		password = PreferencesUtils.getString(context, Constant.PASSWORD);
		initUI();
		initData();

		return view;
	}


	public void refresh(){

	}

	private void initData(){
		if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password))
		{
			name.setText(username);
		}
	}

	private void initUI() {
		person_circleImageView = (CircleImageView) view.findViewById(R.id.person_circleImageView);
		person_circleImageView.setOnClickListener(this);
		view.findViewById(R.id.personal_center_user_btn).setOnClickListener(this);
		view.findViewById(R.id.personal_center_integral_btn).setOnClickListener(this);
		view.findViewById(R.id.personal_center_history).setOnClickListener(this);
		view.findViewById(R.id.personal_center_focus_btn).setOnClickListener(this);
		view.findViewById(R.id.personal_center_modify_info).setOnClickListener(this);
		view.findViewById(R.id.personal_center_setting).setOnClickListener(this);
		view.findViewById(R.id.rl_person_coupond).setOnClickListener(this);
		view.findViewById(R.id.rl_persion_vip).setOnClickListener(this);
		view.findViewById(R.id.rl_persion_award).setOnClickListener(this);
		view.findViewById(R.id.rl_person_point).setOnClickListener(this);
		view.findViewById(R.id.rl_persion_order).setOnClickListener(this);
		view.findViewById(R.id.rl_persion_charge).setOnClickListener(this);
		view.findViewById(R.id.rl_person_addr).setOnClickListener(this);
		name = (TextView) view.findViewById(R.id.tv_name);
		rank = (TextView) view.findViewById(R.id.tv_rank);
		focus = (TextView) view.findViewById(R.id.personal_center_user_focus);
		integral = (TextView) view.findViewById(R.id.personal_center_user_integral);
		tv_history = (TextView) view.findViewById(R.id.personal_center_user_history);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		Intent _intent = null;
		switch (v.getId())
		{
			case R.id.person_circleImageView:
//				_intent = new Intent(this, UserManageActivity.class);
				break;
			case R.id.personal_center_integral_btn:
//				_intent = new Intent(this, IntegralDetailActivity.class);
				break;
			case R.id.personal_center_history:
//				_intent = new Intent(this, ScanLogActivity.class);
				break;
			case R.id.personal_center_focus_btn:
//				_intent = new Intent(this, FoucsActivity.class);
				break;
			case R.id.personal_center_modify_info:
                Intent i = new Intent(context, ProfileActivity.class);
                startActivityForResult(i, 9);
				break;
			case R.id.personal_center_setting:
//				_intent = new Intent(context, SetActivity.class);
				Intent intent = new Intent(context, SetActivity.class);
				startActivityForResult(intent, 10);
				break;
			case R.id.rl_persion_order:
//				_intent = new Intent(this, OrderListActivity.class);
				break;
			case R.id.rl_person_addr:
//				_intent = new Intent(this, AddressManageActivity.class);
				break;
			case R.id.rl_person_coupond:
//				_intent = new Intent(this, CouponActivity.class);
				break;
			case R.id.rl_persion_vip:
//				_intent = new Intent(this, MymemberActivity.class);
				break;
			case R.id.rl_persion_award:

				break;
			case R.id.rl_person_point:
//				_intent = new Intent(this, HongActivity.class);
				break;
			case R.id.rl_persion_charge:

				break;
		}
		if (_intent != null) {
			startActivity(_intent);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.e(TAG, "onActivityResult");
		switch (requestCode) {
            case 9:

                break;
			case 10:
				if (resultCode == RESULT_OK)
				{
					LogUtil.e(TAG, "to login");
					Intent intent = new Intent(context, LoginActivity.class);
					startActivityForResult(intent, 11);
				}
				break;
			case 11:
				if (MainActivity.instance != null)
				{
					MainActivity.instance.jump(resultCode, requestCode);
				}
				break;
		}
	}




}
