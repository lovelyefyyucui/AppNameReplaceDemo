package com.name.replace.adapter;


import android.content.Context;
import android.text.TextUtils;


import com.name.replace.R;
import com.name.replace.domain.DayNews;
import com.name.replace.ui.recyclerview.base.ViewHolder;
import com.name.replace.ui.recyclerview.util.CommonAdapter;
import com.name.replace.util.DateUtil;

import java.util.List;

/**
 * Created by anzhuo002 on 2016/8/23.
 */

public class MyReadCircleAdapter extends CommonAdapter<DayNews> {

    public MyReadCircleAdapter(Context context, List<DayNews> datas) {
        super(context,  R.layout.msg_listview_item, datas);
    }

    @Override
    public void convert(ViewHolder viewHolder, DayNews item, int position) {
        viewHolder.setText(R.id.msg_title, item.getTitle());
        viewHolder.setText(R.id.msg_author, item.getSummary());
        String time = "2015-04-09 08:10:56";
        if (!TextUtils.isEmpty(item.getPublishdate())) {
            time = item.getPublishdate();
            if (time.startsWith("149") || time.startsWith("15"))
            {
                time = time + "000";
                time = DateUtil.StampToDate(time);
            }
        }
        viewHolder.setText(R.id.msg_time, time);
        viewHolder.setText(R.id.msg_title,item.getTitle());
        viewHolder.setImageByUrl(R.id.msg_pic, item.getLogofile());
    }
}






