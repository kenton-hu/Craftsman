package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.IdentifyingCode;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class ForgetpwActivity extends AbstractActivity implements OnClickListener{

	private EditText phone;
	private EditText codeInput;
	private Button getCode;
	private Button next;
	private TimeCount time;
	private String code;
	private String phoneNum;
	
	@Override
	protected int loadLayout() {
		return R.layout.forgetpw;
	}

	@Override
	protected void findView() {
		phone = (EditText)findViewById(R.id.phone);
		codeInput = (EditText)findViewById(R.id.codeinput);
		getCode = (Button)findViewById(R.id.sendcode);
		next = (Button)findViewById(R.id.next);
	}

	@Override
	protected void onCreate() {
		time = new TimeCount(60000, 1000){
			@Override
			public void onFinish() {
				getCode.setText("获取验证码");
				getCode.setClickable(true);
			}
			@Override
			public void onTick(long millisUntilFinished) {
				getCode.setClickable(false);//防止重复点击
				getCode.setText(millisUntilFinished / 1000 + "s");
			}
		};
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		getCode.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.sendcode:
			phoneNum = phone.getText().toString();
			if(TextUtils.isEmpty(phoneNum)){
				toast("请输入手机号");
				return;
			}
			if(!App.isMobileNO(phoneNum.trim())){
				toast("请输入正确的手机号");
				return;
			}
			sendIdentifyingCode(App.API_message,phoneNum,App.API_message_forget);
			break;
		case R.id.next:
			String codeNum = codeInput.getText().toString();
			if(TextUtils.isEmpty(codeNum)){
				toast("请输入验证码");
				return;
			}
//			if(!code.equals(codeNum.trim())){
//				toast("验证码错误，请重新输入");
//				return;
//			}
			confirmIdentifyingCode(App.API_message_auth, phoneNum, App.API_message_forget, codeNum);
//			Intent it = new Intent();
//			it.putExtra(App.PHONE, phoneNum);
//			it.putExtra(App.CODE, code);
//			it.putExtra(App.JUMPFROM, 1);
//			it.setClass(ForgetpwActivity.this, SetPassWordActivity.class);
//			startActivity(it);
			break;
		default:
			break;
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
				VDialog.getDialogInstance().showLoadingDialog(ForgetpwActivity.this,
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
				getCode.setClickable(false);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					toast("验证码已发送，请查看短信");
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
	public void confirmIdentifyingCode(String api, String phoneNum1, String method, final String verifycode){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("phone", phoneNum1);
		params.addFormDataPart("method", method);
		params.addFormDataPart("verifycode", verifycode);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(ForgetpwActivity.this,
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
					it.putExtra(App.PHONE, phoneNum);
					it.putExtra(App.CODE, verifycode);
					it.putExtra(App.JUMPFROM, 2);
					it.setClass(ForgetpwActivity.this, SetPassWordActivity.class);
					startActivityForResult(it, 51);
//					Intent it = new Intent();
//					it.putExtra(App.TYPE, type1);
//					it.putExtra(App.PHONE, phoneNum);
//					it.putExtra(App.CODE, codeNum);
//					it.putExtra(App.JUMPFROM, JumpFrom);
//					it.setClass(SetIdentifyingCodeActivity.this, SetPassWordActivity.class);
//					startActivityForResult(it, 31);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode == RESULT_CANCELED)
    		 return;
		 if (requestCode == 51) {
			finish();
		 }
	}
}
