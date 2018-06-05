package com.ktn.craftsman;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.AD;
import com.ktn.craftsman.bean.ADResult;
import com.ktn.craftsman.bean.Bank;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;
import com.squareup.picasso.Picasso;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class AddBankCardActivity extends AbstractActivity implements OnClickListener{

	private EditText edtName;
	private EditText edtID;
	private EditText edtBankCard;
	private TextView tvBankChosed;
	private View bankChoose;
	private Button btnConfrom;
	
	private ArrayList<Bank> banklist;
	private Handler mHandler;
	private int chooseID = -1;
	private String chooseName;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bankchoose:
			if(banklist == null || banklist.size() == 0)
				return;
			VDialog.getDialogInstance().showBankAlertDialog(AddBankCardActivity.this, banklist, mHandler);
			break;
		case R.id.next:
			String name = edtName.getText().toString();
			if(TextUtils.isEmpty(name)){
				toast("请输入姓名");
				return;
			}
			String id = edtID.getText().toString();
			if(TextUtils.isEmpty(id)){
				toast("请输入身份证号");
				return;
			}
			if(!App.isIDNO(id)){
				toast("请输入正确的身份证号");
				return;
			}
			if(TextUtils.isEmpty(chooseName)){
				toast("请选择开户银行");
				return;
			}
			String card = edtBankCard.getText().toString();
			if(TextUtils.isEmpty(card)){
				toast("请输入银行卡号");
				return;
			}
			
			sendAddBank(App.API_bindbank,chooseID,name,id,card);
			break;
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}		
	}

	public void sendAddBank(String api, int bankId, String idName, String idNo, String cardNo){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("bankId", bankId);
		params.addFormDataPart("idName", idName);
		params.addFormDataPart("idNo", idNo);
		params.addFormDataPart("cardNo", cardNo);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					VDialog.getDialogInstance().toast(AddBankCardActivity.this, "添加银行卡成功！");
					finish();
				}else if(!TextUtils.isEmpty(t.getContent())){
//					VDialog.getDialogInstance().toast(AddBankCardActivity.this, t.getContent());
					VDialog.getDialogInstance().toast(AddBankCardActivity.this, "银行卡号错误");
				}
			}
		});
	}
	
	@Override
	protected int loadLayout() {
		return R.layout.addcard;
	}

	@Override
	protected void findView() {
		edtName = (EditText) findViewById(R.id.setpw);
		edtID = (EditText) findViewById(R.id.ID);
		edtBankCard = (EditText) findViewById(R.id.bankID);
		bankChoose = findViewById(R.id.bankchoose);
		tvBankChosed = (TextView)findViewById(R.id.bankchosed);
		btnConfrom = (Button) findViewById(R.id.next);		
	}

	@Override
	protected void onCreate() {
		mHandler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == VDialog.OK){
					chooseID = msg.arg1;
					Bundle bundle = msg.getData();
					if(bundle != null){
						chooseName = bundle.getString("name");
						if(!TextUtils.isEmpty(chooseName))
							tvBankChosed.setText(chooseName);
					}
				}
			}
		};
	}

	@Override
	protected void setListener() {
		findViewById(R.id.mCancel).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);	
		bankChoose.setOnClickListener(this);
		btnConfrom.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		getBankList(App.API_banklist);
	}
	
	
	public void getBankList(String api){
		RequestParams params = new RequestParams(this);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					banklist = (ArrayList<Bank>) JSON.parseArray(t.getResults().toString(), Bank.class);
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
}
