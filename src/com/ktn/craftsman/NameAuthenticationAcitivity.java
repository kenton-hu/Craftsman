package com.ktn.craftsman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.w3c.dom.Text;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.ADResult;
import com.ktn.craftsman.bean.CraftManDetail;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.NameAuthResult;
import com.ktn.craftsman.bean.UpLoadImage;
import com.ktn.craftsman.util.FileUtil;
import com.ktn.craftsman.util.VDialog;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class NameAuthenticationAcitivity extends AbstractActivity implements OnClickListener {

	private EditText name;
	private EditText idCode;
	private ImageView leftimg;
	private ImageView rightimg;
	private ImageView statusimg;
	private String leftPath;
	private String rightPath;
	private String leftServerPath;
	private String rightServertPath;
	private String nameStr;
	private String idCodeStr;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
			case R.id.mCancel: 
				finish();
				break;
			case R.id.commit:
				nameStr = name.getText().toString();
				if(TextUtils.isEmpty(nameStr) || name.length() == 1){
					toast("请输入真实姓名");
					return;
				}
				idCodeStr = idCode.getText().toString();
				if(TextUtils.isEmpty(nameStr)){
					toast("证件号不能为空");
					return;
				}
				if(!App.isIDNO(idCodeStr)){
					toast("请输入有效的身份证号");
					return;
				}
				if(TextUtils.isEmpty(leftPath))
				{
					toast("请上传身份证正面照片");
					return;
				}
				if(TextUtils.isEmpty(rightPath))
				{
					toast("请上传身份证反面照片");
					return;
				}
				VDialog.getDialogInstance().showLoadingDialog(NameAuthenticationAcitivity.this,
						"正在上传认证资料",true,true,0);
				uploadFile(App.API_upload, new File(leftPath), "image", 0);
				uploadFile(App.API_upload, new File(rightPath), "image", 1);
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
			default:
				break;
		}
	}

	@Override
	protected int loadLayout() {
		return R.layout.name_authentication;
	}

	@Override
	protected void findView() {
		name = (EditText)findViewById(R.id.nameinput);
		idCode = (EditText)findViewById(R.id.codeinput);
		leftimg = (ImageView)findViewById(R.id.leftimg);
		rightimg = (ImageView)findViewById(R.id.rightimg);
		statusimg = (ImageView)findViewById(R.id.status);
	}

	@Override
	protected void onCreate() {
	}

	@Override
	protected void setListener() {
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.commit)).setOnClickListener(this);
		(findViewById(R.id.leftimg)).setOnClickListener(this);
		(findViewById(R.id.rightimg)).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		getAuth(App.API_getauth, "id");
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	}

	@SuppressLint("NewApi")
	private void refreshUI(NameAuthResult result){
		if(result == null)
			return;
		name.setText(result.getIdName());
		idCode.setText(result.getIdNo());
		Picasso.with(this).load(result.getImageFront())
        .placeholder(R.drawable.img_loading)
        .error(R.drawable.pic_no_news)
        .into(leftimg);
		Picasso.with(this).load(result.getImageBack())
        .placeholder(R.drawable.img_loading)
        .error(R.drawable.pic_no_news)
        .into(rightimg);
		if(result.getStatus().equals("audit")){
			name.setEnabled(false);
			idCode.setEnabled(false);
			leftimg.setEnabled(false);
			rightimg.setEnabled(false);
			(findViewById(R.id.commit)).setVisibility(View.GONE);
			statusimg.setVisibility(View.VISIBLE);
			statusimg.setBackground(getResources().getDrawable(R.drawable.status_audit));
		}else if(result.getStatus().equals("success")){
			name.setEnabled(false);
			idCode.setEnabled(false);
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
	
	public void getAuth(String api, String type){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("type", type);
	    params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(NameAuthenticationAcitivity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
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
					NameAuthResult result = JSON.parseObject(t.getResults().toString(), NameAuthResult.class);
					refreshUI(result);
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, t.getContent());
				}
			}
		});
	}
	
	public void sendNameAuthenty(String api, String idName, String idNo, String imageFront, String imageBack){
		
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("idName", idName);
		params.addFormDataPart("idNo", idNo);
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
				VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, t.getContent());
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, t.getContent());
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
				VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
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
						sendNameAuthenty(App.API_nameauth, nameStr, idCodeStr.toUpperCase(), leftServerPath, rightServertPath);
						leftServerPath = null;
						rightServertPath = null;
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(NameAuthenticationAcitivity.this, t.getContent());
				}
			}
		});
		
	}
}
