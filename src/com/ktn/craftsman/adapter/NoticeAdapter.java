package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.ReviewAdapter.ViewHolder;
import com.ktn.craftsman.bean.Notice;
import com.ktn.craftsman.bean.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeAdapter extends BaseAdapter {

	private ArrayList<Notice> dataList;
	private Context mContext;
	
	public NoticeAdapter(Context context, ArrayList<Notice> dataList) {
        mContext = context;
        this.dataList = dataList;
    }
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.noticeitem, null);
            viewHolder = new ViewHolder();
            viewHolder.order = (TextView)convertView.findViewById(R.id.order);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        Notice notice = dataList.get(position);
        viewHolder.name.setText(notice.getName());
        viewHolder.order.setText(notice.getOrder()+"");
		return convertView;
	}
	
	public class ViewHolder{
        TextView name;
        TextView order;
    }

}
