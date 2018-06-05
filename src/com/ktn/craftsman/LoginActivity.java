package com.ktn.craftsman;

import javax.crypto.spec.PSource;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.BaseActivity.TimeCount;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.IdentifyingCode;
import com.ktn.craftsman.bean.LoginResult;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class LoginActivity extends AbstractActivity implements OnClickListener{

	private EditText phone;
	private EditText password;
	private Button submit;
	private View register;
	private View forgetpw;
	private View loginbyqq;
	private String lastUserPhone;
	private TextView passwordlogin;
	private TextView verifylogin;
	private View rlverify;
	private View rlpasswordlogin;
	private String loginType = "password";
	private TextView reSendCode;
	private TimeCount time;
	private EditText codeInput;
	
	@Override
	protected int loadLayout() {
		return R.layout.login;
	}

	@Override
	protected void findView() {
		phone = (EditText)findViewById(R.id.phone);
		password = (EditText)findViewById(R.id.password);
		submit = (Button)findViewById(R.id.login);
		register = findViewById(R.id.register);
		forgetpw = findViewById(R.id.forgetpw);
		loginbyqq = findViewById(R.id.login_qq);
		passwordlogin = (TextView)findViewById(R.id.passwordlogin);
		verifylogin = (TextView)findViewById(R.id.verifylogin);
		rlverify = findViewById(R.id.rlverify);
		rlpasswordlogin = findViewById(R.id.rlpasswordlogin);
		
		reSendCode = (TextView)findViewById(R.id.retry);
		codeInput = (EditText)findViewById(R.id.code);
	}

	@Override
	protected void setListener() {
	    (findViewById(R.id.btn_back)).setOnClickListener(this);
        (findViewById(R.id.mCancel)).setOnClickListener(this);
        (findViewById(R.id.retry)).setOnClickListener(this);
		submit.setOnClickListener(this);
		register.setOnClickListener(this);
		forgetpw.setOnClickListener(this);
		loginbyqq.setOnClickListener(this);
		passwordlogin.setOnClickListener(this);
		verifylogin.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		Intent it = new Intent();
    	it.setClass(this, MainActivity.class);
    	startActivity(it);
	}
	
	@Override
	protected void loadData() {
		toast("请先登录！");
		lastUserPhone = SharedPrefTool.getValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PHONE, "");
		boolean auto = SharedPrefTool.getValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, false);
		if(auto && !TextUtils.isEmpty(lastUserPhone)){
			finish();
			App.user.setPhone(lastUserPhone);
			Intent it = new Intent();
			it.setClass(LoginActivity.this, MainActivity.class);
			startActivity(it);
			finish();
		}
		if(!TextUtils.isEmpty(lastUserPhone)){
			phone.setText(lastUserPhone);
		}
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
	}

	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		switch (v.getId()) {
		case R.id.btn_back:
        case R.id.mCancel: 
            finish();
        	it.setClass(this, MainActivity.class);
        	startActivity(it);
        	break;
        case R.id.retry:
        	//账户密码登录
			String phoneNum1 = phone.getText().toString();
			if(TextUtils.isEmpty(phoneNum1)){
				toast("请输入手机号");
				return;
			}
			if(!App.isMobileNO(phoneNum1.trim())){
				toast("请输入手机号");
				return;
			}
			sendIdentifyingCode(App.API_message,phoneNum1,App.API_message_login);
			break;
        case R.id.passwordlogin:
        	loginType = "password";
        	passwordlogin.setBackgroundColor(getResources().getColor(R.color.red));
        	verifylogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.input));
        	rlpasswordlogin.setVisibility(View.VISIBLE);
        	rlverify.setVisibility(View.GONE);
        	break;
        case R.id.verifylogin:
        	loginType = "verify";
        	verifylogin.setBackgroundColor(getResources().getColor(R.color.red));
        	passwordlogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.input));
        	rlpasswordlogin.setVisibility(View.GONE);
        	rlverify.setVisibility(View.VISIBLE);
        	break;
		case R.id.login:
			//账户密码登录
			String phoneNum = phone.getText().toString();
			if(TextUtils.isEmpty(phoneNum)){
				toast("请输入手机号");
				return;
			}
			if(!App.isMobileNO(phoneNum.trim())){
				toast("请输入手机号");
				return;
			}
			if(loginType.equals("password")){
				String passwordstr = password.getText().toString();
				if(TextUtils.isEmpty(passwordstr)){
					toast("请输入密码");
					return;
				}
				if(passwordstr.length()<4){
					toast("您输入的密码太短，请输入4-20位密码");
					return;
				}
				if(passwordstr.length()>20){
					toast("您输入的密码太长，请输入4-20位密码");
					return;
				}
				sendLogin(App.API_login, phoneNum, passwordstr,loginType,"");
			}else if(loginType.equals("verify")){
				String codeNum = codeInput.getText().toString();
				if(TextUtils.isEmpty(codeNum)){
					toast("请输入验证码");
					return;
				}
				sendLogin(App.API_login, phoneNum, "",loginType,codeNum);
			}
			break;
		case R.id.register:
			it.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(it);
			break;
		case R.id.forgetpw:
			it.setClass(LoginActivity.this, ForgetpwActivity.class);
			startActivity(it);
			break;
		case R.id.login_qq:
			//QQ登录
			break;
		default:
			break;
		}
	}

	public void sendLogin(String api, final String phoneNum, final String password, String loginType, String verifycode){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("username", phoneNum);
		if(!TextUtils.isEmpty(password))
			params.addFormDataPart("password", password);
		params.addFormDataPart("loginType", loginType);
		if(!TextUtils.isEmpty(verifycode))
			params.addFormDataPart("verifycode", verifycode);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(LoginActivity.this,
						"正在登录",true,true,0);
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
					App.isLogin = true;
					LoginResult loginResult = JSON.parseObject(t.getResults().toString(), LoginResult.class);
					App.user.setPhone(phoneNum);
					SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PHONE, phoneNum);
					App.user.setToken(loginResult.getToken());
					App.user.setPassword(password);
					SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PASSWORD, password);
					SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, true);
					Intent it = new Intent();
					it.putExtra("login", true);
					it.setClass(LoginActivity.this, MainActivity.class);
					startActivity(it);
				}else if(!TextUtils.isEmpty(t.getContent())){
					if(t.getContent().equals("此账号不存在")){
						toast("此账号不存在,请先注册！");
						Intent it = new Intent();
						it.setClass(LoginActivity.this, RegisterActivity.class);
						it.putExtra("phone", phoneNum);
						startActivity(it);
					}
					else
						toast(t.getContent());
				}
			}
		});
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
				VDialog.getDialogInstance().showLoadingDialog(LoginActivity.this,
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
				
				if(t.getType().equals(App.SUCCESS)){
//					code = t.getResults().toString();
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
}
