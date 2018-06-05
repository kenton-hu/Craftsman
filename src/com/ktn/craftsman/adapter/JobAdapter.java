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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JobAdapter extends BaseAdapter {

	private ArrayList<Job> dataList;
	private Context mContext;
	private int curIndex = -1;
	
	public JobAdapter(Context context, ArrayList<Job> dataList) {
        mContext = context;
        this.dataList = dataList;
    }
	
	public int getCurIndex() {
        return curIndex;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.jobitem2, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView)convertView.findViewById(R.id.img);
            viewHolder.txt = (TextView)convertView.findViewById(R.id.txt);
            viewHolder.txt.setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        Job job = dataList.get(position);
        
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
