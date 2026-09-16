package com.name.replace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.name.replace.fragment.Chat;
import com.name.replace.fragment.ProfileFragment;
import com.name.replace.fragment.JokeListFragment;
import com.name.replace.fragment.Other;
import com.name.replace.fragment.Read;
import com.name.replace.service.Constant;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.util.StringUtils;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private TextView title;
    private ImageView rightIv;
    private RadioGroup rgBar;
    private long firstBack;
    private Read read;
    private JokeListFragment jokeListFragment;
    private Chat chat;
//    private Other other;
    private ProfileFragment profileFragment;
    private String username = "";
    private String password = "";

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.main);
        TAG = "MainActivity";
        instance = this;
        findViewById(R.id.title_left_ll).setVisibility(View.GONE);
        findViewById(R.id.title_right_ll).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_top);
        rightIv = (ImageView) findViewById(R.id.title_right);
        rightIv.setImageResource(R.drawable.h_logo);
        rgBar = (RadioGroup) findViewById(R.id.rg_main_bar);

        fragmentManager = getSupportFragmentManager();
        rgBar.setOnCheckedChangeListener(this);
        rgBar.check(R.id.main_tab_more);
        // 第一次启动时选中第0个tab
        setTabSelection(R.id.main_tab_more);
        String registrationId = PreferencesUtils.getString(this, Constant.DEVICE_ID);
        if (TextUtils.isEmpty(registrationId)){
            registrationId = JPushInterface.getRegistrationID(this);
            if (!TextUtils.isEmpty(registrationId)){
                LogUtil.e(TAG, "Registration Id : " + registrationId);
                PreferencesUtils.putString(this, Constant.DEVICE_ID, registrationId);
            }
        }
    }


    private FragmentTransaction transaction;

    private void setTabSelection(int checkedId) {
        preCheckedId = checkedId;
        // 开启一个Fragment事务
        transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
//				hideFragments(transaction);
        switch (checkedId) {
            case R.id.main_tab_more:

                title.setText("首页");
                if (jokeListFragment == null) {
                    jokeListFragment = new JokeListFragment();
                    transaction.add(R.id.container, jokeListFragment);
                }else {
                    transaction.show(jokeListFragment);
                }
                jokeListFragment.refresh();

                if (read != null) {
                    transaction.hide(read);
                }

                if (chat != null) {
                    transaction.hide(chat);
                }

//                if (other != null) {
//                    transaction.hide(other);
//                }

                if (profileFragment != null) {
                    transaction.hide(profileFragment);
                }
                break;

            case R.id.main_tab_works:

                title.setText("大道商城");
                if (read == null) {
                    read = new Read();
                    transaction.add(R.id.container, read);
                }else {
                    transaction.show(read);
                }
                read.refresh();

                if (jokeListFragment != null) {
                    transaction.hide(jokeListFragment);
                }

                if (chat != null) {
                    transaction.hide(chat);
                }

//                if (other != null) {
//                    transaction.hide(other);
//                }

                if (profileFragment != null) {
                    transaction.hide(profileFragment);
                }

                break;

            case R.id.main_tab_generate:
                title.setText("联盟商家");
				if (chat == null) {
                    chat = new Chat();
					transaction.add(R.id.container, chat);
                }else {
					transaction.show(chat);
				}

                if (read != null) {
                    transaction.hide(read);
                }

                if (jokeListFragment != null) {
                    transaction.hide(jokeListFragment);
                }


//                if (other != null) {
//                    transaction.hide(other);
//                }

                if (profileFragment != null) {
                    transaction.hide(profileFragment);
                }
                break;

            case R.id.main_tab_chat:

                title.setText("我的");

//                if (other == null) {
//                    other = new Other();
//                    transaction.add(R.id.container, other);
//                }else {
//                    transaction.show(other);
//                }
//                other.refresh();

                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                    transaction.add(R.id.container, profileFragment);
                }else {
                    transaction.show(profileFragment);
                }
                profileFragment.refresh();


                if (read != null) {
                    transaction.hide(read);
                }

                if (jokeListFragment != null) {
                    transaction.hide(jokeListFragment);
                }

                if (chat != null) {
                    transaction.hide(chat);
                }

                break;

            default:
                break;
        }
        transaction.commit();
    }

    private int preCheckedId;


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.main_tab_chat)
        {
            username = PreferencesUtils.getString(this, Constant.USERNAME);
            password = PreferencesUtils.getString(this, Constant.PASSWORD);
            if (TextUtils.isEmpty(password))
            {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 0);
                ((RadioButton) findViewById(checkedId)).setChecked(false);
            }else {
                LogUtil.e("username=" + username);
                ((RadioButton) findViewById(checkedId)).setChecked(true);
                setTabSelection(checkedId);
            }
        } else {
            setTabSelection(checkedId);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondBack = System.currentTimeMillis();
            if (secondBack - firstBack > 2000) {
                Toast.makeText(this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstBack = secondBack;
                return true;
            } else {
                PreferencesUtils.putLong(MainActivity.this, Constant.EXIT_TIME, System.currentTimeMillis());
                ActManager.getAppManager().AppExit(MainActivity.this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            jump(resultCode, requestCode);

    }

    public void jump(int resultCode, int requestCode) {
        if (requestCode == 0)
        {
            if (resultCode != RESULT_OK) {
                ((RadioButton) findViewById(preCheckedId)).setChecked(true);
                setTabSelection(preCheckedId);
            } else {
                ((RadioButton) findViewById(R.id.main_tab_chat)).setChecked(true);
                setTabSelection(R.id.main_tab_chat);
            }
        } else if (requestCode == 11){
            if (resultCode != RESULT_OK)
            {
                ((RadioButton) findViewById(R.id.main_tab_more)).setChecked(true);
                setTabSelection(R.id.main_tab_more);
            } else {
                ((RadioButton) findViewById(R.id.main_tab_chat)).setChecked(true);
                setTabSelection(R.id.main_tab_chat);
            }

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.title_right_ll:
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("isHide", true);
                startActivity(intent);
                break;
        }
    }
}
