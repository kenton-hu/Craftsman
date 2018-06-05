package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.CraftManResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.util.VDialog;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class EvaluateActivity extends AbstractActivity implements OnClickListener{

	private ImageView zhuanyeimg1;
	private ImageView zhuanyeimg2;
	private ImageView zhuanyeimg3;
	private ImageView zhuanyeimg4;
	private ImageView zhuanyeimg5;
	
	private float zhuanyeRate;
	private float goutongRate;
	
	private ImageView goutongimg1;
	private ImageView goutongimg2;
	private ImageView goutongimg3;
	private ImageView goutongimg4;
	private ImageView goutongimg5;
	
	private EditText result;
	
	private int id;
	
	@Override
	protected int loadLayout() {
		return R.layout.evaluate;
	}

	@Override
	protected void findView() {
		result = (EditText)findViewById(R.id.result);
		
		zhuanyeimg1 = (ImageView)findViewById(R.id.zhuanyeimg1);
		zhuanyeimg2 = (ImageView)findViewById(R.id.zhuanyeimg2);
		zhuanyeimg3 = (ImageView)findViewById(R.id.zhuanyeimg3);
		zhuanyeimg4 = (ImageView)findViewById(R.id.zhuanyeimg4);
		zhuanyeimg5 = (ImageView)findViewById(R.id.zhuanyeimg5);
		
		goutongimg1 = (ImageView)findViewById(R.id.goutongimg1);
		goutongimg2 = (ImageView)findViewById(R.id.goutongimg2);
		goutongimg3 = (ImageView)findViewById(R.id.goutongimg3);
		goutongimg4 = (ImageView)findViewById(R.id.goutongimg4);
		goutongimg5 = (ImageView)findViewById(R.id.goutongimg5);
	}

	@Override
	protected void onCreate() {
		id = getIntent().getIntExtra("id", 0);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.submit)).setOnClickListener(this);
		zhuanyeimg1.setOnClickListener(this);
		zhuanyeimg2.setOnClickListener(this);
		zhuanyeimg3.setOnClickListener(this);
		zhuanyeimg4.setOnClickListener(this);
		zhuanyeimg5.setOnClickListener(this);
		goutongimg1.setOnClickListener(this);
		goutongimg2.setOnClickListener(this);
		goutongimg3.setOnClickListener(this);
		goutongimg4.setOnClickListener(this);
		goutongimg5.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
		case R.id.mCancel: 
			finish();
		case R.id.submit:
			if(zhuanyeRate == 0 || goutongRate == 0){
				toast("请先打分再提交");
				return;
			}
			sendCustomerReviews(App.API_review,App.user.getToken(),id,(zhuanyeRate+goutongRate)/2,zhuanyeRate,goutongRate,result.getText().toString());
			break;
		case R.id.zhuanyeimg1:
			zhuanyeimg1.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg2.setImageResource(R.drawable.evaluate_gre);
			zhuanyeimg3.setImageResource(R.drawable.evaluate_gre);
			zhuanyeimg4.setImageResource(R.drawable.evaluate_gre);
			zhuanyeimg5.setImageResource(R.drawable.evaluate_gre);
			zhuanyeRate = 1;
			break;
		case R.id.zhuanyeimg2:
			zhuanyeimg1.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg2.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg3.setImageResource(R.drawable.evaluate_gre);
			zhuanyeimg4.setImageResource(R.drawable.evaluate_gre);
			zhuanyeimg5.setImageResource(R.drawable.evaluate_gre);
			zhuanyeRate = 2;
			break;
		case R.id.zhuanyeimg3:
			zhuanyeimg1.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg2.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg3.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg4.setImageResource(R.drawable.evaluate_gre);
			zhuanyeimg5.setImageResource(R.drawable.evaluate_gre);
			zhuanyeRate = 3;
			break;
		case R.id.zhuanyeimg4:
			zhuanyeimg1.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg2.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg3.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg4.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg5.setImageResource(R.drawable.evaluate_gre);
			zhuanyeRate = 4;
			break;
		case R.id.zhuanyeimg5:
			zhuanyeimg1.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg2.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg3.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg4.setImageResource(R.drawable.evaluate_red);
			zhuanyeimg5.setImageResource(R.drawable.evaluate_red);
			zhuanyeRate = 5;
			break;
		case R.id.goutongimg1:
			goutongimg1.setImageResource(R.drawable.evaluate_red);
			goutongimg2.setImageResource(R.drawable.evaluate_gre);
			goutongimg3.setImageResource(R.drawable.evaluate_gre);
			goutongimg4.setImageResource(R.drawable.evaluate_gre);
			goutongimg5.setImageResource(R.drawable.evaluate_gre);
			goutongRate = 1;
			break;
		case R.id.goutongimg2:
			goutongimg1.setImageResource(R.drawable.evaluate_red);
			goutongimg2.setImageResource(R.drawable.evaluate_red);
			goutongimg3.setImageResource(R.drawable.evaluate_gre);
			goutongimg4.setImageResource(R.drawable.evaluate_gre);
			goutongimg5.setImageResource(R.drawable.evaluate_gre);
			goutongRate = 2;
			break;
		case R.id.goutongimg3:
			goutongimg1.setImageResource(R.drawable.evaluate_red);
			goutongimg2.setImageResource(R.drawable.evaluate_red);
			goutongimg3.setImageResource(R.drawable.evaluate_red);
			goutongimg4.setImageResource(R.drawable.evaluate_gre);
			goutongimg5.setImageResource(R.drawable.evaluate_gre);
			goutongRate = 3;
			break;
		case R.id.goutongimg4:
			goutongimg1.setImageResource(R.drawable.evaluate_red);
			goutongimg2.setImageResource(R.drawable.evaluate_red);
			goutongimg3.setImageResource(R.drawable.evaluate_red);
			goutongimg4.setImageResource(R.drawable.evaluate_red);
			goutongimg5.setImageResource(R.drawable.evaluate_gre);
			goutongRate = 4;
			break;
		case R.id.goutongimg5:
			goutongimg1.setImageResource(R.drawable.evaluate_red);
			goutongimg2.setImageResource(R.drawable.evaluate_red);
			goutongimg3.setImageResource(R.drawable.evaluate_red);
			goutongimg4.setImageResource(R.drawable.evaluate_red);
			goutongimg5.setImageResource(R.drawable.evaluate_red);
			goutongRate = 5;
			break;
		default:
			break;
		}
	}

	
	public void sendCustomerReviews(String api, String token, int id, float rateGeneral, float rateProfessional, 
			float rateCommunicate, String content){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", token);
		params.addFormDataPart("id", id);
		params.addFormDataPart("rateGeneral", (int)Math.rint(rateGeneral));
		params.addFormDataPart("rateProfessional", (int)rateProfessional);
		params.addFormDataPart("rateCommunicate", (int)rateCommunicate);
		params.addFormDataPart("content", content);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(EvaluateActivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(EvaluateActivity.this, "网络异常，请检查你的网络是否连接后再试");
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
					VDialog.getDialogInstance().toast(EvaluateActivity.this, t.getContent());
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(EvaluateActivity.this, t.getContent());
				}
				App.sGlobalHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						finish();
					}
				}, 1500);
			}
		});
	}
}
