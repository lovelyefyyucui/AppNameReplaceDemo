package com.name.replace.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.name.replace.R;
import com.name.replace.WebviewActivity;
import com.name.replace.adapter.MyReadCircleAdapter;
import com.name.replace.domain.DayNews;
import com.name.replace.presenter.GetNewsPresenter;
import com.name.replace.presenter.impl.GetNewsPresenterImpl;
import com.name.replace.service.Constant;
import com.name.replace.ui.recyclerview.SwipyRefreshLayoutDirection;
import com.name.replace.ui.recyclerview.XRecyclerView;
import com.name.replace.ui.recyclerview.util.MultiItemTypeAdapter;
import com.name.replace.util.LogUtil;
import com.name.replace.util.PreferencesUtils;
import com.name.replace.view.BaseDataView;

import java.util.ArrayList;
import java.util.List;

public class Read extends Fragment implements MultiItemTypeAdapter.OnItemClickListener, BaseDataView {
	private Activity context;
	private List<DayNews> list;
	private List<DayNews> listPage;
	private XRecyclerView mXRecyclerView;
	private LinearLayout msgLayout;
	private LinearLayout linear;
	private View read;
	private String TAG = "Read";
	private int mCurrentPage = 1;
	private int mTotalPage = 0;
	private MyReadCircleAdapter adapter;
	private GetNewsPresenter mGetNewsPresenter;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (read == null) {
			read = inflater.inflate(R.layout.read, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
	    ViewGroup parent = (ViewGroup) read.getParent();  
	    if (parent != null)  
	    {  
	        parent.removeView(read);  
	    }
		msgLayout = (LinearLayout) read.findViewById(R.id.msg_layout);
		linear = (LinearLayout) read.findViewById(R.id.msg_load);
		mXRecyclerView = (XRecyclerView) read.findViewById(R.id.xrecycleview);
		// 设置 刷新的方向
		mXRecyclerView.setDirection(SwipyRefreshLayoutDirection.BOTH);
		// 是否自动加载
		mXRecyclerView.setAutoLoadEnable(false);

		list = new ArrayList<>();
		listPage = new ArrayList<>();
		adapter = new MyReadCircleAdapter(context, null);
		adapter.setOnItemClickListener(this);
		mXRecyclerView.setAdapter(adapter);
		mXRecyclerView.setSimpleXRecyclerViewListener(new XRecyclerView.OnNeedPostRequestListener() {
			@Override
			public void postRequest() {
				mXRecyclerView.postDelayed(new Runnable() {
					@Override
					public void run() {
						gainData();
					}
				},100);
			}
		});
		mGetNewsPresenter = new GetNewsPresenterImpl("getNews", this);

		gainData();
		return read;
	}

	private boolean isRequesting;
	private void gainData() {
		if (mXRecyclerView.CURRENT_STATE == mXRecyclerView.PULL_TO_REFRESH) {
			if (!isRequesting) {
				isRequesting = true;
				mCurrentPage = 1;
				list.clear();
				listPage.clear();
				mGetNewsPresenter.getNews();
			}
		} else if (mXRecyclerView.CURRENT_STATE == mXRecyclerView.LOAD_MORE) {
			mXRecyclerView.stopRefreshAndLoadMore();
			if (mCurrentPage < mTotalPage) {
				if (list != null && list.size() > 0) {
					for (int i = mCurrentPage * 10; i < (mCurrentPage + 1) * 10; i++) {
						listPage.add(list.get(i));
					}
					adapter.addAllItem(listPage);
				}
				mCurrentPage++;
			}
		}
	}



	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}


	public void refresh(){
		if (list != null && list.size() == 0){
			list.clear();
			listPage.clear();
			gainData();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
		Intent intent = new Intent(context, WebviewActivity.class);
		intent.putExtra("link", list.get(position).getUrl());
		intent.putExtra("isHide", false);
		intent.putExtra("title", list.get(position).getTitle());//"hide"
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
		return false;
	}

	@Override
	public void showLoading() {
		msgLayout.setVisibility(View.GONE);
		linear.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideLoading() {
		msgLayout.setVisibility(View.VISIBLE);
		linear.setVisibility(View.GONE);
	}



	@Override
	public void onFailure(String tag, int code, String message) {
		LogUtil.e("tag=" + tag + ",err=" + message + ",code=" + code);
		isRequesting = false;
		mXRecyclerView.stopRefreshAndLoadMore();
		Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setDataInfo(String tag, int code, Object obj, String message) {
		LogUtil.e("tag=" + tag + ",err=" + message + ",code=" + code);
		isRequesting = false;
		ArrayList<DayNews> info = (ArrayList<DayNews>) obj;
		if (info != null && info.size() > 0) {
			list = info;
			for (int i = 0; i < 10; i++){
				listPage.add(list.get(i));
			}
			mTotalPage = list.size() / 10;
			LogUtil.e("total page=" + mTotalPage);
			mXRecyclerView.stopRefreshAndLoadMore();
			adapter.updata(listPage);
		}else {
			Toast.makeText(context, "暂无数据!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mGetNewsPresenter.detachView();
	}
}
