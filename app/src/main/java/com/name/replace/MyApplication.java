package com.name.replace;

import com.mob.MobSDK;
import com.name.replace.domain.LoginEntity;
import com.name.replace.service.Constant;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.content.Context;
import android.support.multidex.MultiDex;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends LitePalApplication {
	protected static volatile MyApplication instance;
	public ImageLoader imageLoader;
	private LoginEntity mLoginEntity;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 通过代码注册AppKey和AppSecret
		MobSDK.init(this, Constant.AppKey, Constant.AppSecret);
		initImageLoader(this);
		//初始化极光推送sdk
		JPushInterface.setDebugMode(BuildConfig.DEBUG);
		JPushInterface.init(this);
	}

	@Override
	protected void attachBaseContext(Context context) {
		super.attachBaseContext(context);
		MultiDex.install(this);
	}

	private void initImageLoader(Context context) {
		// 获取本地缓存的目录，该目录在SDCard的根目录下
		// File cacheDir = StorageUtils.getOwnCacheDirectory(context,
		// "Mall/UIL/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
				.threadPoolSize(3) // default
				.threadPriority(Thread.NORM_PRIORITY - 1) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13) // default
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context)) // default
				.imageDecoder(new BaseImageDecoder(BuildConfig.DEBUG)) // default
				.defaultDisplayImageOptions(Constant.options) // default
				.writeDebugLogs()
				.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	public static MyApplication getInstance() {
		return instance;
	}


	public void setLoginEntity(LoginEntity mLoginEntity) {
		this.mLoginEntity = mLoginEntity;
	}

	public LoginEntity getLoginEntity() {
		return mLoginEntity;
	}
}
