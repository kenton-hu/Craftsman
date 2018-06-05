package com.ktn.craftsman;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.JobAdapter;
import com.ktn.craftsman.adapter.JobCategoryAdapter;
import com.ktn.craftsman.bean.CraftManResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.ktn.craftsman.bean.JobCategoryResult;
import com.ktn.craftsman.bean.Joblist;
import com.ktn.craftsman.util.DateUtil;
import com.ktn.craftsman.util.VDialog;

import android.annotation.SuppressLint;
import android.app.SearchableInfo;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.hardware.Camera.Size;
import android.media.Image;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class JobCategoryAcitivity extends AbstractActivity implements OnClickListener{

	private View back;
	private View cancel;
	private int jobCategoryID;
	private String jobCategoryName;
	
	private TextView lastJob;
	private TextView curJob;
	private TextView nextJob;
	private ImageView fenlei;
	
	private TextView title;
	private GridView grid2;
	private JobAdapter adapter2;
	private boolean search = true;
	private String keywords;
	private ArrayList<JobCategory> jobCategoryList;
	private int curIndex = 0;
	private int dataListSize = 0;
	private ScaleAnimation ItemDownAnim;
	private ScaleAnimation ItemUpAnim;
	private boolean isTouchItem = false;
	private int lastItem = 0;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.lastjob:
		case R.id.leftimg:
		    if(DateUtil.equalsTime())
		        return;
			if(jobCategoryList == null || jobCategoryList.size() == 0 || jobCategoryList.size() == 1)
				return;
			dataListSize = jobCategoryList.size();
			if(dataListSize == 2){
				curJob.setText(jobCategoryList.get((curIndex-1+dataListSize)%dataListSize).getName());
				nextJob.setText(jobCategoryList.get((curIndex-2+dataListSize)%dataListSize).getName());
				curIndex--;
				if(curIndex < 0)
					curIndex = curIndex + dataListSize;
			}else {
				curJob.setText(jobCategoryList.get((curIndex+1)%dataListSize).getName());
				nextJob.setText(jobCategoryList.get((curIndex-1+dataListSize)%dataListSize).getName());
				lastJob.setText(jobCategoryList.get((curIndex+dataListSize)%dataListSize).getName());
				curIndex++;
				if(curIndex >= dataListSize)
					curIndex = curIndex - dataListSize;
			}
			App.JOBCATEGORYINDEX = curIndex;
			sendJoblist(App.API_joblist,jobCategoryList.get(curIndex).getId(),1,20);
			break;
		case R.id.nextjob:
		case R.id.rightimg:
		    if(DateUtil.equalsTime())
                return;
			if(jobCategoryList == null || jobCategoryList.size() == 0 || jobCategoryList.size() == 1)
				return;
			dataListSize = jobCategoryList.size();
			if(dataListSize == 2){
				curJob.setText(jobCategoryList.get((curIndex+1+dataListSize)%dataListSize).getName());
				nextJob.setText(jobCategoryList.get((curIndex+2+dataListSize)%dataListSize).getName());
				curIndex++;
				if(curIndex >= dataListSize)
					curIndex = curIndex - dataListSize;
			}else {
				curJob.setText(jobCategoryList.get((curIndex-1+dataListSize)%dataListSize).getName());
				nextJob.setText(jobCategoryList.get((curIndex+dataListSize)%dataListSize).getName());
				lastJob.setText(jobCategoryList.get((curIndex+1)%dataListSize).getName());
				curIndex--;
				if(curIndex < 0)
					curIndex = curIndex + dataListSize;
				
			}
			App.JOBCATEGORYINDEX = curIndex;
			sendJoblist(App.API_joblist,jobCategoryList.get(curIndex).getId(),1,20);
			break;
		default:
			break;
		}
	}
	
	@SuppressLint("NewApi")
	private void sendJobCategorylist(String api,int parentId){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("parentId", parentId);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				 VDialog.getDialogInstance().toast(JobCategoryAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					JobCategoryResult list = JSON.parseObject(t.getResults().toString(), JobCategoryResult.class);
					if(list != null && list.getList() != null){
						App.jobCategoryList = new ArrayList<JobCategory>(list.getList());
						jobCategoryList = list.getList();
						int dataListSizeTemp = jobCategoryList.size();
						if(jobCategoryList == null || jobCategoryList.size() == 0)
							return;
						if(jobCategoryList.size() == 1)
							curJob.setText(jobCategoryList.get(0).getName());
						else if(jobCategoryList.size() == 2){
							curJob.setText(jobCategoryList.get(App.JOBCATEGORYINDEX).getName());
							nextJob.setText(jobCategoryList.get((App.JOBCATEGORYINDEX+1)%dataListSizeTemp).getName());
						}else {
							curJob.setText(jobCategoryList.get(App.JOBCATEGORYINDEX).getName());
							nextJob.setText(jobCategoryList.get((App.JOBCATEGORYINDEX+1)%dataListSizeTemp).getName());
							lastJob.setText(jobCategoryList.get(jobCategoryList.size()-1).getName());
						}
//						adapter1 =  new JobCategoryAdapter (JobCategoryAcitivity.this, list.getList());
//						grid1.setAdapter(adapter1);
//						for (int i = 0; i < grid1.getChildCount(); i++) {
//							if(i==0){
//								grid1.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.bg_add));
//							}else {
//								grid1.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.input));
//							}
//						} 
						sendJoblist(App.API_joblist,list.getList().get(App.JOBCATEGORYINDEX).getId(),1,20);
					}
//					Joblist list = JSON.parseObject(t.getResults().toString(), Joblist.class);
//					if(list != null && list.getList() != null){
//						String json = JSON.toJSONString(list.getList());
//						adapter =  new JobList2Adapter(getActivity(), list.getList());
//						craftManList.setAdapter(adapter);
//					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
	
	private void sendJoblist(String api,int jobCategoryId,int pageNumber,int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("jobCategoryId", jobCategoryId);
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				 VDialog.getDialogInstance().toast(JobCategoryAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					Joblist list = JSON.parseObject(t.getResults().toString(), Joblist.class);
					if(list != null && list.getList() != null){
						adapter2 =  new JobAdapter (JobCategoryAcitivity.this, list.getList());
						grid2.setAdapter(adapter2);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}

	@Override
	protected int loadLayout() {
		return R.layout.jobcategory2;
	}

	@Override
	protected void findView() {
		back = findViewById(R.id.btn_back);
		cancel = findViewById(R.id.mCancel);
		title = (TextView)findViewById(R.id.tv_title);
		lastJob = (TextView)findViewById(R.id.lastjob);
		curJob = (TextView)findViewById(R.id.curjob);
		nextJob = (TextView)findViewById(R.id.nextjob);
		fenlei = (ImageView)findViewById(R.id.fenlei);
//		grid1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		grid2 = (GridView)findViewById(R.id.two_grid);
//		grid2.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
	}

	@Override
	protected void onCreate() {
		jobCategoryID = App.JOBCATEGORYID;
        jobCategoryName = App.JOBCATEGORYNAME;  
		search = getIntent().getBooleanExtra("search", false);
		keywords = getIntent().getStringExtra("keywords");
	}

	@SuppressLint("NewApi")
	@Override
	protected void setListener() {
		initItemAnim();
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
		findViewById(R.id.leftimg).setOnClickListener(this);
		lastJob.setOnClickListener(this);
		findViewById(R.id.rightimg).setOnClickListener(this);
		nextJob.setOnClickListener(this);
//		grid1.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				for (int i = 0; i < grid1.getChildCount(); i++) {
//					grid1.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.input));
//				} 
//				view.setBackground(getResources().getDrawable(R.drawable.bg_add));
//				JobCategory job = (JobCategory) parent.getAdapter().getItem(position);
//				sendJoblist(App.API_joblist,job.getId(),1,20);
//			}
//		});
		grid2.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(adapter2 == null){
                    return false;
                }
            	if(adapter2.getCurIndex() < 0){
            		return false;
            	}
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchItem = true ;
                    grid2.getChildAt(adapter2.getCurIndex()).startAnimation(ItemDownAnim);
                    break;
                case MotionEvent.ACTION_UP:
                   recoverItem();
                    break;
                default:
                    break;
                }
            return false;
            }
        });
		grid2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				for (int i = 0; i < grid2.getChildCount(); i++) {
//					grid2.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.input));
//				} 
//				view.setBackground(getResources().getDrawable(R.drawable.bg_add));
				Job job = (Job) parent.getAdapter().getItem(position);
				App.JOBID = job.getId();
				App.JOBNAME = job.getName();
				Intent intent = new Intent();
				intent.setClass(JobCategoryAcitivity.this, CraftsManListActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	private void recoverItem() {
	    isTouchItem = false ;
	    grid2.getChildAt(adapter2.getCurIndex()).startAnimation(ItemUpAnim);
	    if (lastItem >= 0 && lastItem != adapter2.getCurIndex() && grid2.getChildAt(lastItem) != null) {
	        grid2.getChildAt(lastItem).clearAnimation();
	    }
	    lastItem = adapter2.getCurIndex();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(search){
			title.setText("搜索结果");
			findViewById(R.id.twolyt).setVisibility(View.GONE);
			searchMemberList(App.API_jobsearch,keywords);
		}else{
			if(!TextUtils.isEmpty(jobCategoryName)){
				title.setText(jobCategoryName);
			}
			sendJobCategorylist(App.API_jobcategorylist,jobCategoryID);
		}
	}

	private void initItemAnim() {
	    //缩小动画
	  ItemDownAnim = new ScaleAnimation(1.0F, 0.80F, 1.0F, 0.80F, 1, 0.5F, 1, 0.5F);
	    ItemDownAnim.setDuration(200L);
	    ItemDownAnim.setFillAfter(true);

	    //放大动画
	  ItemUpAnim = new ScaleAnimation(0.80F, 1.0F, 0.80F, 1.0F, 1, 0.5F, 1, 0.5F);
	    ItemUpAnim.setDuration(100L);
	    ItemUpAnim.setFillAfter(true);
	}
	
	@Override
	protected void loadData() {
		
	}
	
	public void searchMemberList(String api, String keywords){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("keywords", keywords);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(JobCategoryAcitivity.this,
						"正在搜索",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(JobCategoryAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
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
					Joblist list = JSON.parseObject(t.getResults().toString(), Joblist.class);
					if(list != null && list.getList() != null){
						adapter2 =  new JobAdapter (JobCategoryAcitivity.this, list.getList());
						grid2.setAdapter(adapter2);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(JobCategoryAcitivity.this, t.getContent());
				}
			}
		});
	}
}
