package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.NameAuthResult;
import com.ktn.craftsman.bean.RenewFee;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class RenewActivity extends AbstractActivity implements OnClickListener {

	private TextView annualFeeTxt;
	private TextView monthlyFeeTxt;
	private TextView feeTxt;
	
	private RenewFee renewFee;
	private String renewType = "monthlyFee";
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.annualtxt:
			renewType = "annualFee";
			annualFeeTxt.setTextColor(getResources().getColor(R.color.deepskyblue));
			annualFeeTxt.setBackgroundResource(R.drawable.bg_renew_blue);
			monthlyFeeTxt.setTextColor(getResources().getColor(R.color.gray));
			monthlyFeeTxt.setBackgroundResource(R.drawable.bg_renew_gray);
			if(renewFee != null)
				feeTxt.setText(renewFee.getAnnualFee()+"元");
			break;
		case R.id.mounthtxt:
			renewType = "monthlyFee";
			monthlyFeeTxt.setTextColor(getResources().getColor(R.color.deepskyblue));
			monthlyFeeTxt.setBackgroundResource(R.drawable.bg_renew_blue);
			annualFeeTxt.setTextColor(getResources().getColor(R.color.gray));
			annualFeeTxt.setBackgroundResource(R.drawable.bg_renew_gray);
			if(renewFee != null)
				feeTxt.setText(renewFee.getMonthlyFee()+"元");
			break;
		case R.id.next:
//			sendRenewFee("app/renew/submit.jhtml", renewType);	
			Intent intent = new Intent();
			intent.setClass(RenewActivity.this, ChoosePayMethodActivity.class);
			intent.putExtra("fee", feeTxt.getText().toString());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected int loadLayout() {
		return R.layout.renew;
	}

	@Override
	protected void findView() {
		annualFeeTxt = (TextView)findViewById(R.id.annualtxt);
		monthlyFeeTxt = (TextView)findViewById(R.id.mounthtxt);
		feeTxt= (TextView)findViewById(R.id.fee);
	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
		annualFeeTxt.setOnClickListener(this);
		monthlyFeeTxt.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    getRenewFee("app/renew/fee.jhtml");
	}
	
	public void getRenewFee(String api){
		RequestParams params = new RequestParams(this);
	    params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(RenewActivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(RenewActivity.this, "网络异常，请检查你的网络是否连接后再试");
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
				    if(TextUtils.isEmpty(t.getResults().toString()))
				        return;
					renewFee = JSON.parseObject(t.getResults().toString(), RenewFee.class);
					if(renewFee != null)
					    feeTxt.setText(renewFee.getMonthlyFee()+"元");
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(RenewActivity.this, t.getContent());
				}
			}
		});
	}
	
	public void sendRenewFee(String api,String renewType){
		RequestParams params = new RequestParams(this);
	    params.addFormDataPart("token", App.user.getToken());
	    params.addFormDataPart("renewType", renewType);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(RenewActivity.this,
						"正在提交续费申请",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(RenewActivity.this, "网络异常，请检查你的网络是否连接后再试");
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
					VDialog.getDialogInstance().toast(RenewActivity.this, t.getContent());
					finish();
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(RenewActivity.this, t.getContent());
				}
			}
		});
	}
	
	public void getPaymentPluginList(String api){
		RequestParams params = new RequestParams(this);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					renewFee = JSON.parseObject(t.getResults().toString(), RenewFee.class);
				}
			}
		});
	}

}
