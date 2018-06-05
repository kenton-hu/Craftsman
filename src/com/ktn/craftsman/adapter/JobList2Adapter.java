package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JobList2Adapter extends BaseAdapter {

	private ArrayList<Job> dataList;
	private Context mContext;
	
	
	public JobList2Adapter(Context context, ArrayList<Job> dataList) {
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
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.craftman_item, null);
            viewHolder = new ViewHolder();
            viewHolder.head = (ImageView)convertView.findViewById(R.id.head);
            viewHolder.type = (TextView)convertView.findViewById(R.id.type);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.consulation = (TextView)convertView.findViewById(R.id.consulation);
            viewHolder.liulan = (TextView)convertView.findViewById(R.id.liulan);
            viewHolder.call = (ImageView)convertView.findViewById(R.id.call);
            viewHolder.distance = (TextView)convertView.findViewById(R.id.distance);
            viewHolder.evaluate1 = (ImageView)convertView.findViewById(R.id.evaluate1);
            viewHolder.evaluate2 = (ImageView)convertView.findViewById(R.id.evaluate2);
            viewHolder.evaluate3 = (ImageView)convertView.findViewById(R.id.evaluate3);
            viewHolder.evaluate4 = (ImageView)convertView.findViewById(R.id.evaluate4);
            viewHolder.evaluate5 = (ImageView)convertView.findViewById(R.id.evaluate5);
            viewHolder.type.setVisibility(View.GONE);
            ((ImageView)convertView.findViewById(R.id.type_bg)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.ruhang)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.xundan)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.llliulan)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.ruhang)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.ruhang)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.ruhang)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.ruhang)).setVisibility(View.GONE);
            (convertView.findViewById(R.id.ruhang)).setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        Job job = dataList.get(position);
        
        Picasso.with(mContext).load(job.getImage())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(viewHolder.head);
        viewHolder.name.setText(job.getName());
        
        return convertView;
	}

	
	public class ViewHolder{
        ImageView head;
        TextView type;
        TextView name;
        TextView time;
        TextView consulation;
        TextView liulan;
        ImageView call;
        TextView distance;
        ImageView evaluate1;
        ImageView evaluate2;
        ImageView evaluate3;
        ImageView evaluate4;
        ImageView evaluate5;
    }
}
