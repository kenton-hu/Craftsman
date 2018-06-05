package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.CraftManAdapter;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.CraftManResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.fragment.BaseFragment;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class CraftsManListActivity extends AbstractActivity implements OnClickListener{

	private View back;
	private View cancel;
	private int jobID;
	private String jobName;
	private View evaluatelyt;
	private View distancelyt;
	private TextView title;
	private TextView distancetext;
	private TextView evaluatetext;
	private ImageView evaluateimg;
	private ImageView distancetimg;
	private View distanceview;
	private View evaluateview;
	private ListView craftManList;
	private CraftManAdapter adapter;
	private int index = 0;
	private boolean isRefreshing = false;
	private String cityName;
	
	protected void loadData() {
		if(!TextUtils.isEmpty(jobName)){
			title.setText(jobName);
		}
		if(App.CHOOSECITY){
		    cityName = SharedPrefTool.getValue(SPKeys.SP_LOCATE_CITY, ValueKeys.CHOOSED_CITY_NAME, "");
		}else {
		    cityName = App.cityName;
        }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getMemberList(App.API_memberlist,App.longitude+"",App.latitude+"",cityName,"worker",jobID,"rate",1,20);
	}
	
//	@Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            //相当于Fragment的onResume
//        } else {
//            //相当于Fragment的onPause
//        }
//    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.lyt_evaluate:
			distancetext.setTextColor(android.graphics.Color.BLACK);
			evaluatetext.setTextColor(getResources().getColorStateList(R.color.textcolor));
			evaluateimg.setImageResource(R.drawable.down_yellow);
			distancetimg.setImageResource(R.drawable.down_yellow);
			distanceview.setVisibility(View.INVISIBLE);
			evaluateview.setVisibility(View.VISIBLE);
			if(index != 0){
				getMemberList(App.API_memberlist,App.longitude+"",App.latitude+"",cityName,"worker",jobID,"rate",1,20);
				index =0;
			}
			break;
		case R.id.lyt_distance:
			evaluatetext.setTextColor(android.graphics.Color.BLACK);
			distancetext.setTextColor(getResources().getColorStateList(R.color.textcolor));
			distancetimg.setImageResource(R.drawable.down_yellow);
			evaluateimg.setImageResource(R.drawable.down_yellow);
			evaluateview.setVisibility(View.INVISIBLE);
			distanceview.setVisibility(View.VISIBLE);
			if(index != 1){
				getMemberList(App.API_memberlist,App.longitude+"",App.latitude+"",cityName,"worker",jobID,"nearby",1,20);
				index =1;
			}
			break;
		default:
			break;
		}
	}
	
	public void getMemberList(String api, String lng, String lat, String areaName, String identity, int jobId
			, String orderType, int pageNumber, int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("lng", lng);
		params.addFormDataPart("lat", lat);
		params.addFormDataPart("identity", identity);
		if(areaName.endsWith("市"))
		    params.addFormDataPart("areaName", areaName.substring(0, areaName.length()-1));
        else {
            params.addFormDataPart("areaName", areaName);
        }
		params.addFormDataPart("jobId", jobId);
		params.addFormDataPart("orderType", orderType);
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftsManListActivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftsManListActivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				VDialog.getDialogInstance().closeLoadingDialog();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					CraftManResult craftManResult = JSON.parseObject(t.getResults().toString(), CraftManResult.class);
					adapter = new CraftManAdapter(CraftsManListActivity.this, craftManResult.getList(),jobName.substring(0, 1));
					craftManList.setAdapter(adapter);
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftsManListActivity.this, t.getContent());
				}
			}
		});
	}

	@Override
	protected int loadLayout() {
		return R.layout.craftmanlist;
	}

	@Override
	protected void findView() {
		back = findViewById(R.id.btn_back);
		cancel = findViewById(R.id.mCancel);
		title = (TextView)findViewById(R.id.tv_title);
		distancelyt = findViewById(R.id.lyt_distance);
		evaluatelyt = findViewById(R.id.lyt_evaluate);
		distancetext = (TextView)findViewById(R.id.distancetext);
		evaluatetext = (TextView)findViewById(R.id.evaluatetext);
		evaluateimg = (ImageView)findViewById(R.id.evaluateimg);
		distancetimg = (ImageView)findViewById(R.id.distancetimg);
		distanceview = findViewById(R.id.bg_distance);
		evaluateview = findViewById(R.id.bg_evaluate);
		craftManList = (ListView)findViewById(R.id.craftmanlist);
		
	}

	@Override
	protected void onCreate() {
	    jobID = App.JOBID;
		jobName = App.JOBNAME;  
		
		loadData();
		
	}

	@Override
	protected void setListener() {
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
		evaluatelyt.setOnClickListener(this);
		distancelyt.setOnClickListener(this);
//		craftManList.setMode(Mode.BOTH);  
//		craftManList.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));  
//		craftManList.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));  
//		craftManList.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
		craftManList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CraftMan man = (CraftMan)parent.getAdapter().getItem(position);
				Intent it = new Intent();
				it.setClass(CraftsManListActivity.this, CraftManDetailAcitvity.class);
				it.putExtra("CraftMan", JSON.toJSONString(man));
				it.putExtra("type", jobName);
				startActivity(it);
			}
		});
//		craftManList.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				if (!isRefreshing) {  
//			        isRefreshing = true;  
//			        if (craftManList.isHeaderShown()) {  
////			            refreshOnlineStatus(true);  
//			        } else if (craftManList.isFooterShown()) {  
////			            loadNextPage();  
//			        }  
//			    } else {  
//			    	craftManList.onRefreshComplete();  
//			    }  
//			}
//		});
		
	}
}
