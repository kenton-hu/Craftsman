package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.CraftManAdapter;
import com.ktn.craftsman.bean.CraftManResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.util.VDialog;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class CraftsManListActivity1 extends AbstractActivity implements OnClickListener{

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
	
	@Override
	protected int loadLayout() {
		return R.layout.craftmanlist;
	}

	@Override
	protected void findView() {
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
		

	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		evaluatelyt.setOnClickListener(this);
		distancelyt.setOnClickListener(this);

	}

	@Override
	protected void loadData() {
		if(!TextUtils.isEmpty(jobName)){
			title.setText(jobName);
		}
		getMemberList(App.API_memberlist,App.longitude+"",App.latitude+"","worker",jobID,"rate",1,20);
	}

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
			break;
		case R.id.lyt_distance:
			evaluatetext.setTextColor(android.graphics.Color.BLACK);
			distancetext.setTextColor(getResources().getColorStateList(R.color.textcolor));
			distancetimg.setImageResource(R.drawable.down_yellow);
			evaluateimg.setImageResource(R.drawable.down_yellow);
			evaluateview.setVisibility(View.INVISIBLE);
			distanceview.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}

	public void getMemberList(String api, String lng, String lat, String identity, int jobId
			, String orderType, int pageNumber, int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("lng", lng);
		params.addFormDataPart("lat", lat);
		params.addFormDataPart("identity", identity);
		params.addFormDataPart("jobId", jobId);
		params.addFormDataPart("orderType", orderType);
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftsManListActivity1.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				toast("网络异常，请检查你的网络是否连接后再试");
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
					adapter = new CraftManAdapter(CraftsManListActivity1.this, craftManResult.getList(),jobName.substring(0, 1));
					craftManList.setAdapter(adapter);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
}
