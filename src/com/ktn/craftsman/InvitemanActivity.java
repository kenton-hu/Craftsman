package com.ktn.craftsman;

import org.w3c.dom.Text;

import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class InvitemanActivity extends AbstractActivity implements OnClickListener {

	private String phoneNum;
	private String code;
	private String type;
	private String password;
	private EditText invite;
	
	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.invite_yes:
			String inviteNum = invite.getText().toString();
			if(TextUtils.isEmpty(inviteNum)){
				toast("请输入邀请人手机号码");
				return;
			}
			resigter(App.API_register,type,phoneNum,password,code,inviteNum);
			break;
		case R.id.invite_no:
			resigter(App.API_register,type,phoneNum,password,code,null);
			break;
		default:
			break;
		}
	}

	@Override
	protected int loadLayout() {
		return R.layout.inviteman;
	}

	@Override
	protected void findView() {
		(findViewById(R.id.invite_yes)).setOnClickListener(this);
		(findViewById(R.id.invite_no)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		invite = (EditText)findViewById(R.id.phone);
	}

	@Override
	protected void onCreate() {
		
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void loadData() {
		type = getIntent().getStringExtra(App.TYPE);
		phoneNum = getIntent().getStringExtra(App.PHONE);
		code = getIntent().getStringExtra(App.CODE);
		password = getIntent().getStringExtra(App.PW);
	}

	public void resigter(String api,final String userType, final String phoneNum, final String password, String code, String invite){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("identity", userType);
		params.addFormDataPart("username", phoneNum);
		params.addFormDataPart("password", password);
		params.addFormDataPart("verifycode", code);
		if(!TextUtils.isEmpty(invite))
			params.addFormDataPart("invitecode", invite);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(InvitemanActivity.this,
						"正在注册",true,true,0);
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
					SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PHONE, phoneNum);
				    SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PASSWORD, password);
					App.user.setPhone(phoneNum);
					App.user.setIdentity(userType);
					Intent it = new Intent();
					it.putExtra("resigter", true);
					it.setClass(InvitemanActivity.this, MainActivity.class);
					startActivity(it);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
}
