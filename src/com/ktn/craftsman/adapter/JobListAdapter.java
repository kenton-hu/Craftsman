package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JobListAdapter extends BaseAdapter {

	private ArrayList<JobCategory> dataList;
	private Context mContext;
	private int curIndex = -1;
	
	private int[] colors = {R.color.job1,R.color.job2,R.color.job3,R.color.job4,R.color.job5,R.color.job6};
	
	public JobListAdapter(Context context, ArrayList<JobCategory> dataList) {
        mContext = context;
        this.dataList = dataList;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	public int getCurIndex() {
        return curIndex;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.jobitem, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView)convertView.findViewById(R.id.img);
            viewHolder.txt = (TextView)convertView.findViewById(R.id.txt);
            viewHolder.txt.setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        JobCategory job = dataList.get(position);
        
        Picasso.with(mContext).load(job.getImage())
        .fit().centerCrop()
        .placeholder(R.drawable.picloading)
        .error(R.drawable.picfailed)
        .into(viewHolder.img);
//        viewHolder.img.setImageResource(colors[position%6]);
//        viewHolder.txt.setText(job.getName());
        
        convertView.setOnTouchListener( new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    curIndex = position;
                }
                return false;
            }
        });
        
        return convertView;
	}

	
	public class ViewHolder{
        ImageView img;
        TextView txt;
    }
}
