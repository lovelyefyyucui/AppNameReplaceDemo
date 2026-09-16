package com.name.replace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.name.replace.service.Constant;
import com.name.replace.ui.BaseDialog;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.util.UILRequestManager;


public class SetActivity extends Base2Activity implements OnClickListener {
    private TextView title;
    private ImageView titleimgLeft;
    private RelativeLayout mSafety;
    private RelativeLayout mClearcache;
    private RelativeLayout mHelp;
    private RelativeLayout mAboutus;
    private RelativeLayout mNotifications;
    private RelativeLayout bt_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting);
        TAG = "SetActivity";
        title = (TextView) findViewById(R.id.title_top);
        title.setText("设置");
        titleimgLeft = (ImageView) findViewById(R.id.title_left);
        titleimgLeft.setImageResource(R.mipmap.top_back);
        titleimgLeft.setOnClickListener(this);
        mNotifications = (RelativeLayout) findViewById(R.id.re_notifications_setting);
//        mChat = (RelativeLayout) findViewById(R.id.re_chat_setting);
        mSafety = (RelativeLayout) findViewById(R.id.re_safety_setting);
        mClearcache = (RelativeLayout) findViewById(R.id.re_clearcache_setting);
        mHelp = (RelativeLayout) findViewById(R.id.re_help_setting);
        mAboutus = (RelativeLayout) findViewById(R.id.re_aboutus_setting);
        bt_exit = (RelativeLayout) findViewById(R.id.re_exit);
        mNotifications.setOnClickListener(this);
        mSafety.setOnClickListener(this);
        mClearcache.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mAboutus.setOnClickListener(this);
        bt_exit.setOnClickListener(this);


    }

    public void back(View view) {
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_safety_setting:
                startActivity(new Intent(this, SetPasswordActivity.class));
                break;
            case R.id.re_clearcache_setting:
                clean();
                mClearcache.setClickable(false);
                break;
            case R.id.re_help_setting:
//                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.re_aboutus_setting:
//                Intent intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra(Constant.Code.TITLE, "关于我们");
//                intent.putExtra(Constant.Code.URL, "guanyu");
//                startActivity(intent);
                break;

            case R.id.re_notifications_setting:
//                startActivity(new Intent(this, SetNotifyActivity.class));
                break;
            case R.id.re_exit:
                logout();
                break;
            case R.id.title_left:
                finish();
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    private void logout() {
        MyApplication.getInstance().setLoginEntity(null);
//        PreferencesUtils.putString(this, Constant.USERNAME, "");
        PreferencesUtils.putString(this, Constant.PASSWORD, "");
        setResult(RESULT_OK);
        finish();
    }



    private BaseDialog exitDialog;

    private void clean() {
        exitDialog = new BaseDialog(this);
        exitDialog.setTitle("提醒");
        exitDialog.setMessage("是否清除缓存？");
        exitDialog.setButton1("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (exitDialog.isShowing()) {
						LogUtil.e(TAG,"DataCleanManager"+SetActivity.this.getExternalCacheDir().getAbsolutePath());//mnt/sdcard/android/data/包名/catch
                        exitDialog.dismiss();
                        UILRequestManager.clearCache();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "缓存已经清除" , Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        LinearLayout toastView = (LinearLayout) toast.getView();
                        ProgressBar mBar = new ProgressBar(getApplicationContext());
                        mBar.setIndeterminate(true);
                        toastView.addView(mBar, 0);
                        toast.show();
                    }
                }
            }
        });
        exitDialog.setButton2("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 1) {
                    if (exitDialog.isShowing()) {
                        exitDialog.dismiss();
                    }
                }
            }
        });
        exitDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exitDialog != null)
        {
            if (exitDialog.isShowing()) {
                exitDialog.dismiss();
            }
            exitDialog = null;
        }

    }
}

