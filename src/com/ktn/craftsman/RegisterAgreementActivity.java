package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.NameAuthResult;
import com.ktn.craftsman.bean.Setting;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class RegisterAgreementActivity extends AbstractActivity implements OnClickListener{

	private String type;
	private String phoneNum;
	private TextView content;
	
	@Override
	protected int loadLayout() {
		return R.layout.registeragreement;
	}

	@Override
	protected void findView() {
		content = (TextView)findViewById(R.id.content);
	}

	@Override
	protected void onCreate() {
		phoneNum = getIntent().getStringExtra("phone");
		type = getIntent().getStringExtra(App.TYPE);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.agree)).setOnClickListener(this);

	}

	@Override
	protected void loadData() {
		getSetting("app/setting.jhtml");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.agree:
			Intent it = new Intent();
			it.putExtra(App.TYPE, type);
			it.setClass(RegisterAgreementActivity.this, SetPhoneActivity.class);
			it.putExtra("phone", phoneNum);
			startActivity(it);
			break;

		default:
			break;
		}
	}
	
	public void getSetting(String api){
		RequestParams params = new RequestParams(this);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(RegisterAgreementActivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(RegisterAgreementActivity.this, "网络异常，请检查你的网络是否连接后再试");
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
					Setting result = JSON.parseObject(t.getResults().toString(), Setting.class);
					content.setText(Html.fromHtml(result.getConsumerRegisterAgreement()));
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(RegisterAgreementActivity.this, t.getContent());
				}
			}
		});
	}

}
