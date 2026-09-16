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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.name.replace.R;
import com.name.replace.adapter.JokeListAdapter;
import com.name.replace.dao.NewsInfo;
import com.name.replace.domain.FeedbackInfo;
import com.name.replace.domain.JokeInfo;
import com.name.replace.service.Constant;
import com.name.replace.service.HttpUser;
import com.name.replace.util.GsonUtils;
import com.name.replace.util.ItemClick;
import com.name.replace.util.LogUtil;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Other extends Fragment implements OnItemClickListener, ItemClick {
	private Activity context;
	private ListView listArticle;
	private View read;
	private JokeListAdapter adapter;
	private String TAG = "Other";
	private List<JokeInfo> list;
	private ClipboardManager cm;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (read == null) {
			read = inflater.inflate(R.layout.other, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
	    ViewGroup parent = (ViewGroup) read.getParent();  
	    if (parent != null)  
	    {  
	        parent.removeView(read);  
	    }
		listArticle = (ListView) read.findViewById(R.id.msg_listview);
		listArticle.setOnItemClickListener(this);
		list = new ArrayList<>();
		adapter = new JokeListAdapter(context, list);
		adapter.setItemClick(this);
		listArticle.setAdapter(adapter);
		//获取剪贴板管理器：
		cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		gainData();
		return read;
	}

	private void gainData() {
			List<NewsInfo> list1 = DataSupport.findAll(NewsInfo.class);
			if (list1 != null && list1.size() > 0) {
				for (int i = 0; i < list1.size(); i++)
				{
					NewsInfo news = list1.get(i);
					JokeInfo jokeInfo = new JokeInfo();
					jokeInfo.setId(news.getJoke_id());
					jokeInfo.setBiaoti(news.getTitle());
					jokeInfo.setZuozhe(news.getAuthor());
					jokeInfo.setNeirong(news.getSummary());
					jokeInfo.setXihuan(news.getLike_num());
					jokeInfo.setPinglun(news.getComment_num());
					jokeInfo.setLike(news.isLike());
					jokeInfo.setCollected(news.isCollected());
					list.add(jokeInfo);
				}
				adapter.notifyDataSetChanged();
			}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		Intent intent = new Intent(context, JokeDetailActivity.class);
//		String id =  list.get(position).getId();
//		intent.putExtra("id", id);
//		intent.putExtra("title", list.get(position).getBiaoti());
//		intent.putExtra("content", list.get(position).getNeirong());
//		intent.putExtra("author", list.get(position).getZuozhe());
//		intent.putExtra("date", list.get(position).getRiqi());
//		intent.putExtra("comment", list.get(position).getPinglun());
//		intent.putExtra("like", list.get(position).getXihuan());
//		startActivity(intent);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
	}


	public void refresh(){
		if (list != null){
			list.clear();
			gainData();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
					DataSupport.deleteAll(NewsInfo.class, "joke_id = ? ", list.get(position).getId());
					list.remove(position);
					adapter.notifyDataSetChanged();
					Toast.makeText(context, "取消收藏成功!", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	/*
	 *  点赞
	 */
	private void like(final int position){
		String url = Constant.URL_JOKE_LIKE + list.get(position).getId();
		LogUtil.e(TAG, "url=" + url);
		HttpUser.get(url, new AsyncHttpResponseHandler(){

			public void onFailure(int arg0, Header[] arg1,
								  byte[] arg2, Throwable arg3) {
				LogUtil.e(TAG, "err=" + arg3.getMessage());
				Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
			}


			public void onSuccess(int arg0, Header[] arg1,
								  byte[] arg2) {
				String str = new String(arg2);
				LogUtil.e(TAG, "result=" + str);
				FeedbackInfo info = null;
				try {
					info = GsonUtils.parseJSON(str,  FeedbackInfo.class);
				}catch (JsonSyntaxException e){
					LogUtil.e(TAG, "err=" + e.getMessage());
					e.printStackTrace();
				}
				if (info != null) {
					if (TextUtils.equals(info.getFankui(), "ok"))
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
			}

		});
	}

	/*
 	 *  取消点赞
	 */
	private void unlike(final int position){
		String url = Constant.URL_JOKE_UNLIKE + list.get(position).getId();
		LogUtil.e(TAG, "url=" + url);
		HttpUser.get(url, new AsyncHttpResponseHandler(){

			public void onFailure(int arg0, Header[] arg1,
								  byte[] arg2, Throwable arg3) {
				LogUtil.e(TAG, "err=" + arg3.getMessage());
				Toast.makeText(context, "网络异常!", Toast.LENGTH_SHORT).show();
			}


			public void onSuccess(int arg0, Header[] arg1,
								  byte[] arg2) {
				String str = new String(arg2);
				LogUtil.e(TAG, "result=" + str);
				FeedbackInfo info = null;
				try {
					info = GsonUtils.parseJSON(str,  FeedbackInfo.class);
				}catch (JsonSyntaxException e){
					LogUtil.e(TAG, "err=" + e.getMessage());
					e.printStackTrace();
				}
				if (info != null) {
					if (TextUtils.equals(info.getFankui(), "ok"))
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

		});
	}

}
