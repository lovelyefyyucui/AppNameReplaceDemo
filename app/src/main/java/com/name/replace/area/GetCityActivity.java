package com.name.replace.area;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.name.replace.Base2Activity;
import com.name.replace.ProfileActivity;
import com.name.replace.R;

import java.util.ArrayList;

public class GetCityActivity extends Base2Activity {

	private ListView lv_get_city;
	private ImageView btn_get_city;
	private TextView tv_getCity_province;

	//市级list
	ArrayList<String> list ;
	String province; //省
	GetCityAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_city);
		TAG = "GetCityActivity";

		lv_get_city = (ListView) findViewById(R.id.lv_get_city);
		btn_get_city = (ImageView) findViewById(R.id.btn_get_city);
		tv_getCity_province = (TextView) findViewById(R.id.tv_getCity_province);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		province = bundle.getString("province");
		list = bundle.getStringArrayList("city");

		//设置省名；
		tv_getCity_province.setText("省：" + province);
		adapter = new GetCityAdapter(getApplicationContext(), list);
		lv_get_city.setAdapter(adapter);

		//返回按钮
		btn_get_city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intentN = new Intent(GetCityActivity.this,GetProvinceActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("province", province);
				bundle.putString("cityName", "");
				intentN.putExtras(bundle);
				setResult(RESULT_OK,intentN);
//				startActivity(intent2);
				finish();

			}
		});

		//点击item
		lv_get_city.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
				//所点item的市级名称
				String cityName = list.get(position);
				//将省名及所点item的市名传给下级
				Intent intent2 = new Intent(GetCityActivity.this,ProfileActivity.class);
				Bundle bundle2 = new Bundle();
				bundle2.putString("province", province);
				bundle2.putString("cityName", cityName);
				intent2.putExtras(bundle2);
				setResult(RESULT_OK,intent2);
//				startActivity(intent2);
				finish();
			}
		});
		
	}

	

}
