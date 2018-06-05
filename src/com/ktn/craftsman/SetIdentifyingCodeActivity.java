package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.IdentifyingCode;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class SetIdentifyingCodeActivity extends AbstractActivity implements OnClickListener {

	private TextView reSendCode;
	private TextView tip;
	private String phoneNum;
	private String type1;
	private TimeCount time;
	private EditText codeInput;
	private int JumpFrom = 0;
	private String codeNum;
	
	@Override
	protected int loadLayout() {
		return R.layout.setidentifyingcode;
	}

	@Override
	protected void findView() {
		reSendCode = (TextView)findViewById(R.id.retry);
		tip = (TextView)findViewById(R.id.tip);
		codeInput = (EditText)findViewById(R.id.code);
	}

	@Override
	protected void onCreate() {
		time = new TimeCount(60000, 1000){
			@Override
			public void onFinish() {
				reSendCode.setText("重新发送");
				reSendCode.setClickable(true);
			}
			@Override
			public void onTick(long millisUntilFinished) {
				reSendCode.setClickable(false);//防止重复点击
				reSendCode.setText(millisUntilFinished / 1000 + "s");
			}
		};
		type1 = getIntent().getStringExtra(App.TYPE);
		phoneNum = getIntent().getStringExtra(App.PHONE);
		JumpFrom = getIntent().getIntExtra(App.JUMPFROM, 0);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.retry)).setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		if(JumpFrom == 0){
			//发送验证码
			sendIdentifyingCode(App.API_message,phoneNum,App.API_message_register);
		}else if(JumpFrom == 1){
			sendIdentifyingCode(App.API_message,phoneNum,"changePassword");
		}
		
		tip.setText("已向手机"+phoneNum+"发送了一条短信验证码");
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.retry:
			if(JumpFrom == 0){
				sendIdentifyingCode(App.API_message,phoneNum,App.API_message_register);
			}else if(JumpFrom == 1){
				sendIdentifyingCode(App.API_message,phoneNum,"changePassword");
			}
			break;
		case R.id.next:
			
			codeNum = codeInput.getText().toString();
			if(TextUtils.isEmpty(codeNum)){
				toast("请输入验证码");
				return;
			}
			if(JumpFrom == 0){
				confirmIdentifyingCode(App.API_message_auth, phoneNum, App.API_message_register, codeNum);
			}else if(JumpFrom == 1){
				confirmIdentifyingCode(App.API_message_auth, phoneNum, "changePassword", codeNum);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode == RESULT_CANCELED)
    		 return;
		 if (requestCode == 31) {
			 Intent intent = new Intent();
			 String result = data.getStringExtra("choice");
			 intent.putExtra("choice", result);
			 intent.putExtra("verifycode", codeNum);
			setResult(RESULT_OK, intent);
			finish();
		 }
	}
	
	public void sendIdentifyingCode(String api, String phoneNum, String method){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("phone", phoneNum);
		params.addFormDataPart("method", method);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				time.start();// 开始计时
				tip.setVisibility(View.INVISIBLE);
				VDialog.getDialogInstance().showLoadingDialog(SetIdentifyingCodeActivity.this,
						"正在发送验证码",true,true,0);
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
				reSendCode.setClickable(false);
				VDialog.getDialogInstance().closeLoadingDialog();
				tip.setVisibility(View.VISIBLE);
				
				if(t.getType().equals(App.SUCCESS)){
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
	public void confirmIdentifyingCode(String api, String phoneNum1, String method, String verifycode){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("phone", phoneNum1);
		params.addFormDataPart("method", method);
		params.addFormDataPart("verifycode", verifycode);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(SetIdentifyingCodeActivity.this,
						"正在验证",true,true,0);
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
					Intent it = new Intent();
					it.putExtra(App.TYPE, type1);
					it.putExtra(App.PHONE, phoneNum);
					it.putExtra(App.CODE, codeNum);
					it.putExtra(App.JUMPFROM, JumpFrom);
					it.setClass(SetIdentifyingCodeActivity.this, SetPassWordActivity.class);
					startActivityForResult(it, 31);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
}
