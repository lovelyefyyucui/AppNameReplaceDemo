package com.name.replace.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/*
 * 屏幕分辨率px跟dip互相转化
 */
public class DensityUtil {

	/*
	 * 将dip转化为px像素
	 */
	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	/*
	 * 将px像素转化为dip
	 */
	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}

	/**
	 * 将bitmap转为Base64字符串
	 *
	 * @param bitmap
	 * @return base64字符串
	 */
	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
		byte[] bytes = outputStream.toByteArray();
		//Base64算法加密，当字符串过长（一般超过76）时会自动在中间加一个换行符，字符串最后也会加一个换行符。
		// 导致和其他模块对接时结果不一致。所以不能用默认Base64.DEFAULT，而是Base64.NO_WRAP不换行
		return Base64.encodeToString(bytes, Base64.NO_WRAP);
	}

	/**
	 * 将base64字符串转为bitmap
	 * @param base64String
	 * @return bitmap
	 */
	public static Bitmap base64ToBitmap(String base64String) {

		byte[] bytes = Base64.decode(base64String, Base64.NO_WRAP);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bitmap;

	}

	public static int getVersionCode(Context context)
	{
		PackageManager manager;
		PackageInfo info = null;

		manager = context.getPackageManager();

		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();

		}

		int versionCode = info.versionCode;

//		info.versionName;
//		info.packageName;
//		info.signatures;

		return versionCode;
	}

	public static JSONObject getJSONObject(String str) {
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(str);
		} catch (JSONException e) {
			LogUtil.e(e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}


}
