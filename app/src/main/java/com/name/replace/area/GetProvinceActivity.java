package com.name.replace.area;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.name.replace.Base2Activity;
import com.name.replace.ProfileActivity;
import com.name.replace.R;
import com.name.replace.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetProvinceActivity extends Base2Activity implements View.OnClickListener {
	private TextView title;
	private ImageView titleimgLeft;
	private ListView lv_getProvince;

	GetProvinceAdapter adapter;
	List<String> mProvinces;// 所有的省
	// 省+对应的市
	private Map<String, List<String>> mCityMap = new HashMap<String, List<String>>();
	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONObject mJsonObj;
	private JSONArray jsonArray;

	//item 的省名
	private String provinceI;
	//item 的市集合
	private ArrayList<String> cityI;
	private List<String> cityO;//获取map中的市list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取布局文件
		setContentView(R.layout.activity_get_provice);
		TAG = "GetProvinceActivity";
		title = (TextView) findViewById(R.id.title_top);
		title.setText("选择省份");
		titleimgLeft = (ImageView) findViewById(R.id.title_left);
		titleimgLeft.setImageResource(R.mipmap.top_back);
		titleimgLeft.setOnClickListener(this);
		lv_getProvince = (ListView) findViewById(R.id.lv_get_province);

		mProvinces = new ArrayList<String>();
		// 获取json数据
		initJsonData();
		// 解析json
		initData();

		adapter = new GetProvinceAdapter(getApplicationContext(), mProvinces);
		lv_getProvince.setAdapter(adapter);

		/// 点击item
		lv_getProvince.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
				//获取点击的item的省份
				provinceI = mProvinces.get(position);
				Toast.makeText(getApplicationContext(), provinceI,
						Toast.LENGTH_LONG).show();
				System.out.println(provinceI);
				cityI = new ArrayList<String>();
				//获取点击的item的省份对应的市list
				 cityO= mCityMap.get(provinceI);

				for (int i = 0; i < cityO.size(); i++) {
					cityI.add(cityO.get(i));
				}

				Intent intent = new Intent(GetProvinceActivity.this,
						GetCityActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("province", provinceI);
				bundle.putStringArrayList("city", cityI);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			String province = bundle.getString("province");
			String cityName = bundle.getString("cityName");
			Intent intentSet = new Intent(GetProvinceActivity.this,ProfileActivity.class);
			Bundle bundleSet = new Bundle();
			bundleSet.putString("province", province);
			bundleSet.putString("cityName", cityName);
			intentSet.putExtras(bundleSet);
			setResult(RESULT_OK,intentSet);
			finish();
		}

	}

	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is =GetProvinceActivity.this.getAssets().open("city.json");
			InputStreamReader isr=new InputStreamReader(is);
			BufferedReader buffer = new BufferedReader(isr);//缓冲
//			byte[] buf = new byte[1024];
			String line;
			while((line = buffer.readLine())!=null){
				LogUtil.e(TAG, "CITY=" + line );
				sb.append(line);
			}
//			int len = 0;
//			while ((len = is.read(buf)) != -1) {
//				sb.append(new String(buf, 0, len, "UTF-8"));
//			}
			isr.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// // 解析json对象
	private void initData() {
		try {
			jsonArray = mJsonObj.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = (JSONObject) jsonArray.get(i);
				String provinceJson = jsonP.getString("name");// json文件中省份的标签为"name"
				// 添加到省份的list中
				mProvinces.add(provinceJson);
				LogUtil.e(TAG,"mProvinces=" + mProvinces);

				String city = jsonP.getString("cities");
				// 去除字符串中的特殊符号
				city = city.replaceAll("\"", "");
				city = city.replace("[", "");
				city = city.replace("]", "");
				// 获取市list
				List<String> listCity = Arrays.asList(city.split(","));
				// 将省及对应的市放到map中；
				mCityMap.put(provinceJson, listCity);
				LogUtil.e("GetProViceActivity","mCityMap="+mCityMap);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.title_left:
				finish();
				break;
		}
	}
}
