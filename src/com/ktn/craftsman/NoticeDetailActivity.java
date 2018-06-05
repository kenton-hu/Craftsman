package com.ktn.craftsman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.NoticeDetail;
import com.ktn.craftsman.bean.NoticeResult;
import com.ktn.craftsman.util.VDialog;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class NoticeDetailActivity extends AbstractActivity implements OnClickListener{

	private TextView name;
	private TextView time;
	private TextView content;
	
	private String title;
	private int id;
	
	@Override
	protected int loadLayout() {
		return R.layout.noticedetail;
	}

	@Override
	protected void findView() {
		name = (TextView)findViewById(R.id.name);
		time = (TextView)findViewById(R.id.time);
		content = (TextView)findViewById(R.id.content);
	}

	@Override
	protected void onCreate() {
		title = getIntent().getStringExtra("Name");
		id = getIntent().getIntExtra("Id", -1);
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		getNoticeDetail(App.API_notifydetail,id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		}		
	}

	public void getNoticeDetail(String api, int id){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("id", id);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(NoticeDetailActivity.this,
						"数据加载中",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(NoticeDetailActivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				if(t.getType().equals(App.SUCCESS)){
					NoticeDetail detail = JSON.parseObject(t.getResults().toString(), NoticeDetail.class);
					if(detail != null ){
						name.setText(detail.getName());
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
						time.setText(df.format(new Date(detail.getCreateDate())));
						content.setText(Html.fromHtml(detail.getContent()));
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
}
