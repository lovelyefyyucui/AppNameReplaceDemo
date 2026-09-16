package com.name.replace.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.name.replace.R;
import com.name.replace.adapter.JokeListAdapter;
import com.name.replace.dao.NewsInfo;
import com.name.replace.domain.FeedbackInfo;
import com.name.replace.domain.JokeInfo;
import com.name.replace.presenter.GetJokePresenter;
import com.name.replace.presenter.impl.GetJokePresenterImpl;
import com.name.replace.ui.PullToRefreshView;
import com.name.replace.util.ItemClick;
import com.name.replace.util.LogUtil;
import com.name.replace.view.BaseDataView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class JokeListFragment extends Fragment  implements BaseDataView, AdapterView.OnItemClickListener, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, ItemClick {
	private Activity context;
	private ListView jokeInfoLv;
	private View areaLottery;
	private JokeListAdapter adapter;
	private String TAG = "JokeListFragment";
	private List<JokeInfo> list;
	private LinearLayout msgLayout;
	private LinearLayout linear;
	private PullToRefreshView mRefreshView;
	private ClipboardManager cm;
	private List<NewsInfo> list1;
	private GetJokePresenter mGetJokePresenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (areaLottery == null) {
			areaLottery = inflater.inflate(R.layout.area_lottery, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
	    ViewGroup parent = (ViewGroup) areaLottery.getParent();
	    if (parent != null)  
	    {  
	        parent.removeView(areaLottery);
	    }
		msgLayout = (LinearLayout) areaLottery.findViewById(R.id.msg_layout);
		mRefreshView = (PullToRefreshView) areaLottery.findViewById(R.id.pullToRefreshView1);
		mRefreshView.setOnHeaderRefreshListener(this);
		mRefreshView.setOnFooterRefreshListener(this);
		linear = (LinearLayout) areaLottery.findViewById(R.id.msg_load);
		jokeInfoLv = (ListView) areaLottery.findViewById(R.id.lottery_gv);
		jokeInfoLv.setOnItemClickListener(this);
		list = new ArrayList<>();
		adapter = new JokeListAdapter(context, list);
		adapter.setItemClick(this);
		jokeInfoLv.setAdapter(adapter);
		//获取剪贴板管理器：
		cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

		mGetJokePresenter = new GetJokePresenterImpl("getJoke", this);
		initData();
		return areaLottery;
	}

	public void refresh(){
		if (list != null && list.size() == 0){
			list.clear();
			initData();
		}
	}

	private boolean isRequesting;
	private void initData(){
		list1 = DataSupport.findAll(NewsInfo.class);
		if (!isRequesting)
		{
			isRequesting = true;
			mGetJokePresenter.setTag("getJoke");
			mGetJokePresenter.getJoke();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Intent intent = new Intent(context, JokeDetailActivity.class);
//		String id1 =  list.get(position).getId();
//		intent.putExtra("id", id1);
//		intent.putExtra("title", list.get(position).getBiaoti());
//		intent.putExtra("content", list.get(position).getNeirong());
//		intent.putExtra("author", list.get(position).getZuozhe());
//		intent.putExtra("date", list.get(position).getRiqi());
//		intent.putExtra("comment", list.get(position).getPinglun());
//		intent.putExtra("like", list.get(position).getXihuan());
//		startActivity(intent);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mRefreshView.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		msgLayout.setVisibility(View.GONE);
		linear.setVisibility(View.VISIBLE);
		list.clear();
		initData();
		mRefreshView.onHeaderRefreshComplete();
	}

	@Override
	public void onItemClick(View v, int position) {
		switch (v.getId())
		{
			case R.id.comment_iv:
//				Intent intent = new Intent(context, JokeDetailActivity.class);
//				String id =  list.get(position).getId();
//				intent.putExtra("id", id);
//				intent.putExtra("title", list.get(position).getBiaoti());
//				intent.putExtra("content", list.get(position).getNeirong());
//				intent.putExtra("author", list.get(position).getZuozhe());
//				intent.putExtra("date", list.get(position).getRiqi());
//				intent.putExtra("comment", list.get(position).getPinglun());
//				intent.putExtra("like", list.get(position).getXihuan());
//				startActivity(intent);
				break;

			case R.id.like_iv:
				if (!list.get(position).isLike())
				{
					like(position);
				} else {
					unlike(position);
				}
			break;

			case R.id.share_iv:
				String title, content;
				title = list.get(position).getBiaoti();
				content = Html.fromHtml(list.get(position).getNeirong()).toString();
				Intent share_intent = new Intent();
				share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
				share_intent.setType("text/plain");//设置分享内容的类型
				share_intent.putExtra(Intent.EXTRA_SUBJECT, title);//添n加分享内容标题
				share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
				//创建分享的Dialog
				share_intent = Intent.createChooser(share_intent, "分享到...");
				startActivity(share_intent);
				break;

			case R.id.copy_iv:
				// 创建普通字符型ClipData
				ClipData mClipData = ClipData.newPlainText("Label", list.get(position).getNeirong());
				// 将ClipData内容放到系统剪贴板里。
				cm.setPrimaryClip(mClipData);
				Toast.makeText(context, "内容已复制到剪贴板", Toast.LENGTH_SHORT).show();
				break;
			case R.id.collect_iv:
				if (!list.get(position).isCollected())
				{
					list.get(position).setCollected(true);
					JokeInfo info = list.get(position);
					NewsInfo newsInfo = new  NewsInfo();
					newsInfo.setJoke_id(info.getId());
					newsInfo.setAuthor(info.getZuozhe());
					newsInfo.setComment_num(info.getPinglun());
					newsInfo.setLike_num(info.getXihuan());
					newsInfo.setTitle(info.getBiaoti());
					newsInfo.setSummary(info.getNeirong());
					newsInfo.setLogofile("");
					newsInfo.setPublishdate(info.getRiqi());
					newsInfo.setUrl("");
					newsInfo.setLike(info.isLike());
					newsInfo.setCollected(info.isCollected());
					if (newsInfo.save())
					{
						adapter.notifyDataSetChanged();
						Toast.makeText(context, "添加收藏成功!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "添加收藏失败!", Toast.LENGTH_SHORT).show();
					}
				} else {
					list.get(position).setCollected(false);
					DataSupport.deleteAll(NewsInfo.class, "joke_id = ? ", list.get(position).getId());
					adapter.notifyDataSetChanged();
					Toast.makeText(context, "取消收藏成功!", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private int position;
	/*
	 *  点赞
	 */
	private void like(final int position){
		   this.position = position;
		   mGetJokePresenter.setTag("like");
		   mGetJokePresenter.like(list.get(position).getId());
	}

	/*
 	 *  取消点赞
	 */
	private void unlike(final int position){
		this.position = position;
		mGetJokePresenter.setTag("unlike");
		mGetJokePresenter.unlike(list.get(position).getId());
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
		if (TextUtils.equals(tag, "getJoke")) {
			isRequesting = false;
		}
		Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setDataInfo(String tag, int code, Object obj, String message) {
		LogUtil.e("tag=" + tag + ",err=" + message + ",code=" + code);
		if (TextUtils.equals(tag, "getJoke"))
		{
			isRequesting = false;
			ArrayList<JokeInfo> info = (ArrayList<JokeInfo>) obj;
			if (info != null) {
				if (info.size() > 0)
				{
					for (int j = 0; j < info.size(); j++)
					{
						info.get(j).setLike(false);
						info.get(j).setCollected(false);
						if (list1 != null && list1.size() > 0)
						{
							for (int i = 0; i < list1.size(); i++)
							{
								if (TextUtils.equals(info.get(j).getId(), list1.get(i).getJoke_id()))
								{
									info.get(j).setLike(list1.get(i).isLike());
									info.get(j).setCollected(list1.get(i).isCollected());
								}
							}
						}
					}
					list.addAll(info);
					adapter.notifyDataSetChanged();
				}else {
					Toast.makeText(context, "暂无数据!", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(context, "数据异常!", Toast.LENGTH_SHORT).show();
			}
		} else if (TextUtils.equals(tag, "like")){
			FeedbackInfo info1 = (FeedbackInfo) obj;
			if (info1 != null) {
				if (TextUtils.equals(info1.getFankui(), "ok"))
				{
					list.get(position).setLike(true);
					int likes = Integer.valueOf(list.get(position).getXihuan());
					list.get(position).setXihuan(likes + 1 + "");
					adapter.notifyDataSetChanged();
					Toast.makeText(context, "已添加喜欢!", Toast.LENGTH_SHORT).show();
					NewsInfo newsInfo = new  NewsInfo();
					newsInfo.setLike(true);
					newsInfo.updateAll("joke_id = ? ", list.get(position).getId());
				}else {
					Toast.makeText(context, "添加喜欢失败!", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(context, "数据异常!", Toast.LENGTH_SHORT).show();
			}
		}else {
			FeedbackInfo info2 = (FeedbackInfo) obj;
			if (info2 != null) {
				if (TextUtils.equals(info2.getFankui(), "ok"))
				{
					list.get(position).setLike(false);
					int likes = Integer.valueOf(list.get(position).getXihuan());
					list.get(position).setXihuan(likes - 1 + "");
					adapter.notifyDataSetChanged();
					Toast.makeText(context, "已取消喜欢!", Toast.LENGTH_SHORT).show();
					NewsInfo newsInfo = new  NewsInfo();
					newsInfo.setLike(false);
					newsInfo.updateAll("joke_id = ? ", list.get(position).getId());
				}else {
					Toast.makeText(context, "添加喜欢失败!", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(context, "数据异常!", Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mGetJokePresenter.detachView();
	}

}
