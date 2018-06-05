package com.ktn.craftsman;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.BankListAdapter;
import com.ktn.craftsman.bean.BankCard;
import com.ktn.craftsman.bean.BankListResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.IdentifyingCode;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class MyBankCardActivity extends AbstractActivity implements OnClickListener {

	private ListView banklist;
	private BankListAdapter adpter;
	private ArrayList<BankCard> cardlist;
	private boolean tixian = false;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.addbankcard:
			Intent it = new Intent();
			it.setClass(MyBankCardActivity.this, AddBankCardActivity.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	@Override
	protected int loadLayout() {
		return R.layout.mycard;
	}

	@Override
	protected void findView() {
		banklist = (ListView)findViewById(R.id.banklist);
	}

	@Override
	protected void onCreate() {
		tixian = getIntent().getBooleanExtra("TIXIAN", false);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.addbankcard)).setOnClickListener(this);
		banklist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BankCard card = cardlist.get(position);
				Intent intent = new Intent();
				intent.putExtra("memberBankId", card.getId());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	protected void loadData() {
		if(tixian){
			((TextView)findViewById(R.id.tv_title)).setText("选择银行卡");
			(findViewById(R.id.addbankcard)).setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sendBankList(App.API_memberbanklist,1,20);
	}
	
	public void sendBankList(String api, int pageNumber, int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(MyBankCardActivity.this,
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
				super.onFinish();
				VDialog.getDialogInstance().closeLoadingDialog();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					BankListResult list = JSON.parseObject(t.getResults().toString(), BankListResult.class);
					if(list.getTotal() != 0){
						cardlist = list.getList();
						adpter = new BankListAdapter(MyBankCardActivity.this, list.getList());
						banklist.setAdapter(adpter);
					}else{
						if(tixian)
							toast("请先绑定银行卡再提现！");
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}

}
