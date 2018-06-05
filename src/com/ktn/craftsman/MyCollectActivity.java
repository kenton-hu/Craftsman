package com.ktn.craftsman;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.HistoryMemberAdapter;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.HistoryMember;
import com.ktn.craftsman.bean.HistoryMemberResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class MyCollectActivity extends AbstractActivity implements OnClickListener{

	private View back;
	private View cancel;
	private ListView listview;
	private HistoryMemberAdapter adapter;
	
	@Override
	protected int loadLayout() {
		return R.layout.historyfragment;
	}

	@Override
	protected void findView() {
		back = findViewById(R.id.btn_back);
		cancel = findViewById(R.id.mCancel);
		listview = (ListView)findViewById(R.id.listView);
	}

	@Override
	protected void onCreate() {
		((TextView)findViewById(R.id.tv_title)).setText("我的收藏");
	}

	@Override
	protected void setListener() {
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HistoryMember man = (HistoryMember)parent.getAdapter().getItem(position);
				CraftMan craftMan = new CraftMan(man);
				Intent it = new Intent();
				it.setClass(MyCollectActivity.this, CraftManDetailAcitvity.class);
				it.putExtra("CraftMan", JSON.toJSONString(craftMan));
				it.putExtra("type", man.getMemberJobName());
				startActivity(it);
			}
		});
	}

	@Override
	protected void loadData() {
		getMyCollectlist(App.API_favoritelist, 1,20);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}		
	}

	private void getMyCollectlist(String api, int pageNumber,int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(MyCollectActivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				 VDialog.getDialogInstance().toast(MyCollectActivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				if(t.getType().equals(App.SUCCESS)){
					HistoryMemberResult historyMemberResult = JSON.parseObject(t.getResults().toString(), HistoryMemberResult.class);
					adapter = new HistoryMemberAdapter(MyCollectActivity.this, historyMemberResult.getList());
					listview.setAdapter(adapter);
				}else if(!TextUtils.isEmpty(t.getContent())){
					 VDialog.getDialogInstance().toast(MyCollectActivity.this, t.getContent());
				}
			}
		});
	}
}
