package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.CraftManAdapter.ViewHolder;
import com.ktn.craftsman.bean.Case;
import com.ktn.craftsman.bean.CraftMan;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CraftmanDetailAdapter extends BaseAdapter {
	private ArrayList<Case> dataList;
	private Context mContext;
	
	public CraftmanDetailAdapter(Context context, ArrayList<Case> dataList) {
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
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.craftdetail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView)convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        Case mycase = dataList.get(position);
        Picasso.with(mContext).load(mycase.getImage())
        .fit().centerCrop()
        .placeholder(R.drawable.img_loading)
        .error(R.drawable.img_load_fail)
        .into(viewHolder.img);
        
		return convertView;
	}
	
	public class ViewHolder{
        ImageView img;
    }

}
