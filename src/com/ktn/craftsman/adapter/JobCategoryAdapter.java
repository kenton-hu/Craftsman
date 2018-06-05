package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.JobList2Adapter.ViewHolder;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.squareup.picasso.Picasso;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JobCategoryAdapter extends BaseAdapter {

	private ArrayList<JobCategory> dataList;
	private Context mContext;
	
	
	public JobCategoryAdapter(Context context, ArrayList<JobCategory> dataList) {
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.one_tiem, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.one);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        JobCategory job = dataList.get(position);
        
        viewHolder.name.setText(job.getName());
        if(position == 0)
        	viewHolder.name.setBackground(mContext.getResources().getDrawable(R.drawable.bg_add));
        else
        	viewHolder.name.setBackground(mContext.getResources().getDrawable(R.drawable.input));
        
        return convertView;
	}

	public class ViewHolder{
        TextView name;
    }
}
