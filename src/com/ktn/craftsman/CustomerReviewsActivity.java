package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.ReviewAdapter;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.CraftManDetail;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.ReviewResult;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class CustomerReviewsActivity extends AbstractActivity implements OnClickListener{

	private TextView review;
	private View tip;
	private TextView reviewRate;
	private TextView zhuanyeReview;
	private TextView toutongReview;
	private ListView reviewListView;
	private ReviewAdapter adapter;
	
	private ImageView zhuanyeImg1;
	private ImageView zhuanyeImg2;
	private ImageView zhuanyeImg3;
	private ImageView zhuanyeImg4;
	private ImageView zhuanyeImg5;
	
	private ImageView goutongImg1;
	private ImageView goutongImg2;
	private ImageView goutongImg3;
	private ImageView goutongImg4;
	private ImageView goutongImg5;
	
	private CraftMan man;
	private CraftManDetail detail;
	
	@Override
	protected int loadLayout() {
		return R.layout.customerreviews;
	}

	@Override
	protected void findView() {
		review = (TextView)findViewById(R.id.marks);
		tip = findViewById(R.id.tip);
		reviewRate = (TextView)findViewById(R.id.evaluatetext);
		zhuanyeReview = (TextView)findViewById(R.id.zhuanyemarks);
		toutongReview = (TextView)findViewById(R.id.goutongmarks);
		reviewListView = (ListView)findViewById(R.id.reviewslist);
		
		zhuanyeImg1 = (ImageView)findViewById(R.id.zhuanyeimg1);
		zhuanyeImg2 = (ImageView)findViewById(R.id.zhuanyeimg2);
		zhuanyeImg3 = (ImageView)findViewById(R.id.zhuanyeimg3);
		zhuanyeImg4 = (ImageView)findViewById(R.id.zhuanyeimg4);
		zhuanyeImg5 = (ImageView)findViewById(R.id.zhuanyeimg5);
		
		goutongImg1 = (ImageView)findViewById(R.id.goutongimg1);
		goutongImg2 = (ImageView)findViewById(R.id.goutongimg2);
		goutongImg3 = (ImageView)findViewById(R.id.goutongimg3);
		goutongImg4 = (ImageView)findViewById(R.id.goutongimg4);
		goutongImg5 = (ImageView)findViewById(R.id.goutongimg5);
	}

	@Override
	protected void onCreate() {
		String json = getIntent().getStringExtra("man");
		if(!TextUtils.isEmpty(json)){
			man = JSON.parseObject(json, CraftMan.class); 
		}
		String jsondetail = getIntent().getStringExtra("mandetail");
		if(!TextUtils.isEmpty(jsondetail)){
			detail = JSON.parseObject(jsondetail, CraftManDetail.class); 
		}
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		findViewById(R.id.review).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		if(detail == null)
			return;
		review.setText(detail.getRateGeneral()+"");
		
		float zhuanyeevaluate = detail.getRateProfessional();
		zhuanyeReview.setText(zhuanyeevaluate+"");
        if(zhuanyeevaluate == 0.0){
        	zhuanyeImg1.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg2.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg3.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
        }else if (zhuanyeevaluate > 0.0 & zhuanyeevaluate <= 0.5) {
        	zhuanyeImg1.setImageResource(R.drawable.evaluate_red_half);
        	zhuanyeImg2.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg3.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 0.5 & zhuanyeevaluate <= 1.0) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red);
			zhuanyeImg2.setImageResource(R.drawable.evaluate_gre);
			zhuanyeImg3.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 1.0 & zhuanyeevaluate <= 1.5) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red_half);
        	zhuanyeImg3.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 1.5 & zhuanyeevaluate <= 2.0) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red); 
        	zhuanyeImg3.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 2.0 & zhuanyeevaluate <= 2.5) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red);  
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg3.setImageResource(R.drawable.evaluate_red_half);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 2.5 & zhuanyeevaluate <= 3.0) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg3.setImageResource(R.drawable.evaluate_red);
        	zhuanyeImg4.setImageResource(R.drawable.evaluate_gre);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 3.0 & zhuanyeevaluate <= 3.5) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg3.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg4.setImageResource(R.drawable.evaluate_red_half);
        	zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 3.5 & zhuanyeevaluate <= 4.0) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg3.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg4.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zhuanyeevaluate > 4.0 & zhuanyeevaluate <= 4.5) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg3.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg4.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg5.setImageResource(R.drawable.evaluate_red_half);
		}else if (zhuanyeevaluate > 4.5 & zhuanyeevaluate <= 5.0) {
			zhuanyeImg1.setImageResource(R.drawable.evaluate_red); 
            zhuanyeImg2.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg3.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg4.setImageResource(R.drawable.evaluate_red);
            zhuanyeImg5.setImageResource(R.drawable.evaluate_red);
		}
        
        float goutongevaluate = detail.getRateCommunicate();
        toutongReview.setText(goutongevaluate+"");
        if(goutongevaluate == 0.0){
        	goutongImg1.setImageResource(R.drawable.evaluate_gre);
        	goutongImg2.setImageResource(R.drawable.evaluate_gre);
        	goutongImg3.setImageResource(R.drawable.evaluate_gre);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
        }else if (goutongevaluate > 0.0 & goutongevaluate <= 0.5) {
        	goutongImg1.setImageResource(R.drawable.evaluate_red_half);
        	goutongImg2.setImageResource(R.drawable.evaluate_gre);
        	goutongImg3.setImageResource(R.drawable.evaluate_gre);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 0.5 & goutongevaluate <= 1.0) {
        	goutongImg1.setImageResource(R.drawable.evaluate_red);
        	goutongImg2.setImageResource(R.drawable.evaluate_gre);
        	goutongImg3.setImageResource(R.drawable.evaluate_gre);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 1.0 & goutongevaluate <= 1.5) {
			goutongImg1.setImageResource(R.drawable.evaluate_red);
            goutongImg2.setImageResource(R.drawable.evaluate_red_half);
        	goutongImg3.setImageResource(R.drawable.evaluate_gre);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 1.5 & goutongevaluate <= 2.0) {
			goutongImg1.setImageResource(R.drawable.evaluate_red); 
            goutongImg2.setImageResource(R.drawable.evaluate_red); 
        	goutongImg3.setImageResource(R.drawable.evaluate_gre);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 2.0 & goutongevaluate <= 2.5) {
			goutongImg1.setImageResource(R.drawable.evaluate_red);  
            goutongImg2.setImageResource(R.drawable.evaluate_red); 
            goutongImg3.setImageResource(R.drawable.evaluate_red_half);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 2.5 & goutongevaluate <= 3.0) {
			goutongImg1.setImageResource(R.drawable.evaluate_red); 
            goutongImg2.setImageResource(R.drawable.evaluate_red);
            goutongImg3.setImageResource(R.drawable.evaluate_red);
        	goutongImg4.setImageResource(R.drawable.evaluate_gre);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 3.0 & goutongevaluate <= 3.5) {
			goutongImg1.setImageResource(R.drawable.evaluate_red); 
            goutongImg2.setImageResource(R.drawable.evaluate_red);
            goutongImg3.setImageResource(R.drawable.evaluate_red);
            goutongImg4.setImageResource(R.drawable.evaluate_red_half);
        	goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 3.5 & goutongevaluate <= 4.0) {
			goutongImg1.setImageResource(R.drawable.evaluate_red); 
            goutongImg2.setImageResource(R.drawable.evaluate_red);
            goutongImg3.setImageResource(R.drawable.evaluate_red);
            goutongImg4.setImageResource(R.drawable.evaluate_red);
            goutongImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (goutongevaluate > 4.0 & goutongevaluate <= 4.5) {
			goutongImg1.setImageResource(R.drawable.evaluate_red); 
            goutongImg2.setImageResource(R.drawable.evaluate_red);
            goutongImg3.setImageResource(R.drawable.evaluate_red);
            goutongImg4.setImageResource(R.drawable.evaluate_red);
            goutongImg5.setImageResource(R.drawable.evaluate_red_half);
		}else if (goutongevaluate > 4.5 & goutongevaluate <= 5.0) {
			goutongImg1.setImageResource(R.drawable.evaluate_red); 
            goutongImg2.setImageResource(R.drawable.evaluate_red);
            goutongImg3.setImageResource(R.drawable.evaluate_red);
            goutongImg4.setImageResource(R.drawable.evaluate_red);
            goutongImg5.setImageResource(R.drawable.evaluate_red);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getCustomerReviews(App.API_reviewlist,man.getId(),1,20);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.review:
			Intent intent = new Intent();
			intent.setClass(CustomerReviewsActivity.this, EvaluateActivity.class);
			intent.putExtra("id", man.getId());
			startActivity(intent);
			break;
		case R.id.btn_back:
		case R.id.mCancel: 
			finish();
		default:
			break;
		}
	}

	public void getCustomerReviews(String api, int id, int pageNumber, int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("id", id);
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CustomerReviewsActivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CustomerReviewsActivity.this, "网络异常，请检查你的网络是否连接后再试");
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
					ReviewResult reviewResult = JSON.parseObject(t.getResults().toString(), ReviewResult.class);
					if(reviewResult.getTotal() == 0)
						tip.setVisibility(View.VISIBLE);
					else{
						tip.setVisibility(View.GONE);
					
						adapter = new ReviewAdapter(CustomerReviewsActivity.this, reviewResult.getList());
						reviewListView.setAdapter(adapter);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CustomerReviewsActivity.this, t.getContent());
				}
			}
		});
	}
}
