package com.ktn.craftsman;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BindSuccessActivity extends AbstractActivity implements OnClickListener{

	private TextView phone;
	private String phoneNum;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.backtohomepage:
			setResult(0);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected int loadLayout() {
		// TODO Auto-generated method stub
		return R.layout.bindsuccess;
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		phone = (TextView)findViewById(R.id.phone);
	}

	@Override
	protected void onCreate() {
		phoneNum = getIntent().getStringExtra(App.PHONE);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.backtohomepage)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		phone.setText(phoneNum);
	}

}
