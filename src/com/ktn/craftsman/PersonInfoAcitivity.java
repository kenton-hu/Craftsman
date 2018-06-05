package com.ktn.craftsman;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.UpLoadImage;
import com.ktn.craftsman.util.RoundImageView;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;
import com.squareup.picasso.Picasso;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class PersonInfoAcitivity extends AbstractActivity implements OnClickListener {

	private RoundImageView head;
	private TextView name;
	private TextView sex;
	private TextView intro;
	private View lyt_head;
	private View lyt_name;
	private View lyt_sex;
	private View lyt_pw;
	private View lyt_introduce;
	
	@Override
	public void onClick(View v) {
		Intent it = new Intent();  
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;
		case R.id.lyt_head:
			Intent intent = new Intent(Intent.ACTION_PICK);  
	        intent.setType("image/*");//相片类型  
	        startActivityForResult(intent, 1);  
			break;
		case R.id.lyt_name:
			it.setClass(PersonInfoAcitivity.this, InfoEditActivity.class);
	        startActivityForResult(it, 3);  
			break;
		case R.id.lyt_sex:
	        it.setClass(PersonInfoAcitivity.this, PersoninfoChooseActivity.class);
	        startActivityForResult(it, 2);  
			break;
		case R.id.lyt_pw:
			it.setClass(PersonInfoAcitivity.this, SetIdentifyingCodeActivity.class);
			it.putExtra(App.PHONE, App.user.getPhone());
			it.putExtra(App.JUMPFROM, 1);
	        startActivityForResult(it, 5);  
			break;
		case R.id.lyt_introduce:
			it.setClass(PersonInfoAcitivity.this, InfoEditActivity.class);
	        startActivityForResult(it, 4);  
			break;
		default:
			break;
		}
	}

	@Override
	protected int loadLayout() {
		return R.layout.personinfo;
	}

	@Override
	protected void findView() {
		head = (RoundImageView)findViewById(R.id.img_head);
		name = (TextView)findViewById(R.id.txt_name);
		sex = (TextView)findViewById(R.id.tv_sex);
		intro = (TextView)findViewById(R.id.tv_intro);
		lyt_head = findViewById(R.id.lyt_head);
		lyt_name = findViewById(R.id.lyt_name);
		lyt_sex = findViewById(R.id.lyt_sex);
		lyt_pw = findViewById(R.id.lyt_pw);
		lyt_introduce = findViewById(R.id.lyt_introduce);
	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		lyt_head.setOnClickListener(this);
		lyt_name.setOnClickListener(this);
		lyt_sex.setOnClickListener(this);
		lyt_pw.setOnClickListener(this);
		lyt_introduce.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		Picasso.with(this).load(App.baseinfo.getAvatar())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(head);
		name.setText(App.baseinfo.getName());
		String sexstr = App.baseinfo.getGender();
		if(!TextUtils.isEmpty(sexstr)){
			if(sexstr.equals("male"))
				sex.setText("男");
			else if(sexstr.equals("female"))
				sex.setText("女");
		}else
			sex.setText("未知");
		
		intro.setText(App.baseinfo.getIntroduction());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode == RESULT_CANCELED)
    		 return;
		 if (data == null || "".equals(data)) {
             return;
         } 
		if (requestCode == 1) {
			 ContentResolver resolver = getContentResolver();
			 String path = null;
			 Bitmap bm = null;
			 Uri originalUri = data.getData();
			 try {
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}     
			 String[] proj = {MediaStore.Images.Media.DATA};
			 Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
			 int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			 cursor.moveToFirst();
			 path = cursor.getString(column_index);
		     
			 if(!TextUtils.isEmpty(path) && bm != null){
				 App.resizePic(path, path , 320);
				 uploadFile(App.API_upload,new java.io.File(path),"image");
			 }
	     } else if(requestCode == 2){
	    	 String result = data.getStringExtra("choice");
	    	 sendModifyInfo(App.API_edit,"gender",result,null);
	     }else if(requestCode == 3){
	    	 String result = data.getStringExtra("choice");
	    	 sendModifyInfo(App.API_edit,"name",result,null);
	     }else if(requestCode == 4){
	    	 String result = data.getStringExtra("choice");
	    	 sendModifyInfo(App.API_edit,"introduction",result,null);
	     }
	     else if(requestCode == 5){
	    	 String result = data.getStringExtra("choice");
	    	 String codeNum = data.getStringExtra("verifycode");
	    	 sendModifyInfo(App.API_edit,"password",result,codeNum);
	     }
	}
	
	public void sendModifyInfo(String api, final String baseType, final String value,String verifycode){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("baseType", baseType);
		params.addFormDataPart("value", value);
		if(!TextUtils.isEmpty(verifycode))
			params.addFormDataPart("verifycode", verifycode);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(PersonInfoAcitivity.this,
						"正在修改个人信息",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				toast("网络异常，请检查你的网络是否连接后再试");
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
					if(baseType.equals("avatar")){
						
					}else if(baseType.equals("name")){
						name.setText(value);
						App.baseinfo.setName(value);
					}else if(baseType.equals("gender")){
						if(value.equals("male")){
							sex.setText("男");
						}else if(value.equals("female")){
							sex.setText("女");
						}else{
							sex.setText("未知");
						}
						App.baseinfo.setGender(value);
					}else if(baseType.equals("password")){
						SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PASSWORD, value);
					}else if(baseType.equals("introduction")){
						intro.setText(value);
						App.baseinfo.setIntroduction(value);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					toast(t.getContent());
				}
			}
		});
	}
	
	public void uploadFile(String api, java.io.File file, String fileType){
		
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("fileType", fileType);
		params.addFormDataPart("file", file);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(PersonInfoAcitivity.this,
						"正在上传图片",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(PersonInfoAcitivity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					VDialog.getDialogInstance().toast(PersonInfoAcitivity.this, "头像上传成功");
					UpLoadImage upLoadImage = JSON.parseObject(t.getResults().toString(), UpLoadImage.class);
					App.baseinfo.setAvatar(upLoadImage.getUrl());
					Picasso.with(PersonInfoAcitivity.this).load(App.baseinfo.getAvatar())
					.fit().centerCrop()
			        .placeholder(R.drawable.defalut)
			        .error(R.drawable.defalut)
			        .into(head);
					sendModifyInfo(App.API_edit,"avatar",upLoadImage.getUrl(),null);
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(PersonInfoAcitivity.this, t.getContent());
				}
			}
		});
		
	}
}
