package com.ktn.craftsman;

import java.util.Date;

import android.content.Intent;
import android.graphics.Matrix;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class InfoEditActivity extends AbstractActivity implements OnClickListener {

	private EditText input;
	private boolean tixian = false;
	private long max = 0;
	private long temp;
	
	@Override
	protected int loadLayout() {
		return R.layout.infoedit;
	}

	@Override
	protected void findView() {
		input = (EditText)findViewById(R.id.input);
	}

	@Override
	protected void onCreate() {
		tixian = getIntent().getBooleanExtra("TIXIAN", false);
		max = getIntent().getLongExtra("MAX", 0);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		if(tixian){
			((TextView)findViewById(R.id.tv_title)).setText("提现");
			input.setHint("最多可提现"+max+"元");
			input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(InfoEditActivity.this,PersonInfoAcitivity.class);
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.next:
			String str = input.getText().toString();
			if(tixian){
				if(TextUtils.isEmpty(str)){
					toast("请输入提现金额");
					return;
				}
				temp =  Long.parseLong(str);
				if(temp < 0 || temp > max){
					toast("提现金额超过了余额");
					return;
				}
				intent.putExtra("TIXIAN", true);
				intent.setClass(InfoEditActivity.this, MyBankCardActivity.class);
				startActivityForResult(intent, 41);
				return;
			}
			if(TextUtils.isEmpty(str)){
				toast("请输入信息");
				return;
			}
			intent.putExtra("choice", str);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == 41){
				data.putExtra("TIXIAN", temp);
				setResult(RESULT_OK, data);
				finish();
			}
		}
	}

}
