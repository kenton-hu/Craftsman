package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.BaseActivity.TimeCount;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.IdentifyingCode;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class BindPhoneGetCodeAcitivity extends AbstractActivity implements OnClickListener{

	private String phoneNum;
	private EditText codeInput;
	private Button sendcode;
	private TimeCount time;
//	private String code;
	@Override
	protected int loadLayout() {
		return R.layout.bindphone_getcode;
	}

	@Override
	protected void findView() {
		codeInput = (EditText)findViewById(R.id.codeinput);
		sendcode = (Button)findViewById(R.id.sendcode);
	}

	@Override
	protected void onCreate() {
		time = new TimeCount(60000, 1000){
			@Override
			public void onFinish() {
				sendcode.setText("重新发送");
				sendcode.setClickable(true);
			}
			@Override
			public void onTick(long millisUntilFinished) {
				sendcode.setClickable(false);//防止重复点击
				sendcode.setText(millisUntilFinished / 1000 + "s");
			}
		};
		phoneNum = getIntent().getStringExtra(App.PHONE);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		sendcode.setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		//发送验证码
		sendIdentifyingCode(App.API_message,phoneNum,App.API_message_change);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.sendcode:
			sendIdentifyingCode(App.API_message,phoneNum,App.API_message_change);
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
			sendbindPhone(App.API_bindPhone,phoneNum,codeNum);
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
				VDialog.getDialogInstance().showLoadingDialog(BindPhoneGetCodeAcitivity.this,
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
				sendcode.setClickable(false);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
//					IdentifyingCode identifyingCode = JSON.parseObject(t.getResults().toString(), IdentifyingCode.class);
//					code = identifyingCode.getVerify();
//					toast("验证码为："+code);
//					Log.d("hqy", "code = "+ code);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
	public void sendbindPhone(String api, String phone, String verifycode){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("phone", phone);
		params.addFormDataPart("verifycode", verifycode);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(BindPhoneGetCodeAcitivity.this,
						"正在绑定手机号",true,true,0);
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
					App.user.setPhone(phoneNum);
					SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PHONE, phoneNum);
					Intent it = new Intent();
					it.putExtra(App.PHONE, phoneNum);
					it.setClass(BindPhoneGetCodeAcitivity.this, BindSuccessActivity.class);
					startActivityForResult(it, 99);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 99){
			setResult(0);
			finish();
		}
	}
}
