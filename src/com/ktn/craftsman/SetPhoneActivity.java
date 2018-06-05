package com.ktn.craftsman;

import android.content.Intent;
import android.renderscript.Type;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class SetPhoneActivity extends AbstractActivity implements OnClickListener{

	private EditText phone;
	private View qq;
	private View next;
	private String type;
	private String phoneNum;
	
	@Override
	protected int loadLayout() {
		return R.layout.setphone;
	}

	@Override
	protected void findView() {
		phone = (EditText)findViewById(R.id.phone);
		qq = findViewById(R.id.login_qq);
		next = findViewById(R.id.next);
	}

	@Override
	protected void onCreate() {
		phoneNum = getIntent().getStringExtra("phone");
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.phone)).setOnClickListener(this);
		(findViewById(R.id.login_qq)).setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		type = getIntent().getStringExtra(App.TYPE);
		if(!TextUtils.isEmpty(phoneNum))
			phone.setText(phoneNum);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.login_qq:
			
			break;
		case R.id.next:
			String phoneNum = phone.getText().toString();
			if(TextUtils.isEmpty(phoneNum)){
				toast("请输入手机号");
				return;
			}
			if(!App.isMobileNO(phoneNum.trim())){
				toast("请输入正确的手机号");
				return;
			}
				
			Intent it = new Intent();
			it.putExtra(App.TYPE, type);
			it.putExtra(App.PHONE, phoneNum.trim());
			it.setClass(SetPhoneActivity.this, SetIdentifyingCodeActivity.class);
			startActivity(it);
			break;

		default:
			break;
		}
	}

}
