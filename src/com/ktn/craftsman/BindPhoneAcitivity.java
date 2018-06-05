package com.ktn.craftsman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BindPhoneAcitivity extends AbstractActivity  implements OnClickListener{

	private EditText edtPhone;
	private Button submit;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.next:
			String phone = edtPhone.getText().toString();
			if(TextUtils.isEmpty(phone))
				Toast.makeText(BindPhoneAcitivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
			if(!App.isMobileNO(phone.trim())){
				Toast.makeText(BindPhoneAcitivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			}
			if(phone.equals(App.user.getPhone())){
				Toast.makeText(BindPhoneAcitivity.this, "该手机号已绑定", Toast.LENGTH_SHORT).show();
			}
				
			Intent it = new Intent();
			it.setClass(BindPhoneAcitivity.this, BindPhoneGetCodeAcitivity.class);
			it.putExtra("phone", phone.trim());
			startActivityForResult(it, 99);
			break;

		default:
			break;
		}
	}

	@Override
	protected int loadLayout() {
		return R.layout.bindphone;
	}

	@Override
	protected void findView() {
		edtPhone = (EditText) findViewById(R.id.phoneinput);
		submit = (Button) findViewById(R.id.next);
	}

	@Override
	protected void onCreate() {
		
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 99){
			setResult(0);
			finish();
		}
	}
}
