package com.name.replace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.name.replace.service.Constant;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class GuideActivity extends Base2Activity implements View.OnClickListener {
    private ViewPager guideViewPager;
    private List<View> viewContainer;
//    private String latitude;
//    private String longitude;
    private String registration_id = "";
    private final int REQUEST_CODE_ASK_LOCATION_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        TAG = "GuideActivity";
        PreferencesUtils.putBoolean(this, Constant.IS_FIRST, false);
        viewContainer = new ArrayList<View>();
//        latitude = SPUtils.getValue(getBaseContext(), Constant.key.LATITUDE);
//        longitude = SPUtils.getValue(getBaseContext(), Constant.key.LONGITUDE);
        registration_id = PreferencesUtils.getString(this, Constant.DEVICE_ID);
        if (TextUtils.isEmpty(registration_id)) {
            registration_id = JPushInterface.getRegistrationID(this);
            PreferencesUtils.putString(this, Constant.DEVICE_ID, registration_id);
        }
        LogUtil.e(TAG, "device_id=" + registration_id);
        initView();
        initData();
    }
    private void initView() {
        guideViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        View view1 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_child_pager_item_1, null);
        View view2 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_child_pager_item_2, null);
        View view3 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_child_pager_item_3, null);
        view1.findViewById(R.id.img_skip_).setOnClickListener(this);
        view2.findViewById(R.id.img_skip_).setOnClickListener(this);
        view3.findViewById(R.id.img_skip_).setOnClickListener(this);
        view3.findViewById(R.id.img_guide_experience).setOnClickListener(this);
        viewContainer.add(view1);
        viewContainer.add(view2);
        viewContainer.add(view3);

    }

    private void initData() {
        guideViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewContainer.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainer.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainer.get(position));
                return viewContainer.get(position);
            }
        });

//        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                PackageManager pkm = getPackageManager();
//                int checkPermission = pkm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this.getPackageName());
//                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            REQUEST_CODE_ASK_LOCATION_PERMISSIONS);
//                }else {
//                    if (!BaseUtils.gPSIsOPen(this)) {
//                        BaseUtils.openGPS(this);
//                    }
//                    DemoApplication.locateStart();
//                }
//            }else {
//                if (!BaseUtils.gPSIsOPen(this)) {
//                    BaseUtils.openGPS(this);
//                }
//                DemoApplication.locateStart();
//            }
//        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_LOCATION_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (!BaseUtils.gPSIsOPen(this)) {
//                    BaseUtils.openGPS(this);
//                }
//                DemoApplication.locateStart();
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        intent.putExtra(Constant.IS_FIRST, true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (DemoApplication.locationUtil != null)
//            DemoApplication.locationUtil.stopMonitor(false);
    }

}
