package com.ktn.craftsman;

import java.io.File;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ktn.craftsman.adapter.CraftManAdapter;
import com.ktn.craftsman.adapter.CraftmanDetailAdapter;
import com.ktn.craftsman.bean.AD;
import com.ktn.craftsman.bean.ADResult;
import com.ktn.craftsman.bean.CraftMan;
import com.ktn.craftsman.bean.CraftManDetail;
import com.ktn.craftsman.bean.CraftManResult;
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
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class CraftManDetailAcitvity extends AbstractActivity implements OnClickListener{

	private CraftMan man;
	private RoundImageView head;
	private TextView title;
	private TextView name;
	private TextView time;
	private TextView type;
	private TextView consulation;
	private TextView liulan;
	private TextView distance;
	private TextView typeName;
	private TextView idCode;
	private TextView yanzheng;
	private TextView count;
	
	private TextView address;
	private TextView introduce;
	
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
	
	private ImageView zongheImg1;
	private ImageView zongheImg2;
	private ImageView zongheImg3;
	private ImageView zongheImg4;
	private ImageView zongheImg5;
	
	private GridView gridView;
	private CraftmanDetailAdapter adapter;
	
	private CraftManDetail craftManDetail;
	private String jobName;
	
	@Override
	protected int loadLayout() {
		return R.layout.craftmandetail;
	}

	@Override
	protected void findView() {
		title = (TextView)findViewById(R.id.tv_title);
		head = (RoundImageView)findViewById(R.id.head);
		name = (TextView)findViewById(R.id.name);
		time = (TextView)findViewById(R.id.time);
		type = (TextView)findViewById(R.id.type);
		consulation = (TextView)findViewById(R.id.consulation);
		liulan = (TextView)findViewById(R.id.liulan);
		distance = (TextView)findViewById(R.id.distance);
		typeName = (TextView)findViewById(R.id.typename);
		idCode = (TextView)findViewById(R.id.id);
		yanzheng = (TextView)findViewById(R.id.yanzheng);
		count = (TextView)findViewById(R.id.count);
		
		address = (TextView)findViewById(R.id.address);
		introduce = (TextView)findViewById(R.id.introduce);
		gridView = (GridView)findViewById(R.id.detail_grid);
		
		zhuanyeImg1 = (ImageView)findViewById(R.id.zhuanyeevaluate1);
		zhuanyeImg2 = (ImageView)findViewById(R.id.zhuanyeevaluate2);
		zhuanyeImg3 = (ImageView)findViewById(R.id.zhuanyeevaluate3);
		zhuanyeImg4 = (ImageView)findViewById(R.id.zhuanyeevaluate4);
		zhuanyeImg5 = (ImageView)findViewById(R.id.zhuanyeevaluate5);
		
		goutongImg1 = (ImageView)findViewById(R.id.goutongevaluate1);
		goutongImg2 = (ImageView)findViewById(R.id.goutongevaluate2);
		goutongImg3 = (ImageView)findViewById(R.id.goutongevaluate3);
		goutongImg4 = (ImageView)findViewById(R.id.goutongevaluate4);
		goutongImg5 = (ImageView)findViewById(R.id.goutongevaluate5);
		
		zongheImg1 = (ImageView)findViewById(R.id.zongheevaluate1);
		zongheImg2 = (ImageView)findViewById(R.id.zongheevaluate2);
		zongheImg3 = (ImageView)findViewById(R.id.zongheevaluate3);
		zongheImg4 = (ImageView)findViewById(R.id.zongheevaluate4);
		zongheImg5 = (ImageView)findViewById(R.id.zongheevaluate5);
		
	}

	@Override
	protected void onCreate() {
		String json = getIntent().getStringExtra("CraftMan");
		jobName = getIntent().getStringExtra("type");
		if(!TextUtils.isEmpty(json)){
			man = JSON.parseObject(json, CraftMan.class); 
		}
		refreshUI();
	}

	private void refreshUI(){
		Picasso.with(this).load(man.getAvatar())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(head);
		name.setText(man.getName());
		title.setText(man.getName());
		time.setText(App.Date2String(man.getCreateDate()));
		if(!TextUtils.isEmpty(jobName))
			type.setText(jobName.substring(0, 1));;
		consulation.setText(man.getConsulationCount()+"次");
		liulan.setText("已有"+man.getViews()+"人浏览");
		double distanceNum = App.getDistance(App.latitude, App.longitude,
        		Double.parseDouble(man.getLat()), Double.parseDouble(man.getLng()));
        if(distanceNum < 1000)
        	distance.setText(((int)distanceNum)+"米");
        else
        	distance.setText(((int)distanceNum/1000)+"公里");
		typeName.setText(jobName);
	}
	@Override
	protected void setListener() {
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.collect)).setOnClickListener(this);
		(findViewById(R.id.share)).setOnClickListener(this);
		(findViewById(R.id.evaluatelyt)).setOnClickListener(this);
		(findViewById(R.id.addresslyt)).setOnClickListener(this);
		(findViewById(R.id.introducelyt)).setOnClickListener(this);
		(findViewById(R.id.add)).setOnClickListener(this);
		(findViewById(R.id.call)).setOnClickListener(this);
		
	}

	@Override
	protected void loadData() {
		if(!TextUtils.isEmpty(App.user.getPhone()) && App.user.getPhone().equals(man.getPhone())){
			(findViewById(R.id.add)).setVisibility(View.VISIBLE);
		}
		getMemberDetail(App.API_memberDetail,man.getId());
		GeoCoder coder = GeoCoder.newInstance();
		coder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.parseDouble(man.getLat()), Double.parseDouble(man.getLng()))));
		coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                if(!TextUtils.isEmpty(arg0.getAddress())){
                    address.setText(arg0.getAddress());
                }
            }
            
            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_back:
		case R.id.mCancel:
			finish();	
			break;
		case R.id.collect:
			addCraftManCollect(App.API_favorite, man.getId());
			break;
		case R.id.share:
			Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_TEXT, "我在"+ getResources().getString(R.string.app_name) +"发现"+ man.getName() +"工匠手艺不错，强烈分享给大家！http://fusion.qq.com/cgi-bin/qzapps/unified_jump?appid=42386151&isTimeline=true&actionFlag=0&params=pname%3Dcom.ktn.craftsman%26versioncode%3D14%26channelid%3D%26actionflag%3D0&from=timeline&isappinstalled=1");
	        shareIntent.setType("text/plain");
	 
	        //设置分享列表的标题，并且每次都显示分享列表
	        startActivity(Intent.createChooser(shareIntent, "分享到"));
			break;
		case R.id.evaluatelyt:
			intent.setClass(CraftManDetailAcitvity.this, CustomerReviewsActivity.class);
			intent.putExtra("man", JSON.toJSONString(man));
			intent.putExtra("mandetail", JSON.toJSONString(craftManDetail));
			startActivity(intent);
			break;
		case R.id.addresslyt:
			
			break;
		case R.id.introducelyt:
			
			break;
		case R.id.add:
			Intent it = new Intent(Intent.ACTION_PICK);  
			it.setType("image/*");//相片类型  
	        startActivityForResult(it, 13);  
			break;
		case R.id.call:
			if(man == null)
				return;
			if(!App.isLogin){
                Intent itJump = new Intent();
                itJump.setClass(CraftManDetailAcitvity.this, LoginActivity.class);
                CraftManDetailAcitvity.this.startActivity(itJump);
                return;
            }
			sendHistorySave(App.API_historysave,man.getId());
			
			break;
		default:
			break;
		}
	}

	public void sendHistorySave(String api, int id){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("id", id);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
            @Override
            protected void onSuccess(HttpResult t) {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                if(t.getType().equals(App.SUCCESS)){
                    Intent it = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+man.getPhone()));  
                    CraftManDetailAcitvity.this.startActivity(it); 
                }else if(!TextUtils.isEmpty(t.getContent())){
                    VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
                }
                
            }
        });
		
	}
	
	private void loadCraftManDetail(CraftManDetail craftManDetail){
		if(craftManDetail == null)
			return;
		if(!TextUtils.isEmpty(craftManDetail.getIdNo())){
			String idNum = craftManDetail.getIdNo().substring(0, 4)+"*******"+craftManDetail.getIdNo().substring(craftManDetail.getIdNo().length()-4);
			idCode.setText(idNum);
			if(craftManDetail.isAuthId())
				yanzheng.setText("(已验证)");
			else
				yanzheng.setText("(未验证)");
		}else {
			idCode.setVisibility(View.INVISIBLE);
			yanzheng.setVisibility(View.INVISIBLE);
		}
		count.setText(craftManDetail.getReviewCount()+"条");
		introduce.setText(craftManDetail.getIntroduction());
		adapter = new CraftmanDetailAdapter(CraftManDetailAcitvity.this, craftManDetail.getCases());
		gridView.setAdapter(adapter);
		
		float zhuanyeevaluate = craftManDetail.getRateProfessional();
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
        
        float goutongevaluate = craftManDetail.getRateCommunicate();
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
        
        float zongheevaluate = craftManDetail.getRateGeneral();
        if(zongheevaluate == 0.0){
        	zongheImg1.setImageResource(R.drawable.evaluate_gre);
        	zongheImg2.setImageResource(R.drawable.evaluate_gre);
        	zongheImg3.setImageResource(R.drawable.evaluate_gre);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
        }else if (zongheevaluate > 0.0 & zongheevaluate <= 0.5) {
        	zongheImg1.setImageResource(R.drawable.evaluate_red_half);
        	zongheImg2.setImageResource(R.drawable.evaluate_gre);
        	zongheImg3.setImageResource(R.drawable.evaluate_gre);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 0.5 & zongheevaluate <= 1.0) {
        	zongheImg1.setImageResource(R.drawable.evaluate_red);
        	zongheImg2.setImageResource(R.drawable.evaluate_gre);
        	zongheImg3.setImageResource(R.drawable.evaluate_gre);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 1.0 & zongheevaluate <= 1.5) {
			zongheImg1.setImageResource(R.drawable.evaluate_red);
            zongheImg2.setImageResource(R.drawable.evaluate_red_half);
        	zongheImg3.setImageResource(R.drawable.evaluate_gre);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 1.5 & zongheevaluate <= 2.0) {
			zongheImg1.setImageResource(R.drawable.evaluate_red); 
            zongheImg2.setImageResource(R.drawable.evaluate_red); 
        	zongheImg3.setImageResource(R.drawable.evaluate_gre);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 2.0 & zongheevaluate <= 2.5) {
			zongheImg1.setImageResource(R.drawable.evaluate_red);  
            zongheImg2.setImageResource(R.drawable.evaluate_red); 
            zongheImg3.setImageResource(R.drawable.evaluate_red_half);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 2.5 & zongheevaluate <= 3.0) {
			zongheImg1.setImageResource(R.drawable.evaluate_red); 
            zongheImg2.setImageResource(R.drawable.evaluate_red);
            zongheImg3.setImageResource(R.drawable.evaluate_red);
        	zongheImg4.setImageResource(R.drawable.evaluate_gre);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 3.0 & zongheevaluate <= 3.5) {
			zongheImg1.setImageResource(R.drawable.evaluate_red); 
            zongheImg2.setImageResource(R.drawable.evaluate_red);
            zongheImg3.setImageResource(R.drawable.evaluate_red);
            zongheImg4.setImageResource(R.drawable.evaluate_red_half);
        	zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 3.5 & zongheevaluate <= 4.0) {
			zongheImg1.setImageResource(R.drawable.evaluate_red); 
            zongheImg2.setImageResource(R.drawable.evaluate_red);
            zongheImg3.setImageResource(R.drawable.evaluate_red);
            zongheImg4.setImageResource(R.drawable.evaluate_red);
            zongheImg5.setImageResource(R.drawable.evaluate_gre);
		}else if (zongheevaluate > 4.0 & zongheevaluate <= 4.5) {
			zongheImg1.setImageResource(R.drawable.evaluate_red); 
            zongheImg2.setImageResource(R.drawable.evaluate_red);
            zongheImg3.setImageResource(R.drawable.evaluate_red);
            zongheImg4.setImageResource(R.drawable.evaluate_red);
            zongheImg5.setImageResource(R.drawable.evaluate_red_half);
		}else if (zongheevaluate > 4.5 & zongheevaluate <= 5.0) {
			zongheImg1.setImageResource(R.drawable.evaluate_red); 
            zongheImg2.setImageResource(R.drawable.evaluate_red);
            zongheImg3.setImageResource(R.drawable.evaluate_red);
            zongheImg4.setImageResource(R.drawable.evaluate_red);
            zongheImg5.setImageResource(R.drawable.evaluate_red);
		}
		
	}
	
	public void getMemberDetail(String api, int id){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("id", id);
		if(App.user.getToken() != null)
		    params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftManDetailAcitvity.this,
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "网络异常，请检查你的网络是否连接后再试");
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
					craftManDetail = JSON.parseObject(t.getResults().toString(), CraftManDetail.class);
					loadCraftManDetail(craftManDetail);
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
				}
			}
		});
	}
	
	public void addCraftManCollect(String api, int id){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("id", id);
		params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftManDetailAcitvity.this,
						"正在收藏该工匠",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "网络异常，请检查你的网络是否连接后再试");
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
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "收藏成功");
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(data == null){
	        return;
	    }
		 ContentResolver resolver = getContentResolver();
		 String path = null;
		 Uri originalUri = data.getData();
		 String[] proj = {MediaStore.Images.Media.DATA};
		 Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
		 int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		 cursor.moveToFirst();
		 path = cursor.getString(column_index);
		 if (requestCode == 13) {    
			 if(!TextUtils.isEmpty(path)){
				 File file = new File(path);
				 if(file.exists() && file.isFile()){
					 uploadFile(App.API_upload, file, "image");
				 }
			 }
        
	     } 
	}
	
public void sendCraftCase(String api, String image, String name){
		
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("image", image);
		params.addFormDataPart("name", name);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				VDialog.getDialogInstance().closeLoadingDialog();
				
				if(t.getType().equals(App.SUCCESS)){
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
					getMemberDetail(App.API_memberDetail,man.getId());
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
				}
			}
		});
		
	}
	
	public void uploadFile(String api, final java.io.File file, String fileType){
		
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("fileType", fileType);
		params.addFormDataPart("file", file);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftManDetailAcitvity.this,
						"正在上传案例",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					UpLoadImage upLoadImage = JSON.parseObject(t.getResults().toString(), UpLoadImage.class);
					
					sendCraftCase(App.API_addcase, upLoadImage.getUrl(), file.getName());
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
				}
			}
		});
		
	}
	
	public void addCraftManCase(String api, int id){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("id", id);
	    params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(CraftManDetailAcitvity.this,
						"正在收藏该工匠",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "网络异常，请检查你的网络是否连接后再试");
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
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, "收藏成功");
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(CraftManDetailAcitvity.this, t.getContent());
				}
			}
		});
	}
}
