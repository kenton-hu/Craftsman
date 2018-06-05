package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.App;
import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.CraftmanDetailAdapter.ViewHolder;
import com.ktn.craftsman.bean.Case;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CraftManAuthAdapter extends BaseAdapter {

	private ArrayList<String> dataList;
	private Context mContext;
	
	public CraftManAuthAdapter(Context context, ArrayList<String> dataList) {
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
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.craftauth_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView)convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
    	App.getInstance().load(viewHolder.img, R.drawable.add, dataList.get(position));
        
		return convertView;
	}
	
	public class ViewHolder{
        ImageView img;
    }

}
