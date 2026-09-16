package com.name.replace.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.name.replace.R;
import com.name.replace.domain.JokeInfo;
import com.name.replace.util.ItemClick;

import java.util.List;

public class JokeListAdapter extends BaseAdapter {
	private Context context;
	private List<JokeInfo> list;
	private ItemClick itemClick;

	public JokeListAdapter(Context context, List<JokeInfo> list)
	{
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class Holder {
		private TextView mBiaoti;
		private TextView mZuozhe;
		private TextView mNeirong;
		private ImageView mCollect_iv;
		private ImageView mShare_iv;
		private ImageView mCopy_iv;
		private ImageView mLike_iv;
		private TextView mXihuan;
		private ImageView mComment_iv;
		private TextView mPinglun;
		private TextView mRiqi;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context,
					R.layout.listview_item, null);
			holder.mBiaoti = (TextView) convertView.findViewById(R.id.biaoti);
			holder.mZuozhe = (TextView) convertView.findViewById(R.id.zuozhe);
			holder.mNeirong = (TextView) convertView.findViewById(R.id.neirong);
			holder.mCollect_iv = (ImageView) convertView.findViewById(R.id.collect_iv);
			holder.mShare_iv = (ImageView) convertView.findViewById(R.id.share_iv);
			holder.mCopy_iv = (ImageView) convertView.findViewById(R.id.copy_iv);
			holder.mLike_iv = (ImageView) convertView.findViewById(R.id.like_iv);
			holder.mXihuan = (TextView) convertView.findViewById(R.id.xihuan);
			holder.mComment_iv = (ImageView) convertView.findViewById(R.id.comment_iv);
			holder.mPinglun = (TextView) convertView.findViewById(R.id.pinglun);
			holder.mRiqi = (TextView) convertView.findViewById(R.id.riqi);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		JokeInfo info = list.get(position);
		holder.mBiaoti.setText(info.getBiaoti());
		holder.mZuozhe.setText(info.getZuozhe());
		holder.mNeirong.setText(Html.fromHtml(info.getNeirong()));
		holder.mXihuan.setText(info.getXihuan());
		holder.mPinglun.setText(info.getPinglun());
		holder.mRiqi.setText(info.getRiqi());
		if (info.isLike())
		{
			holder.mLike_iv.setImageResource(R.drawable.ico_like_blue);
		} else {
			holder.mLike_iv.setImageResource(R.drawable.ico_like_gray);
		}
		if (info.isCollected())
		{
			holder.mCollect_iv.setImageResource(R.drawable.ico_favourite_orange);
		} else {
			holder.mCollect_iv.setImageResource(R.drawable.ico_favourite_gray);
		}
		final int finalPosition = position;
		if (itemClick != null)
		{
			holder.mCollect_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClick.onItemClick(v, finalPosition);
				}
			});
			holder.mShare_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClick.onItemClick(v, finalPosition);
				}
			});
			holder.mCopy_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClick.onItemClick(v, finalPosition);
				}
			});
			holder.mLike_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClick.onItemClick(v, finalPosition);
				}
			});
			holder.mComment_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClick.onItemClick(v, finalPosition);
				}
			});
		}
		return convertView;
	}

	public void setItemClick(ItemClick itemClick) {
		this.itemClick = itemClick;
	}
}
