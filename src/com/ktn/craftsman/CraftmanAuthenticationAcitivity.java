package com.ktn.craftsman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.adapter.CraftManAuthAdapter;
import com.ktn.craftsman.adapter.JobAdapter;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.ktn.craftsman.bean.JobCategoryResult;
import com.ktn.craftsman.bean.Joblist;
import com.ktn.craftsman.bean.NameAuthResult;
import com.ktn.craftsman.bean.UpLoadImage;
import com.ktn.craftsman.bean.WorkerAuthResult;
import com.ktn.craftsman.util.FileUtil;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothClass.Device.Major;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CraftmanAuthenticationAcitivity extends AbstractActivity implements OnClickListener{

	private Spinner jobSpinner;
	private Spinner jobSpinner2;
	private Spinner jobSpinner3;
//	private GridView gridView;
//	private CraftManAuthAdapter authAdapter;
//	private ArrayList<String> dataList = new ArrayList<String>();
	private String leftPath;
	private String rightPath;
	private String leftServerPath;
	private String rightServertPath;
	private ImageView leftimg;
	private ImageView rightimg;
	private ImageView statusimg;
	private String jobID;
	private TextView content;
	private ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> map2 = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> map3 = new ArrayList<HashMap<String,String>>();
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
		case R.id.mCancel: 
			finish();
			break;
		case R.id.leftimg: 
			Intent intent = new Intent(Intent.ACTION_PICK);  
	        intent.setType("image/*");//相片类型  
	        startActivityForResult(intent, 1);  
	        break;
		case R.id.rightimg: 
			Intent it = new Intent(Intent.ACTION_PICK);  
			it.setType("image/*");//相片类型  
	        startActivityForResult(it, 2);  
	        break;
		case R.id.commit:
			if(TextUtils.isEmpty(leftPath))
			{
				toast("请上传工匠认证资料");
				return;
			}
			if(TextUtils.isEmpty(rightPath))
			{
				toast("请上传工匠认证资料");
				return;
			}
			VDialog.getDialogInstance().showLoadingDialog(CraftmanAuthenticationAcitivity.this,
					"正在上传认证资料",true,true,0);
			uploadFile(App.API_upload, new File(leftPath), "image", 0);
			uploadFile(App.API_upload, new File(rightPath), "image", 1);
			break;
		default:
			break;
	}		
	}

	@Override
	protected int loadLayout() {
		return R.layout.craftman_authentication;
	}

	@Override
	protected void findView() {
		jobSpinner = (Spinner)findViewById(R.id.spinner);
		jobSpinner2 = (Spinner)findViewById(R.id.spinner2);
		jobSpinner3 = (Spinner)findViewById(R.id.spinner3);
		content = (TextView)findViewById(R.id.content);
//		gridView = (GridView)findViewById(R.id.pic_grid);
		leftimg = (ImageView)findViewById(R.id.leftimg);
		rightimg = (ImageView)findViewById(R.id.rightimg);
		statusimg = (ImageView)findViewById(R.id.status);
	}

	@Override
	protected void onCreate() {
		map.clear();
		if(App.jobCategoryList == null || App.jobCategoryList.size() == 0)
			return;
		for (JobCategory job : App.jobCategoryList) {
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("id", job.getId()+"");
			map1.put("name", job.getName());
			map.add(map1);
		}
		SimpleAdapter adapter = new SimpleAdapter(this,
				map, R.layout.simple_spinner_item,
                new String[] { "name" },
                new int[] { android.R.id.text1 });
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		jobSpinner.setAdapter(adapter);
		
		jobSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> maptemp = (HashMap<String, String>) jobSpinner.getSelectedItem();
				sendJobCategorylist(App.API_jobcategorylist,Integer.parseInt(maptemp.get("id")));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		jobSpinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> maptemp = (HashMap<String, String>) jobSpinner2.getSelectedItem();
				sendJoblist(App.API_joblist,Integer.parseInt(maptemp.get("id")),1,20);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);	
		(findViewById(R.id.commit)).setOnClickListener(this);
		(findViewById(R.id.leftimg)).setOnClickListener(this);
		(findViewById(R.id.rightimg)).setOnClickListener(this);
		
		jobSpinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> map1 = (HashMap<String, String>) jobSpinner.getSelectedItem();
                if (map1 != null) {
                	jobID = map1.get("id");
                }
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				if(position == parent.getAdapter().getCount()-1){
//					Intent intent = new Intent(Intent.ACTION_PICK);  
//			        intent.setType("image/*");//相片类型  
//			        startActivityForResult(intent, 1);  
//				}
//			}
//		});
		
//		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//				VDialog.getDialogInstance().showDeleteConfirmDialog(CraftmanAuthenticationAcitivity.this, new Handler(){
//					@Override
//					public void handleMessage(Message msg) {
//						switch (msg.what) {
//						case 1:
//							SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, false);
//							dataList.remove(position);
//							authAdapter.notifyDataSetChanged();
//							break;
//						default:
//							break;
//						}
//					}
//				});
//				return true;
//			}
//		});
	}

	@Override
	protected void loadData() {
		jobSpinner.setSelection(0);
		getAuth(App.API_getauth, "work");
	}

	@Override
	protected void onResume() {
	    super.onResume();
	}
	
	private boolean selectEventType(Spinner spn, String typeID) {
        if (typeID != null && !"".equals(typeID)) {
            HashMap<String, String> map = (HashMap<String, String>) spn
                    .getSelectedItem();
            if (map != null && map.get("id").equals(typeID)) {
                return true;
            } else if (spn.getAdapter() != null
                    && spn.getAdapter().getCount() > 0) {
                for (int i = 0; i < spn.getAdapter().getCount(); i++) {
                    map = (HashMap<String, String>) spn.getAdapter().getItem(i);
                    if (map.get("id").equals(typeID)) {
                        spn.setSelection(i);
                        return false;
                    }
                }
            }
        }
        return false;
    }
	
	@SuppressLint("NewApi")
	private void refreshUI(WorkerAuthResult result){
		if(result == null)
			return;
		selectEventType(jobSpinner,result.getJobId()+"");
		Picasso.with(this).load(result.getImageFront())
        .placeholder(R.drawable.img_loading)
        .error(R.drawable.pic_no_news)
        .into(leftimg);
		Picasso.with(this).load(result.getImageBack())
        .placeholder(R.drawable.img_loading)
        .error(R.drawable.pic_no_news)
        .into(rightimg);
		if(result.getStatus().equals("audit")){
			jobSpinner.setEnabled(false);
			jobSpinner2.setEnabled(false);
			jobSpinner3.setEnabled(false);
			jobSpinner.setVisibility(View.GONE);
			jobSpinner2.setVisibility(View.GONE);
			jobSpinner3.setVisibility(View.GONE);
			content.setText(result.getJobName());
			content.setVisibility(View.VISIBLE);
			leftimg.setEnabled(false);
			rightimg.setEnabled(false);
			(findViewById(R.id.commit)).setVisibility(View.GONE);
			statusimg.setVisibility(View.VISIBLE);
			statusimg.setBackground(getResources().getDrawable(R.drawable.status_audit));
		}else if(result.getStatus().equals("success")){
			jobSpinner.setEnabled(false);
			jobSpinner2.setEnabled(false);
			jobSpinner.setVisibility(View.GONE);
			jobSpinner2.setVisibility(View.GONE);
            jobSpinner3.setEnabled(false);
            jobSpinner3.setVisibility(View.GONE);
			content.setText(result.getJobName());
			content.setVisibility(View.VISIBLE);
			leftimg.setEnabled(false);
			rightimg.setEnabled(false);
			(findViewById(R.id.commit)).setVisibility(View.GONE);
			statusimg.setVisibility(View.VISIBLE);
			statusimg.setBackground(getResources().getDrawable(R.drawable.status_success));
		}else if(result.getStatus().equals("failure")){
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 String path = null;
		 path = getPicAddLocalPath(this,data);
		 if (requestCode == 1) {    
			 if(!TextUtils.isEmpty(path)){
				 leftPath = path;
				 App.getInstance().load(leftimg,path);
			 }
        
	     } else if (requestCode == 2 ) {             
	    	 if(!TextUtils.isEmpty(path)){
	    		 rightPath = path;
	    		 App.getInstance().load(rightimg,path);
			 }
	     }
	}
	
	/**
	 * 获取添加的图片本地位置
	 * 
	 * @param data
	 * @return
	 */
	public String getPicAddLocalPath(Activity activity, Intent data) {
		Uri uri = null;
		if (data != null) {
			uri = data.getData();
		}
		if (uri == null) {
			Toast.makeText(activity, "文件无法选择.", Toast.LENGTH_LONG).show();

			return null;
		}

		String strPath = FileUtil.uriToPath(activity, uri, data.getDataString());
		if (strPath == null) {
			Toast.makeText(activity, "文件无法选择.", Toast.LENGTH_LONG).show();

			return null;
		}

		return strPath;
	}
	
	public void sendCraftManAuthenty(String api, String jobId, String imageFront, String imageBack){
		
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("jobId", jobId);
		params.addFormDataPart("imageFront", imageFront);
		params.addFormDataPart("imageBack", imageBack);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, t.getContent());
					jobSpinner.setEnabled(false);
		            jobSpinner2.setEnabled(false);
		            jobSpinner3.setEnabled(false);
		            leftimg.setEnabled(false);
		            rightimg.setEnabled(false);
					finish();
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, t.getContent());
				}
			}
		});
		
	}
	
	public void uploadFile(String api, java.io.File file, String fileType, final int uploadtype){
		
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("fileType", fileType);
		params.addFormDataPart("file", file);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					UpLoadImage upLoadImage = JSON.parseObject(t.getResults().toString(), UpLoadImage.class);
					if(uploadtype == 0)
						leftServerPath = upLoadImage.getUrl();
					else if(uploadtype ==1)
						rightServertPath = upLoadImage.getUrl();
					
					if(!TextUtils.isEmpty(leftServerPath) && !TextUtils.isEmpty(rightServertPath)){
						sendCraftManAuthenty(App.API_workauth, jobID, leftServerPath, rightServertPath);
						leftServerPath = null;
						rightServertPath = null;
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, t.getContent());
				}
			}
		});
		
	}
	
	public void getAuth(String api, String type){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("type", type);
	    params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftmanAuthenticationAcitivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
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
				    if(TextUtils.isEmpty(t.getResults().toString()))
                        return;
					WorkerAuthResult result = JSON.parseObject(t.getResults().toString(), WorkerAuthResult.class);
					refreshUI(result);
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, t.getContent());
				}
			}
		});
	}
	
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
				 VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
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
						ArrayList<JobCategory> jobCategoryList = list.getList();
						if(jobCategoryList == null || jobCategoryList.size() == 0)
							return;
						map2.clear();
						for (JobCategory job : jobCategoryList) {
							HashMap<String, String> map22 = new HashMap<String, String>();
							map22.put("id", job.getId()+"");
							map22.put("name", job.getName());
							map2.add(map22);
						}
						SimpleAdapter adapter = new SimpleAdapter(CraftmanAuthenticationAcitivity.this,
								map2, R.layout.simple_spinner_item,
				                new String[] { "name" },
				                new int[] { android.R.id.text1 });
						adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
						jobSpinner2.setAdapter(adapter);
					}
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
				 VDialog.getDialogInstance().toast(CraftmanAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
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
						ArrayList<Job> jobList = list.getList();
						if(jobList == null || jobList.size() == 0)
							return;
						map3.clear();
						for (Job job : jobList) {
							HashMap<String, String> map33 = new HashMap<String, String>();
							map33.put("id", job.getId()+"");
							map33.put("name", job.getName());
							map3.add(map33);
						}
						SimpleAdapter adapter = new SimpleAdapter(CraftmanAuthenticationAcitivity.this,
								map3, R.layout.simple_spinner_item,
				                new String[] { "name" },
				                new int[] { android.R.id.text1 });
						adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
						jobSpinner3.setAdapter(adapter);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
}
