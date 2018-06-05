package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.App;
import com.ktn.craftsman.CraftsManListActivity;
import com.ktn.craftsman.LoginActivity;
import com.ktn.craftsman.R;
import com.ktn.craftsman.R.id;
import com.ktn.craftsman.adapter.JobListAdapter.ViewHolder;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.CraftManResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.util.RoundImageView;
import com.ktn.craftsman.util.VDialog;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class CraftManAdapter extends BaseAdapter {

	private ArrayList<CraftMan> dataList;
	private Context mContext;
	private String type;
	private String phone;
	
	public CraftManAdapter(Context context, ArrayList<CraftMan> dataList, String type) {
        mContext = context;
        this.dataList = dataList;
        this.type = type;
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
            viewHolder.head = (RoundImageView)convertView.findViewById(R.id.head);
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
        
        CraftMan man = dataList.get(position);
        viewHolder.type.setText(type);
        viewHolder.name.setText(man.getName());
        viewHolder.time.setText(App.Date2String(man.getCreateDate()));
        viewHolder.consulation.setText(man.getConsulationCount()+"次");
        viewHolder.liulan.setText("已有"+man.getViews()+"人浏览");
        Picasso.with(mContext).load(man.getAvatar())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(viewHolder.head);
        double distance = App.getDistance(App.latitude, App.longitude,
        		Double.parseDouble(man.getLat()), Double.parseDouble(man.getLng()));
        if(distance < 1000)
        	viewHolder.distance.setText(((int)distance)+"米");
        else
        	viewHolder.distance.setText(((int)distance/1000)+"公里");
        float evaluate = man.getRateGeneral();
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
        
        
        phone = man.getPhone();
        final int id = man.getId();
        
        viewHolder.call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    if(!App.isLogin){
		            Intent it = new Intent();
		            it.setClass(mContext, LoginActivity.class);
		            mContext.startActivity(it);
		            return;
		        }
			    sendHistorySave(App.API_historysave,id);
				 Intent it = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
	             mContext.startActivity(it); 
			}
		});
        
        return convertView;
	}

	public void sendHistorySave(String api, int id){
		RequestParams params = new RequestParams((HttpCycleContext) mContext);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("id", id);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
		    @Override
		    protected void onSuccess(HttpResult t) {
		        // TODO Auto-generated method stub
		        super.onSuccess(t);
		        if(t.getType().equals(App.SUCCESS)){
		            Intent it = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
	                mContext.startActivity(it); 
                }else if(!TextUtils.isEmpty(t.getContent())){
                    VDialog.getDialogInstance().toast(mContext, t.getContent());
                }
		        
		    }
		});
	}
	
	public class ViewHolder{
	    RoundImageView head;
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
