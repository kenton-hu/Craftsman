package com.ktn.craftsman;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.NoticeAdapter;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Notice;
import com.ktn.craftsman.bean.NoticeResult;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class NoticeAcitivity extends AbstractActivity implements OnClickListener{

	private ListView listview;
	private ArrayList<Notice> notices;
	private NoticeAdapter adapter;
	
	@Override
	protected int loadLayout() {
		return R.layout.notice;
	}

	@Override
	protected void findView() {
		listview = (ListView)findViewById(R.id.listView);

	}

	@Override
	protected void onCreate() {
		String str = getIntent().getStringExtra("NoticeList");
		if(str != null){
			notices = (ArrayList<Notice>) JSON.parseArray(str, Notice.class);
			adapter = new NoticeAdapter(this, notices);
			listview.setAdapter(adapter);
		}
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(notices == null || notices.size() == 0)
					return;
				Notice item = notices.get(position);
				Intent intent = new Intent();
				intent.putExtra("Name", item.getName());
				intent.putExtra("Id", item.getId());
				intent.setClass(NoticeAcitivity.this, NoticeDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void loadData() {
		getNoticeList(App.API_notifylist,1,20);
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

	public void getNoticeList(String api, int pageNumber, int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					NoticeResult result = JSON.parseObject(t.getResults().toString(), NoticeResult.class);
					if(result != null && result.getList().size() != 0 ){
						notices = result.getList();
						if(adapter != null)
							adapter.notifyDataSetChanged();
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
}
