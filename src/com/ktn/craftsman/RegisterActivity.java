package com.ktn.craftsman;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class RegisterActivity extends AbstractActivity implements OnClickListener{

	private String phone;
	
	@Override
	protected int loadLayout() {
		return R.layout.register;
	}

	@Override
	protected void findView() {

	}

	@Override
	protected void onCreate() {
		phone = getIntent().getStringExtra("phone");
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.craftman)).setOnClickListener(this);
		(findViewById(R.id.user)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {

	}

	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		
		case R.id.craftman:
			it.putExtra(App.TYPE, App.CRAFTMAN);
			it.setClass(RegisterActivity.this, RegisterAgreementActivity.class);
			it.putExtra("phone", phone);
			startActivity(it);
			break;
		case R.id.user:
			it.putExtra(App.TYPE, App.USER);
			it.setClass(RegisterActivity.this, RegisterAgreementActivity.class);
			it.putExtra("phone", phone);
			startActivity(it);
			break;

		default:
			break;
		}
	}

}
