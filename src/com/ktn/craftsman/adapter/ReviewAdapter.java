package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.App;
import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.CraftManAdapter.ViewHolder;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.Review;
import com.ktn.craftsman.util.RoundImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewAdapter extends BaseAdapter {

	private ArrayList<Review> dataList;
	private Context mContext;
	
	public ReviewAdapter(Context context, ArrayList<Review> dataList) {
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
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.reviews_item, null);
            viewHolder = new ViewHolder();
            viewHolder.head = (RoundImageView)convertView.findViewById(R.id.head);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.evaluate1 = (ImageView)convertView.findViewById(R.id.evaluate1);
            viewHolder.evaluate2 = (ImageView)convertView.findViewById(R.id.evaluate2);
            viewHolder.evaluate3 = (ImageView)convertView.findViewById(R.id.evaluate3);
            viewHolder.evaluate4 = (ImageView)convertView.findViewById(R.id.evaluate4);
            viewHolder.evaluate5 = (ImageView)convertView.findViewById(R.id.evaluate5);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        Review review = dataList.get(position);
        viewHolder.name.setText(review.getMemberName());
        viewHolder.time.setText(App.Date2String(review.getCreateDate()));
        viewHolder.content.setText(review.getContent());
        Picasso.with(mContext).load(review.getMemberAvatar())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(viewHolder.head);
        float evaluate = review.getRateGeneral();
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
        
        return convertView;
	}

	public class ViewHolder{
	    RoundImageView head;
        TextView name;
        TextView time;
        TextView content;
        ImageView evaluate1;
        ImageView evaluate2;
        ImageView evaluate3;
        ImageView evaluate4;
        ImageView evaluate5;
    }
}
