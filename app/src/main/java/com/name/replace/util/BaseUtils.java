package com.name.replace.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.name.replace.MyApplication;
import com.name.replace.domain.ClientInfo;
import com.name.replace.domain.LoginEntity;
import com.name.replace.service.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtils {

    public static boolean isFriend(int friendsStatus){
        return friendsStatus == 1;
    }

    public static String localTempImgDir = "myDir";
    public static String localTempImgFileName = ".jpg";


    public static Uri initUri() {
        Uri uri = null;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir);
            if (!dir.exists()) dir.mkdirs();
            File f = new File(dir, System.currentTimeMillis() / 1000 + localTempImgFileName);
            uri = Uri.fromFile(f);
        } else {
            LogUtil.e("utils", "no sdcard");
        }
        return uri;
    }


    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern regex = Pattern
                    .compile("^(1[3-9])\\d{9}$");
            Matcher matcher = regex.matcher(mobiles);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证密码
     * @return
     */
    public static boolean isPassWord(String character) {
        boolean flag = false;
        try {
            String check = "[a-zA-Z0-9_]{6,18}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 判断GPS是否开启
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean gPSIsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);GPS或者AGPS开启一个就认为是开启的if (gps || network)
        if (gps) {
            return true;
        }
        return false;
    }



    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }



    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /* 
     * 将时间戳字符串转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    private static final double EARTH_RADIUS = 6378137.0;

    /*
     * 计算两个经纬度直接的距离， 返回单位是米
     */
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 读取arm语音文件
     *
     * @param context
     * @return true 表示开启
     */
    private static MediaPlayer mp = null;
    public static final void speech(Context context, File file) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        try {
            final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(1, 1);
                    mp.start();
                }
            };

            final MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            };

            final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mp = null;
                }
            };
            try {
                if (mp != null)
                    mp.release();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                    Uri uri = Uri.fromFile(file);
                    mp = MediaPlayer.create(context, uri);
                }else {
                    mp = new MediaPlayer();
                    mp.setDataSource(file.getAbsolutePath());
                }
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setOnErrorListener(onErrorListener);
                mp.setOnCompletionListener(onCompletionListener);
                mp.setOnPreparedListener(onPreparedListener);
                mp.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static TreeMap<String, String> getMap(TreeMap<String, String> postPairs)
    {
        String mTimestamp = DateUtil.getCurrentTime();// String.valueOf(System.currentTimeMillis() / 1000);
        postPairs.put("requestTime", mTimestamp);
        ClientInfo info = new ClientInfo();
        info.setDeviceId("imei3333");
        info.setDeviceModel("iphone 5s");
        info.setHeight(960);
        info.setWidth(640);
        info.setOs(1);
        info.setOsVersion("8.4");
        String clientInfoString = GsonUtils.toJson(info, ClientInfo.class);
        postPairs.put("client", clientInfoString);
        postPairs.put("accessId", "1000");
        if (MyApplication.getInstance().getLoginEntity() != null)
        {
            LoginEntity data = MyApplication.getInstance().getLoginEntity();
            postPairs.put("userId", data.getId());
            postPairs.put("token", data.getToken());
        }
        Set<Map.Entry<String, String>> entrySet = postPairs.entrySet();
        // Mac 参数构建macStr
        StringBuilder macStr = new StringBuilder();
        for (Map.Entry<String, String> entry : entrySet) {
            macStr.append(entry.getKey());
            macStr.append("=");
            macStr.append(entry.getValue());
            macStr.append("&");
        }
        LogUtil.e("postPairs:" + postPairs);
        macStr.deleteCharAt(macStr.length() - 1);
        macStr.append(Constant.AccessKey);
        LogUtil.e("before md5:" + macStr);
        String md5Str = MD5Util.getMD5String(macStr.toString());
        postPairs.put("sign",md5Str);
        return postPairs;
    }


}
