package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.ktn.craftsman.bean.PayMethod;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PayMethodAdapter extends BaseAdapter {

	private ArrayList<PayMethod> dataList;
	private Context mContext;
	
	
	public PayMethodAdapter(Context context, ArrayList<PayMethod> dataList) {
        mContext = context;
        this.dataList = dataList;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.item_paymethod, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
            viewHolder.check = (ImageView)convertView.findViewById(R.id.check);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        PayMethod method = dataList.get(position);
        
        viewHolder.title.setText(method.getTitle());
        viewHolder.content.setText(method.getContent());
        
        if(method.isChecked())
        	viewHolder.check.setBackground(mContext.getResources().getDrawable(R.drawable.check_sel));
        else
        	viewHolder.check.setBackground(mContext.getResources().getDrawable(R.drawable.check));
        
        switch (position) {
		case 0:
			viewHolder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.yue));
			break;
		case 1:
			viewHolder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.zhifubao));
			break;
		case 2:
			viewHolder.icon.setBackground(mContext.getResources().getDrawable(R.drawable.weixin));
			break;
		default:
			break;
		}
        
        return convertView;
	}

	
	public class ViewHolder{
        ImageView icon;
        TextView title;
        TextView content;
        ImageView check;
    }
}
