package com.ktn.craftsman;

import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class SetPassWordActivity extends AbstractActivity implements OnClickListener{

	private EditText password;
	private EditText repassword;
	private View rePasswordLyt;
	private Button next;
	private String phoneNum;
	private String code;
	private String type;
	private int JumpFrom = 0; 
	
	@Override
	protected int loadLayout() {
		return R.layout.setpassword;
	}

	@Override
	protected void findView() {
		password = (EditText)findViewById(R.id.setpw);
		repassword = (EditText)findViewById(R.id.setpwnew);
		rePasswordLyt = findViewById(R.id.lyt_setpwnew);
		next = (Button)findViewById(R.id.next);
		
	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		next.setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		type = getIntent().getStringExtra(App.TYPE);
		phoneNum = getIntent().getStringExtra(App.PHONE);
		code = getIntent().getStringExtra(App.CODE);
		JumpFrom = getIntent().getIntExtra(App.JUMPFROM, 0);
		
		if(JumpFrom == 0){
			password.setHint("设置登录密码");
			rePasswordLyt.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.next:
			String passwordstr = password.getText().toString();
			if(TextUtils.isEmpty(passwordstr)){
				toast("请输入密码");
				return;
			}
			if(passwordstr.length()<4){
				toast("您输入的密码太短，请重新输入4-20位密码");
				return;
			}
			if(passwordstr.length()>20){
				toast("您输入的密码太长，请重新输入4-20位密码");
				return;
			}
			if(JumpFrom == 1 || JumpFrom == 2){
				String repasswordstr = repassword.getText().toString();
				if(TextUtils.isEmpty(repasswordstr)){
					toast("请重复密码密码");
					return;
				}
				if(repasswordstr.length()<4){
					toast("您输入的密码太短，请重新输入");
					return;
				}
				if(repasswordstr.length()>20){
					toast("您输入的密码太长，请重新输入");
					return;
				}
				if(!passwordstr.equals(repasswordstr)){
					toast("您输入的密码不一致，请重新输入");
					return;
				}
				if(JumpFrom == 1){
					//修改密码接口
					Intent intent = new Intent();
					intent.putExtra("choice", passwordstr);
					setResult(RESULT_OK, intent);
					finish();
					return;
				}else if(JumpFrom == 2){
					forgottenPW(phoneNum,passwordstr,code);
					return;
				}
			}
			Intent it = new Intent();
			it.putExtra(App.TYPE, type);
			it.putExtra(App.PHONE, phoneNum);
			it.putExtra(App.CODE, code);
			it.putExtra(App.PW, passwordstr);
			it.setClass(SetPassWordActivity.this, InvitemanActivity.class);
			startActivity(it);
			break;

		default:
			break;
		}
	}

	public void forgottenPW(String phoneNum1, String password, final String verifycode){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("username", phoneNum1);
		params.addFormDataPart("password", password);
		params.addFormDataPart("verifycode", verifycode);
		HttpRequest.post(App.serverURL+"app/forgotten.jhtml", params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(SetPassWordActivity.this,
						"正在修改密码",true,true,0);
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
					toast("修改密码成功");
					App.sGlobalHandler.postDelayed(new Runnable() {
						public void run() {
							//修改密码接口
							setResult(RESULT_OK);
							finish();
						}
					}, 1000);
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
}
