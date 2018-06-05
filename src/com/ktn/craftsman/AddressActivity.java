package com.ktn.craftsman;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AddressActivity extends AbstractActivity implements OnClickListener{

	private TextView address;
	private String intentAddress;
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected int loadLayout() {
		// TODO Auto-generated method stub
		return R.layout.address;
	}

	@Override
	protected void findView() {
		address = (TextView)findViewById(R.id.address);
	}

	@Override
	protected void onCreate() {
		intentAddress = getIntent().getStringExtra("address");
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
//		if()
	}

}
