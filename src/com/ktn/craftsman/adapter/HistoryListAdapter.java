package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.bean.Job;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HistoryListAdapter extends BaseAdapter {

	private ArrayList<Job> dataList;
	private Context mContext;
	
	public HistoryListAdapter(Context context, ArrayList<Job> dataList) {
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
		return null;
	}

}
