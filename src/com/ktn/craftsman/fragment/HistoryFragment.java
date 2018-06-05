package com.ktn.craftsman.fragment;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.App;
import com.ktn.craftsman.CraftManDetailAcitvity;
import com.ktn.craftsman.LoginActivity;
import com.ktn.craftsman.MainActivity;
import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.CraftManAdapter;
import com.ktn.craftsman.adapter.HistoryMemberAdapter;
import com.ktn.craftsman.adapter.JobListAdapter;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.CraftManResult;
import com.ktn.craftsman.bean.HistoryMember;
import com.ktn.craftsman.bean.HistoryMemberResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.Joblist;
import com.ktn.craftsman.util.VDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class HistoryFragment extends BaseFragment implements OnClickListener {
	private View back;
	private View cancel;
	private ListView listview;
	private HistoryMemberAdapter adapter;
	
	public int getFragmentType() { 	
        return BaseFragment.FragmentType_historyFragment;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.historyfragment, container, false);
        //查找控件
        findView(rootView);
        //设置监听
        setListener();
        //获取数据
       // getDb();
        
        return rootView;
    }
	
	private void findView(View view){
		back = view.findViewById(R.id.btn_back);
		cancel = view.findViewById(R.id.mCancel);
		listview = (ListView)view.findViewById(R.id.listView);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		sendHistorylist(App.API_historylist, 1,20);
	}
	
//	@Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//        	sendHistorylist(App.API_historylist, 1,20);
//        } else {
//            //相当于Fragment的onPause
//        }
//    }
	
	private void setListener(){
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HistoryMember man = (HistoryMember)parent.getAdapter().getItem(position);
				CraftMan craftMan = new CraftMan(man);
				Intent it = new Intent();
				it.setClass(getActivity(), CraftManDetailAcitvity.class);
				it.putExtra("CraftMan", JSON.toJSONString(craftMan));
				it.putExtra("type", man.getMemberJobName());
				startActivity(it);
			}
		});
	}
	
	private void sendHistorylist(String api, int pageNumber,int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(getActivity(),
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				 VDialog.getDialogInstance().toast(getActivity(), "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				if(t.getType().equals(App.SUCCESS)){
					HistoryMemberResult historyMemberResult = JSON.parseObject(t.getResults().toString(), HistoryMemberResult.class);
					adapter = new HistoryMemberAdapter(getActivity(), historyMemberResult.getList());
					listview.setAdapter(adapter);
				}else if(!TextUtils.isEmpty(t.getContent())){
					 VDialog.getDialogInstance().toast(getActivity(), t.getContent());
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.updateFragement(MainActivity.HOME_ITEM_INDEX);
			break;
		default:
			break;
		}
	}
}
