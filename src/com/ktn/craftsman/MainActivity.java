package com.ktn.craftsman;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.SDKInitializer;
import com.ktn.craftsman.BaseActivity.TimeCount;
import com.ktn.craftsman.adapter.JobList2Adapter;
import com.ktn.craftsman.adapter.JobListAdapter;
import com.ktn.craftsman.bean.AD;
import com.ktn.craftsman.bean.ADResult;
import com.ktn.craftsman.bean.Baseinfo;
import com.ktn.craftsman.bean.Case;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.ktn.craftsman.bean.JobCategoryResult;
import com.ktn.craftsman.bean.Joblist;
import com.ktn.craftsman.bean.LoginResult;
import com.ktn.craftsman.fragment.BaseFragment;
import com.ktn.craftsman.fragment.HistoryFragment;
import com.ktn.craftsman.fragment.HomeFragment;
import com.ktn.craftsman.fragment.SettingsFragment;
import com.ktn.craftsman.service.ICallback;
import com.ktn.craftsman.service.IService;
import com.ktn.craftsman.service.LocateService;
import com.ktn.craftsman.util.AppVersionUpdate;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.renderscript.Sampler.Value;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class MainActivity extends BaseActivity implements OnClickListener {

	public static final int HOME_ITEM_INDEX = 0;// 首页
    public static final int HISTORY_ITEM_INDEX = 1; // 历史
    public static final int SETTING_ITEM_INDEX = 2;// 设置
    public static final int CRAFTMAN_ITEM_INDEX = 3;// 工匠列表
    public static final int CATEGORY_ITEM_INDEX = 4;// 工种列表
    
    private static final String LOCATE_SERVICE = "com.ktn.craftsman.service.LocateService";
    
    private BaseFragment mCurrentFragement;
    
    private RadioButton homeRadio, historytRadio, settingsRadio;
    
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private HomeFragment homeFragement;
    private HistoryFragment historyFragment;
    private SettingsFragment settingsFragment;
    private CraftsManListActivity craftmanFragment;
    private JobCategoryAcitivity jobCategoryFragment;
    
    private boolean autoLogin = false;
    private TimeCount time;
    private View adView;
    private ImageView adImg;
    private TextView tip;
    private Target target;
	private boolean hasSend = false;
    
    public int mCurrentFragmentIndex = -1;// 当前的fragment页index
    private boolean jumpFromLogin = false;
    
    private static IService locateService;// 基础数据服务
	private ServiceConnection mLocateConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			locateService = null;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			locateService = IService.Stub.asInterface(service);  
            try {  
            	locateService.registerCallback(mCallback);  
            } catch (RemoteException e) {  
            }  	
		}
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		updateFragement(HOME_ITEM_INDEX);
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 SDKInitializer.initialize(getApplication());        
		//显示主界面
        setContentView(R.layout.main_layout);
        
        initView();
        
        initLocateService();
        
        App.getInstance().setMainActivity(this);
        
        AppVersionUpdate.getInstance().checkAppVersion();
        
        getFirstADList(App.API_adlist,"open");
        
//        sendJoblist(App.API_joblist);
        
        time = new TimeCount(2000, 1000){
			@Override
			public void onFinish() {
				tip.setText("0s");
				AnimationSet set = new AnimationSet(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
				alphaAnimation.setDuration(1000);
				alphaAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						adView.setVisibility(View.GONE);
					}
				});
				set.addAnimation(alphaAnimation);
				adView.startAnimation(set);
			}
			@Override
			public void onTick(long millisUntilFinished) {
				tip.setText(millisUntilFinished / 1000 + "s");
			}
		};
		
		final File dcimFile = new File(App.firstADimg);
		
		//Target
        target = new Target(){

			@Override
			public void onBitmapFailed(Drawable arg0) {
				
			}

			@Override
			public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                File parent = dcimFile.getParentFile();
                if(!parent.exists())
                	parent.mkdirs();
                
                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(dcimFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}

			@Override
			public void onPrepareLoad(Drawable arg0) {
				
			}
        };
        
        if(dcimFile.exists() && dcimFile.isFile()){
        	adView.setVisibility(View.VISIBLE);
        	App.getInstance().load(adImg,dcimFile.getAbsolutePath(),true);
        	time.start();
        }else {
        	adView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
        jumpFromLogin = getIntent().getBooleanExtra("login", false);
        
        String phone = SharedPrefTool.getValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PHONE, "");
        String password = SharedPrefTool.getValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PASSWORD, "");
        autoLogin = SharedPrefTool.getValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, false);
        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)){
        	SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, false);
        }else{
        	App.user.setPassword(password);
        	App.user.setPhone(phone);
        	if(!jumpFromLogin){
	        	getToken();
	        }
        }
	}
	
	private void initLocateService(){
		//一进入就启动定位服务
		Intent service = new Intent();
		service.setClass(this, LocateService.class);
		startService(service);
		bindService(service,mLocateConn, Context.BIND_AUTO_CREATE);	
	}
	
	private void initView() {
        
		adView = findViewById(R.id.adlayout);
		adImg = (ImageView)findViewById(R.id.adimg);
		tip = (TextView)findViewById(R.id.tip);
		
		homeRadio = (RadioButton) findViewById(R.id.menu_home);
		historytRadio = (RadioButton) findViewById(R.id.menu_history);
        settingsRadio = (RadioButton) findViewById(R.id.menu_settings);

        homeRadio.setOnClickListener(this);
        historytRadio.setOnClickListener(this);
        settingsRadio.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        
        updateFragement(mCurrentFragmentIndex);
    }
	
	public void updateFragement(int index) {
        if (index == -1) {
            index = HOME_ITEM_INDEX;
        }
        
        if(!App.isLogin && index != HOME_ITEM_INDEX && index != CRAFTMAN_ITEM_INDEX && index != CATEGORY_ITEM_INDEX){
        	Intent it = new Intent();
        	it.setClass(this, LoginActivity.class);
        	startActivity(it);
        }
        
        if (index == mCurrentFragmentIndex) {
            return ;
        }
        // 切换fragment后，要改变tab页的颜色效果
        changColor(index);

        transaction = fragmentManager.beginTransaction();
        // 隐藏上一个页面
        if (mCurrentFragement != null) {
            transaction.hide(mCurrentFragement);
        }

        BaseFragment fragment = null;
        switch (index) {
            case HOME_ITEM_INDEX: {
                fragment = homeItemIndex();
                break;
            }
            case HISTORY_ITEM_INDEX: {
                fragment = historyItemIndex();
                break;
            }
            case SETTING_ITEM_INDEX: {
                fragment = settingsItemIndex();
                break;
            }
//            case CRAFTMAN_ITEM_INDEX: {
//                fragment = craftmanItemIndex();
//                break;
//            }
//            case CATEGORY_ITEM_INDEX:{
//            	fragment = jobCategoryItemIndex();
//                break;	
//            }
        }
        
        if(fragment == null) {
            return;
        }
        
        transaction.commitAllowingStateLoss();
        mCurrentFragement = fragment;
        mCurrentFragmentIndex = index;
        mCurrentFragement.onShow();
    }
	
    private BaseFragment homeItemIndex() {
        if (homeFragement == null) {
            Fragment f = fragmentManager.findFragmentByTag("homeFragement");
            if (f != homeFragement) {
                HomeFragment homeFra = (HomeFragment) f;
                transaction.remove(homeFra);
                homeFra = null;
            }
            homeFragement = new HomeFragment();
            transaction.add(R.id.content_frame, homeFragement,
                    "homeFragement");
        } else {
            transaction.show(homeFragement);
        }
        return homeFragement;
    }


    private BaseFragment historyItemIndex() {
        if (historyFragment == null) {
            Fragment f = fragmentManager.findFragmentByTag("historyFragment");
            if (f != historyFragment) {
                HistoryFragment historyFra = (HistoryFragment) f;
                transaction.remove(historyFra);
                historyFra = null;
            }
            historyFragment = new HistoryFragment();
            transaction.add(R.id.content_frame, historyFragment,
                    "historyFragment");
        } else {
            transaction.show(historyFragment);
        }
        return historyFragment;
    }
    
//    private BaseFragment craftmanItemIndex() {
//        if (craftmanFragment == null) {
//            Fragment f = fragmentManager.findFragmentByTag("craftmanFragment");
//            if (f != craftmanFragment) {
//                CraftsManListActivity craftmanFra = (CraftsManListActivity) f;
//                transaction.remove(craftmanFra);
//                craftmanFra = null;
//            }
//            craftmanFragment = new CraftsManListActivity();
//            transaction.add(R.id.content_frame, craftmanFragment,
//                    "craftmanFragment");
//        } else {
//            transaction.show(craftmanFragment);
//        }
//        return craftmanFragment;
//    }

//    private BaseFragment jobCategoryItemIndex() {
//        if (jobCategoryFragment == null) {
//            Fragment f = fragmentManager.findFragmentByTag("jobCategoryFragment");
//            if (f != jobCategoryFragment) {
//                JobCategoryAcitivity jobCategoryFra = (JobCategoryAcitivity) f;
//                transaction.remove(jobCategoryFra);
//                jobCategoryFra = null;
//            }
//            jobCategoryFragment = new JobCategoryAcitivity();
//            transaction.add(R.id.content_frame, jobCategoryFragment,
//                    "jobCategoryFragment");
//        } else {
//            transaction.show(jobCategoryFragment);
//        }
//        return jobCategoryFragment;
//    }
    
    private BaseFragment settingsItemIndex() {
        if (settingsFragment == null) {
            Fragment f = fragmentManager.findFragmentByTag("settingsFragment");
            if (f != settingsFragment) {
                SettingsFragment settingFra = (SettingsFragment) f;
                transaction.remove(settingFra);
                settingFra = null;
            }
            settingsFragment = new SettingsFragment();
            transaction.add(R.id.content_frame, settingsFragment,
                    "settingsFragment");
        } else {
            transaction.show(settingsFragment);
        }
        return settingsFragment;
    }
	
	
	private void changColor(int index) {
		if(index > 2)
			return;
        homeRadio.setTextColor(getResources().getColor(R.color.table_default_color));
        historytRadio.setTextColor(getResources().getColor(R.color.table_default_color));
        settingsRadio.setTextColor(getResources().getColor(R.color.table_default_color));

        homeRadio.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                .getDrawable(R.drawable.icon_home_default), null, null);
        historytRadio.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                .getDrawable(R.drawable.icon_history_default), null, null);
        settingsRadio.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                .getDrawable(R.drawable.icon_settings_default), null, null);

        switch (index) {
            case HOME_ITEM_INDEX: {
            	homeRadio.setTextColor(getResources().getColor(R.color.emphasize_color));
            	homeRadio.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                        .getDrawable(R.drawable.icon_home_selected), null, null);
                break;
            }
            case HISTORY_ITEM_INDEX: {
            	historytRadio.setTextColor(getResources().getColor(R.color.emphasize_color));
            	historytRadio.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                        .getDrawable(R.drawable.icon_history_selected), null, null);
                break;
            }
            case SETTING_ITEM_INDEX: {
            	settingsRadio.setTextColor(getResources().getColor(R.color.emphasize_color));
            	settingsRadio.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                        .getDrawable(R.drawable.icon_settings_selected), null, null);
                break;
            }
            default:
                break;
        }
    }
	
	@Override
	public void onClick(View v) {
		int index = -1;
        switch (v.getId()) {
            case R.id.menu_home:
                index = HOME_ITEM_INDEX;
                break;
            case R.id.menu_history:
                index = HISTORY_ITEM_INDEX;
                break;
            case R.id.menu_settings:
                index = SETTING_ITEM_INDEX;
                break;

            default:
                break;
        }
        updateFragement(index);
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locateService != null) {  
            try {  
            	locateService.unregisterCallback(mCallback);  
            } catch (RemoteException e) {  
            }  
        } 
		if(isServiceRunning(this,LOCATE_SERVICE)){
			unbindService(mLocateConn);// 注意要unbind
		}
	}
	
	private ICallback.Stub mCallback = new ICallback.Stub() {  
		  
        @Override  
        public void showResult(String result) {
        	if(!hasSend && App.user != null && App.user.getToken() != null){
				sendGPS(App.API_gps, result);
			}
        	if(App.CHOOSECITY)
    			return;
        	if(mCurrentFragement != null && mCurrentFragement.getFragmentType() == BaseFragment.FragmentType_homeFragement){
        		HomeFragment c =(HomeFragment)mCurrentFragement;
        		c.showCityName(result);
        	}
        		
        }  
    }; 
	
	/**
     * 用来判断服务是否运行.
     * @param context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
       if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
    
    public void getToken(){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("username", App.user.getPhone());
		params.addFormDataPart("password", App.user.getPassword());
		params.addFormDataPart("loginType", "password");
		HttpRequest.post(App.serverURL+App.API_login, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, false);
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					LoginResult loginResult = JSON.parseObject(t.getResults().toString(), LoginResult.class);
					App.isLogin = true;
					App.user.setToken(loginResult.getToken());
					getUserInfo();
				}else{
					App.isLogin = false;
				}
					
			}
		});
	}
    
    public void getUserInfo(){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		HttpRequest.post(App.serverURL+App.API_baseInfo, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					App.baseinfo = JSON.parseObject(t.getResults().toString(), Baseinfo.class);
					long expireDate = App.baseinfo.getExpireDate();
					if(expireDate > 0){
						String date = App.Date2StringYYMMDD(expireDate);
						if(!TextUtils.isEmpty(date)){
							if(expireDate < System.currentTimeMillis())
								VDialog.getDialogInstance().toast(MainActivity.this, "您的账户已经到期，请及时续费！");
							else if(expireDate - System.currentTimeMillis() < 604800000){
								VDialog.getDialogInstance().toast(MainActivity.this, "您的账户将在"+date+"到期，请及时续费！");
							}
								
						}
					}
				}
			}
		});
	}
    
    public void getFirstADList(String api, String adPosition){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("adPosition", adPosition);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					ADResult result = JSON.parseObject(t.getResults().toString(), ADResult.class);
					if(result != null && result.getList().size() != 0 ){
						AD item = result.getList().get(0);
						String adName = SharedPrefTool.getValue(SPKeys.SP_CRAFTMAN_FIRSTAD, ValueKeys.FIRSTAD_NAME, "");
						if(!item.getName().equals(adName)){
							Picasso.with(MainActivity.this).load(item.getImage()).into(target);
							SharedPrefTool.setValue(SPKeys.SP_CRAFTMAN_FIRSTAD, ValueKeys.FIRSTAD_NAME, item.getImage());
						}
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
    
    public void sendGPS(String api, String city){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("token", App.user.getToken());
		params.addFormDataPart("areaName", city);
		params.addFormDataPart("lng", App.longitude);
		params.addFormDataPart("lat", App.latitude);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					hasSend = true;
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
    
//    private void sendJoblist(String api){
//		RequestParams params = new RequestParams(this);
//		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
//			@Override
//			public void onStart() {
//				super.onStart();
//			}
//			
//			@Override
//			public void onFailure(int errorCode, String msg) {
//				super.onFailure(errorCode, msg);
//				 VDialog.getDialogInstance().toast(MainActivity.this, "网络异常，请检查你的网络是否连接后再试");
//			}
//			
//			@Override
//			public void onFinish() {
//				super.onFinish();
//			}
//			
//			@Override
//			protected void onSuccess(HttpResult t) {
//				super.onSuccess(t);
//				
//				if(t.getType().equals(App.SUCCESS)){
//					Joblist list = JSON.parseObject(t.getResults().toString(), Joblist.class);
//					if(list != null && list.getList() != null){
//						App.jobList = new ArrayList<Job>(list.getList());
//					}
//				}else if(!TextUtils.isEmpty(t.getContent())){
//				}
//			}
//		});
//	}
}
