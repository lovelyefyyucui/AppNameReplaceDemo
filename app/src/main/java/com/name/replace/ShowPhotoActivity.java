package com.name.replace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.name.replace.ui.HackyViewPager;
import com.name.replace.ui.photoview.EasePhotoView;
import com.name.replace.ui.photoview.PhotoViewAttacher;
import com.name.replace.util.LogUtil;
import com.name.replace.util.UILRequestManager;

import java.util.ArrayList;
import java.util.List;


public class ShowPhotoActivity extends Base2Activity {
    private MenuItem menuLockItem;
    private ArrayList<String> imgData;
    private ArrayList<String> thumbpicture;
    private List<View> imgList;
    private HackyViewPager mViewPager;
    private int mIndex;
    private Activity activity;
    private static final String ISLOCKED_ARG = "isLocked";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        TAG = "ShowPhotoActivity";
        activity = ShowPhotoActivity.this;
        getIntentData();

        imgList = new ArrayList<View>();
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        for (int i = 0; i < imgData.size(); i++) {
            EasePhotoView photoView = new EasePhotoView(this);
            final String url = imgData.get(i).trim();
            UILRequestManager.displayImage(url, photoView);
            imgList.add(photoView);
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float v, float v1) {
                    LogUtil.e("ShowPhotoActivity", " photoView.OnViewTapListener=====");
                    ShowPhotoActivity.this.finish();
                    ShowPhotoActivity.this.overridePendingTransition(0, R.anim.pic_activity_hide);
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LogUtil.e("ShowPhotoActivity"," photoView.OnLongClickListener=====");
                    ShowPhotoActivity.this.finish();
                    ShowPhotoActivity.this.overridePendingTransition(0,R.anim.pic_activity_hide);
                    return true;
                }
            });
        }
        mViewPager.setAdapter(new SamplePagerAdapter(imgList));
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
        mViewPager.setCurrentItem(mIndex);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mIndex = intent.getIntExtra("mIndex", 0);
        imgData = intent.getStringArrayListExtra("imgData");
        thumbpicture = intent.getStringArrayListExtra("thumb");
        LogUtil.e(TAG, "imgData=" + imgData);
        LogUtil.e(TAG, "thumb=" + thumbpicture);
    }

    static class SamplePagerAdapter extends PagerAdapter {
        private List<View> imgList;

        public SamplePagerAdapter(List<View> imgList) {
            this.imgList = imgList;
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            EasePhotoView photoView = new EasePhotoView(container.getContext());
            photoView = (EasePhotoView) imgList.get(position);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewpager_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuLockItem = menu.findItem(R.id.menu_lock);
        toggleLockBtnTitle();
        menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleViewPagerScrolling();
                toggleLockBtnTitle();
                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

    private void toggleLockBtnTitle() {
        boolean isLocked = false;
        if (isViewPagerActive()) {
            isLocked = ((HackyViewPager) mViewPager).isLocked();
        }
        String title = "测测";
        if (menuLockItem != null) {
            menuLockItem.setTitle(title);
        }
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

}
