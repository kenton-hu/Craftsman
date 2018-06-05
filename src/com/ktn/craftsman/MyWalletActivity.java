package com.ktn.craftsman;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.Balance;
import com.ktn.craftsman.bean.Baseinfo;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.LoginResult;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.ValueKeys;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class MyWalletActivity extends AbstractActivity implements OnClickListener {

	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_more:
			it.setClass(MyWalletActivity.this, WalletDetailActivity.class);
			startActivity(it);
			break;
		case R.id.next:
			if(App.baseinfo.getBalance() == 0){
				toast("余额为0，无法提现");
				break;
			}
			it.putExtra("TIXIAN", true);
			it.putExtra("MAX", App.baseinfo.getBalance());
			it.setClass(MyWalletActivity.this, InfoEditActivity.class);
			startActivityForResult(it, 21);
			break;
		default:
			break;
		}

	}

	@Override
	protected int loadLayout() {
		// TODO Auto-generated method stub
		return R.layout.mywallet;
	}

	@Override
	protected void findView() {
		((TextView)findViewById(R.id.balance)).setText("¥" + App.baseinfo.getBalance());
	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.tv_more)).setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == 21){
				long temp = data.getLongExtra("TIXIAN", 0);
				int cardNo = data.getIntExtra("memberBankId", 0);
				sendAtmSave(cardNo,temp);
			}
		}
	}
	
	public void sendAtmSave(int memberBankId, long amount){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("memberBankId", memberBankId);
		params.addFormDataPart("amount", amount);
		HttpRequest.post(App.serverURL+App.API_atmsave, params, new BaseHttpRequestCallback<HttpResult>(){
			
			
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					toast("提现申请已提交！");
					if(t.getResults() != null){
					    Balance balance = JSON.parseObject(t.getResults().toString(), Balance.class);
					    if(balance != null && balance.getBalance() != 0){
					        App.baseinfo.setBalance(balance.getBalance());
					        ((TextView)findViewById(R.id.balance)).setText("¥" + App.baseinfo.getBalance());
					    }
					}
				}
			}
		});
	}
	
	public void getUserInfo(){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+App.API_baseInfo, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					App.baseinfo = JSON.parseObject(t.getResults().toString(), Baseinfo.class);
				}
			}
		});
	}
}
