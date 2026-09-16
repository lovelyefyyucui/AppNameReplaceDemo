package com.name.replace.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.name.replace.MyApplication;
import com.name.replace.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author YFY
 * 
 * @describe UIL图片显示管理类
 */
public class UILRequestManager {
	/**
	 * 注意事项 1.权限必须加入，否则会出错
	 * 2.ImageLoaderConfiguration必须配置并且全局化的初始化这个配置ImageLoader
	 * .getInstance().init(config); 否则也会出现错误提示
	 * 3.ImageLoader是根据ImageView的height，width确定图片的宽高。 4.如果经常出现OOM
	 * ①减少配置之中线程池的大小，(.threadPoolSize).推荐1-5；
	 * ②使用.bitmapConfig(Bitmap.config.RGB_565)代替ARGB_8888;
	 * ③使用.imageScaleType(ImageScaleType.IN_SAMPLE_INT)或者
	 * try.imageScaleType(ImageScaleType.EXACTLY)；
	 * ④避免使用RoundedBitmapDisplayer.他会创建新的ARGB_8888格式的Bitmap对象；
	 * ⑤使用.memoryCache(new WeakMemoryCache())，不要使用.cacheInMemory();
	 */
	private static final int KEY_COMMON_IMAGE = 1;
	private static final int KEY_ROUND_IMAGE = 2;
	private static final int KEY_FEED_IMAGE = 3;
	private static final int KEY_AVATAR_IMAGE = 4;

	public static AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();

	private static SparseArray<DisplayImageOptions> options = new SparseArray<DisplayImageOptions>();

	private UILRequestManager() {

	}

	public static DisplayImageOptions getOptions(boolean isRound) {
		return getOptions(isRound, R.drawable.empty_photo);//mipmap.ic_launcher
	}

	public static DisplayImageOptions getOptions(boolean isRound, int defaultImageRes) {
		int key = defaultImageRes * (isRound ? KEY_COMMON_IMAGE : KEY_ROUND_IMAGE);

		DisplayImageOptions opts = options.get(key);
		if (opts != null) {
			return opts;
		} else {
			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().showImageOnLoading(defaultImageRes)
					.showImageOnFail(defaultImageRes).cacheOnDisk(true).considerExifParams(true);
			if (isRound) {
				builder.displayer(new RoundedBitmapDisplayer(20));
			}
			opts = builder.build();
			options.put(key, opts);
			return opts;
		}
	}

	public static DisplayImageOptions getAvatarOptions() {

		DisplayImageOptions opts = options.get(KEY_AVATAR_IMAGE);
		if (opts != null) {
			return opts;
		} else {
			opts = new DisplayImageOptions.Builder().showStubImage(R.mipmap.ic_launcher)
					// 默认显示的图片
					.showImageOnLoading(R.mipmap.ic_launcher)
					// 加载过程显示图片
					.showImageOnFail(R.mipmap.ic_launcher)
					// 加载失败的显示图片
					.cacheInMemory(true)
					// 开启SDCard缓存
					.showImageForEmptyUri(R.mipmap.ic_launcher)
					// 地址为空的默认显示图片
					.bitmapConfig(Config.RGB_565)
					// 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
					.cacheOnDisk(true)
					// 开启SDCard缓存
					.considerExifParams(true)
					// .displayer(new RoundedBitmapDisplayer(90)) 图片显示方式设置默认图片弧角
					// 图片显示方式设置按宽等比压缩
					// .displayer(new BitmapDisplayer() {
					//
					// @Override
					// public void display(Bitmap bitmap, ImageAware imageAware,
					// LoadedFrom loadedFrom) {
					// // TODO Auto-generated method stub
					// // TODO Auto-generated method stub
					// if (bitmap != null) {
					// bitmap = resizeImageByWidth(bitmap, 100);
					// }
					// imageAware.setImageBitmap(bitmap);
					// }
					// })
					.build();
			options.put(KEY_AVATAR_IMAGE, opts);
			return opts;
		}
	}

	/**
	 * 根据宽度等比例缩放图片
	 * 
	 * @param defaultBitmap
	 * @param targetWidth
	 * @return
	 */
	public static Bitmap resizeImageByWidth(Bitmap defaultBitmap, int targetWidth) {
		int rawWidth = defaultBitmap.getWidth();
		int rawHeight = defaultBitmap.getHeight();
		float targetHeight = targetWidth * (float) rawHeight / (float) rawWidth;
		float scaleWidth = targetWidth / (float) rawWidth;
		float scaleHeight = targetHeight / (float) rawHeight;
		Matrix localMatrix = new Matrix();
		localMatrix.postScale(scaleHeight, scaleWidth);
		return Bitmap.createBitmap(defaultBitmap, 0, 0, rawWidth, rawHeight, localMatrix, true);
	}

	/**
	 * @param uri
	 *            下载url
	 * @param img
	 *            Imageview控件
	 */
	public static void displayImage(String uri, ImageView img) {
//		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.empty_photo)
//				.showImageOnFail(R.drawable.empty_photo).cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Config.RGB_565)
//				.displayer(new SimpleBitmapDisplayer()).build();
		MyApplication.getInstance().imageLoader.displayImage(uri, img, getOptions(false), null);
	}

	/**
	 * @describe 显示带圆角
	 * @param uri
	 *            下载url
	 * @param img
	 *            Imageview控件
	 * @param corner
	 *            图片圆角 corner 0-100
	 */
	public static void displayCircleImage(String uri, ImageView img, int corner) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.ic_launcher)
				.showImageOnFail(R.mipmap.ic_launcher).cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(corner))
				.build();
		ImageLoader.getInstance().displayImage(uri, img, options, null);
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {

				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();// 本地缓存
		ImageLoader.getInstance().clearMemoryCache();// 内存缓存
	}
}
