package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.App;
import com.ktn.craftsman.R;
import com.ktn.craftsman.bean.HistoryMember;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryMemberAdapter extends BaseAdapter {

	private ArrayList<HistoryMember> dataList;
	private Context mContext;
	
	public HistoryMemberAdapter(Context context, ArrayList<HistoryMember> dataList) {
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
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        HistoryMember man = dataList.get(position);
        String jobName = man.getMemberJobName();
        if(!TextUtils.isEmpty(jobName)){
        	viewHolder.type.setText(jobName.substring(0, 1));
        }
        viewHolder.name.setText(man.getMemberName());
        viewHolder.time.setText(App.Date2String(man.getMemberCreateDate()));
        viewHolder.consulation.setText(man.getMemberConsulationCount()+"次");
        viewHolder.liulan.setText("已有"+man.getMemberViews()+"人浏览");
//        if(!TextUtils.isEmpty(man.getMemberAvatar())){
//            Picasso.with(mContext).load(man.getMemberAvatar())
//            .fit().centerCrop()
//            .memoryPolicy(MemoryPolicy.NO_CACHE)  
//            .networkPolicy(NetworkPolicy.NO_CACHE)  
//            .placeholder(R.drawable.defalut)
//            .error(R.drawable.defalut)
//            .into(viewHolder.head);
//        }else{
//            viewHolder.head.setImageDrawable(mContext.getResources().getDrawable(R.drawable.defalut));
//        }
        ImageLoader.getInstance().displayImage(man.getMemberAvatar(), viewHolder.head);
        double distance = App.getDistance(App.latitude, App.longitude,
        		Double.parseDouble(man.getMemberLat()), Double.parseDouble(man.getMemberLng()));
        if(distance < 1000)
        	viewHolder.distance.setText(((int)distance)+"米");
        else
        	viewHolder.distance.setText(((int)distance/1000)+"公里");
        float evaluate = man.getMemberRateGeneral();
        if(evaluate == 0.0){
        	viewHolder.evaluate1.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate2.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate3.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
        }else if (evaluate > 0.0 & evaluate <= 0.5) {
        	viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red_half);
        	viewHolder.evaluate2.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate3.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 0.5 & evaluate <= 1.0) {
        	viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red);
        	viewHolder.evaluate2.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate3.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 1.0 & evaluate <= 1.5) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red_half);
        	viewHolder.evaluate3.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 1.5 & evaluate <= 2.0) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red); 
        	viewHolder.evaluate3.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 2.0 & evaluate <= 2.5) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red);  
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate3.setImageResource(R.drawable.evaluate_red_half);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 2.5 & evaluate <= 3.0) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate3.setImageResource(R.drawable.evaluate_red);
        	viewHolder.evaluate4.setImageResource(R.drawable.evaluate_gre);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 3.0 & evaluate <= 3.5) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate3.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate4.setImageResource(R.drawable.evaluate_red_half);
        	viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 3.5 & evaluate <= 4.0) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate3.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate4.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate5.setImageResource(R.drawable.evaluate_gre);
		}else if (evaluate > 4.0 & evaluate <= 4.5) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate3.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate4.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate5.setImageResource(R.drawable.evaluate_red_half);
		}else if (evaluate > 4.5 & evaluate <= 5.0) {
			viewHolder.evaluate1.setImageResource(R.drawable.evaluate_red); 
            viewHolder.evaluate2.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate3.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate4.setImageResource(R.drawable.evaluate_red);
            viewHolder.evaluate5.setImageResource(R.drawable.evaluate_red);
		}
        
        
        final String phone = man.getMemberPhone();
        
        viewHolder.call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent it = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
	             mContext.startActivity(it); 
			}
		});
        
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
