package com.name.replace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.name.replace.area.GetProvinceActivity;
import com.name.replace.domain.LoginEntity;
import com.name.replace.service.Constant;
import com.name.replace.ui.CircleImageView;
import com.name.replace.ui.FXAlertDialog;
import com.name.replace.util.BaseUtils;
import com.name.replace.util.BitmapUtils;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.util.StringUtils;
import com.name.replace.util.UILRequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends Base2Activity implements TakePhoto.TakeResultListener,InvokeListener,View.OnClickListener {
    private TextView title;
    private ImageView titleimgLeft;
    private CircleImageView iv_avatar;
    private TextView tv_name;
    private TextView tv_fxid;
    private TextView tv_sex;
    private TextView tv_sign;
    private static final int UPDATE_NICK = 5;// 昵称
    private static final int UPDATE_SIGN = 6;// 个性签名
    private static final int BEGIN_CODE = 0;
    private static final int UPDATE_SEX = 8;// 个性签名
    // 头像，昵称，凡信号是否发生变化
    private boolean hasChange = false;
    private ImageView iv_code;
    private TextView tv_area;

    // String mobile;
    private String nick = "";
    private int sex;
    private String area = "";
    private String sign = "";
    private String base64Bitmap;
    private String qr_code;
    private Uri picUri;
    private String mobile;
    private String head_img;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_myinfo);
        TAG = "ProfileActivity";
        initView();
        initData();
    }

    private void initData() {
        if (MyApplication.getInstance().getLoginEntity() != null)
        {
            LoginEntity data = MyApplication.getInstance().getLoginEntity();
            if (TextUtils.isEmpty(data.getMobile())) {
                tv_fxid.setText(PreferencesUtils.getString(this, Constant.KEY_mobile, ""));
            } else {
                mobile = data.getMobile();
                tv_fxid.setText(mobile);
                LogUtil.e(TAG, "tv_fxid===" + data.getMobile());
            }
            if (TextUtils.isEmpty(data.getPicture())) {
                iv_avatar.setImageResource(R.mipmap.logo);
            } else {
                head_img = data.getPicture();
                LogUtil.e(TAG, "head_img==" + head_img);
                UILRequestManager.displayImage(head_img, iv_avatar);
            }
            if (TextUtils.isEmpty(data.getQrcode())) {
                iv_code.setImageResource(R.drawable.fx_icon_qrcode);
            } else {
                qr_code = data.getQrcode();
                UILRequestManager.displayImage(qr_code, iv_code);
            }
            if (TextUtils.isEmpty(data.getNickname())) {
                nick = "未设置";
            } else {
                nick = data.getNickname();
            }
            tv_name.setText(nick);
            int sex = data.getSex();
            if (Constant.MAN == sex) {
                this.sex = 0;
                tv_sex.setText("男");
            } else if (Constant.WOMAN == sex) {
                this.sex = 1;
                tv_sex.setText("女");
            }
            if (TextUtils.isEmpty(data.getSign())) {
                sign = " ";
            } else {
                sign = data.getSign();
            }
            tv_sign.setText(sign);
            if (TextUtils.isEmpty(data.getArea())) {
                area = "未设置";
                tv_area.setText(area);
            } else {
                area = data.getArea();
                tv_area.setText(data.getArea());
            }
        }

        LogUtil.e(TAG + "initData", "nick=" + nick + ",area=" + area + ",sign=" + sign + ",sex=" + sex + ",mobile=" + mobile + ",qr_code" + qr_code + "\n, head_img" + head_img);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_top);
        title.setText("个人信息");
        titleimgLeft = (ImageView) findViewById(R.id.title_left);
        titleimgLeft.setImageResource(R.mipmap.top_back);
        titleimgLeft.setOnClickListener(this);
        iv_avatar = (CircleImageView) this.findViewById(R.id.iv_myinfo_avatar);
        iv_code = (ImageView) this.findViewById(R.id.iv_myinfo_code);
        tv_name = (TextView) this.findViewById(R.id.tv_myinfo_name);//昵称
        tv_fxid = (TextView) this.findViewById(R.id.tv_myinfo_fxid);//手机号gone了
        tv_sex = (TextView) this.findViewById(R.id.tv_myinfo_sex);
        tv_sign = (TextView) this.findViewById(R.id.tv_myinfo_sign);
        tv_area = (TextView) this.findViewById(R.id.iv_myinfo_area);
        // 设置监听
        iv_avatar.setOnClickListener(this);
        findViewById(R.id.re_avatar).setOnClickListener(this);
        findViewById(R.id.re_name).setOnClickListener(this);
        findViewById(R.id.re_sex).setOnClickListener(this);
        findViewById(R.id.re_area).setOnClickListener(this);
        findViewById(R.id.re_sign).setOnClickListener(this);
        findViewById(R.id.re_qrcode).setOnClickListener(this);
        findViewById(R.id.re_address).setOnClickListener(this);// 暂放
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.re_avatar:
                showPhotoDialog();
                break;
            case R.id.iv_myinfo_avatar:
                if (StringUtils.isNotEmpty(head_img))
                {
                    ArrayList<String> photoAvatar = new ArrayList<>();
                    photoAvatar.add(head_img);
                    startActivity(new Intent(ProfileActivity.this, ShowPhotoActivity.class)
                            .putStringArrayListExtra("imgData", photoAvatar)
                            .putStringArrayListExtra("thumb", photoAvatar)
                            .putExtra("mIndex", 0));
                }
                break;
            case R.id.re_name:
                intent = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
                intent.putExtra("type", ProfileUpdateActivity.TYPE_NICK);
                intent.putExtra("default", tv_name.getText().toString().trim());
                LogUtil.e(TAG, "type=" + 0 + tv_name.getText().toString().trim());
                startActivityForResult(intent, UPDATE_NICK);
                break;
            case R.id.re_sex:
                showSexDialog();
                break;
            case R.id.re_area:// 地区
                intent = new Intent(ProfileActivity.this,
                        GetProvinceActivity.class);
                startActivityForResult(intent, BEGIN_CODE);
                break;
            case R.id.re_sign:
                intent = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
                intent.putExtra("type", ProfileUpdateActivity.TYPE_SIGN);
                String sign = tv_sign.getText().toString().trim();
                if (TextUtils.isEmpty(sign)){
                    sign = "";
                }
                intent.putExtra("default", sign);
                LogUtil.e(TAG, "type=" + sign);
                startActivityForResult(intent, UPDATE_SIGN);
                break;
            case R.id.re_qrcode:
//                intent = new Intent(ProfileActivity.this, MyQrActivity.class);
//                intent.putExtra("qrcodestr", qr_code);
//                if (TextUtils.isEmpty(head_img)) {
//                    head_img = "";
//                }
//                intent.putExtra(Constant.key.USER_IMG, head_img);
//                intent.putExtra(Constant.key.NAME, nick);
//                if (TextUtils.isEmpty(area)) {
//                    area = "未设置";
//                }
//                intent.putExtra(Constant.key.JSON_KEY_ADDRESS, area);
//                intent.putExtra(Constant.key.JSON_KEY_SEX, sex);
//                startActivity(intent);
                break;
            case R.id.re_address:
                LogUtil.e(TAG, "nick=" + nick + ",area=" + area + ",sex=" + sex);
                break;

            case R.id.title_left:
//                if (MyApplication.getInstance().mLoginEntity != null)
//                {
//                    setResult(RESULT_OK);
//                }
                finish();
                break;
        }

    }

    private void showSexDialog() {
        String title = "性别";
        List<String> items = new ArrayList<String>();
        items.add("男");
        items.add("女");
        FXAlertDialog fxAlertDialog = new FXAlertDialog(ProfileActivity.this, title, items);
        fxAlertDialog.init(new FXAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        tv_sex.setText("男");
                        sex = 0;
                        updateInServer(nick, sex, area, sign);
                        break;
                    case 1:
                        tv_sex.setText("女");
                        sex = 1;
                        updateInServer(nick, sex, area, sign);
                        break;
                    default:
                        break;

                }
            }
        });

    }

    @SuppressLint("NewApi")
    private void showPhotoDialog() {
        List<String> items = new ArrayList<String>();
        items.add("拍照");
        items.add("相册");
        FXAlertDialog fxAlertDialog = new FXAlertDialog(ProfileActivity.this, null, items);
        fxAlertDialog.init(new FXAlertDialog.OnItemClickListner() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(int position) {
                File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+ (System.currentTimeMillis() / 1000) + ".jpg");
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                picUri = Uri.fromFile(file);
                switch (position) {
                    case 0:
                        takePhoto.onPickFromCaptureWithCrop(picUri,getCropOptions(480,480));
                        break;
                    case 1:
                        takePhoto.onPickFromGalleryWithCrop(picUri,getCropOptions(480,480));
                        break;
                }
            }
        });
    }


    private Bitmap bitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        getTakePhoto().onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPDATE_NICK:// 昵称
                    nick = intent.getStringExtra("value");
                    if (nick != null) {
                        tv_name.setText(nick);
                    }
                    updateInServer(nick, sex, area, sign);
                    break;
                case BEGIN_CODE:// 地区
                    Bundle bundle = intent.getExtras();
                    String province = bundle.getString("province");
                    String cityName = bundle.getString("cityName");
                    // 出发地
                    area = province + " " + cityName;
                    tv_area.setText(area);
                    updateInServer(nick, sex, area, sign);
                    break;
                case UPDATE_SIGN:// 签名
                    sign = intent.getStringExtra("value");
                    if (sign != null) {
                        tv_sign.setText(sign);
                    }
                    updateInServer(nick, sex, area, sign);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, intent);

        }
    }

    private void updateHeadImg(final String key, final String value) {
        LogUtil.e("key=" + key + ",value=" + value);
//        iv_avatar.setImageBitmap(bitmap);
        head_img = picUri.toString();
        LogUtil.e("head_img=" + head_img);
        UILRequestManager.displayImage(head_img, iv_avatar);
    }

    private void updateInServer(final String nick, final int sex, final String area, final String sign) {
        LogUtil.e("nick=" + nick + ",sex=" + sex + ",area=" + area + ",sign=" + sign);

    }

    public void back(View view) {
        ckeckChange();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ckeckChange();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ckeckChange() {

        if (hasChange) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }
    @Override
    public void takeSuccess(TResult result) {
        LogUtil.e(TAG,"takeSuccess：" + result.getImage().getCompressPath());
        String path = BaseUtils.getImageAbsolutePath(this, picUri);
        bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            base64Bitmap = BitmapUtils.encodeTobase64(bitmap);
            if (!TextUtils.isEmpty(base64Bitmap)) {
                updateHeadImg(Constant.HEAD_IMG, base64Bitmap);
            }
        }
    }
    @Override
    public void takeFail(TResult result,String msg) {
        LogUtil.e(TAG, "takeFail:" + msg);
    }
    @Override
    public void takeCancel() {
        LogUtil.e(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    private CropOptions getCropOptions(int width,int height){
        CropOptions.Builder builder=new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(false);
        return builder.create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }
}
