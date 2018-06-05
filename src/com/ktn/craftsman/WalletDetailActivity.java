package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.ReviewAdapter;
import com.ktn.craftsman.adapter.WalletDetailAdapter;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.IdentifyingCode;
import com.ktn.craftsman.bean.ReviewResult;
import com.ktn.craftsman.bean.WalletDetailResult;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class WalletDetailActivity extends AbstractActivity implements OnClickListener{

	private ListView listview;
	private TextView tip;
	private WalletDetailAdapter adapter;
	
	@Override
	protected int loadLayout() {
		return R.layout.walletdatail;
	}

	@Override
	protected void findView() {
		listview = (ListView)findViewById(R.id.listview);
		tip = (TextView)findViewById(R.id.tip);
	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		sendDepositList(App.API_depositlist,1,20);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	public void sendDepositList(String api,int pageNumber,int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(WalletDetailActivity.this,
						"正在加载中",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				toast("网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				VDialog.getDialogInstance().closeLoadingDialog();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					WalletDetailResult result = JSON.parseObject(t.getResults().toString(), WalletDetailResult.class);
					if(result.getTotal() == 0)
						tip.setVisibility(View.VISIBLE);
					else{
						tip.setVisibility(View.GONE);
					
						adapter = new WalletDetailAdapter(WalletDetailActivity.this, result.getList());
						listview.setAdapter(adapter);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
}
