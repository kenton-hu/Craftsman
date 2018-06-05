package com.ktn.craftsman;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.ktn.craftsman.util.LoadingDialog;
import com.ktn.craftsman.util.NetStatusUtil;
import com.ktn.craftsman.util.VDialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpTaskHandler;

public class BaseActivity extends FragmentActivity implements HttpCycleContext{
	// dialog 依附的activity销毁时的回调通知
	public static interface AttachActivtyDestroyCallback {
		public void onDestroy();
	}

    //启动主activity的intent参数设置
    public static final String IntentParam_FromActivty = "FromActivty"; //启动主Activity的界面
    public static final String IntentParam_LoginType = "LoginType"; //附带的登录类型（自动登录，注册登录，密码登录，已登录）
    
    private int modelType = -1;
    private boolean mEnterResumeFlag = false;
    // 全局的定时器
    protected static final long LOGIN_TIMEOUT_TIME = 60000;
    protected TimeCount timeCount;
    protected boolean isReceiveMessage = false;
    protected LoadingDialog mLoginLogingDlg = null;
    protected static final long LOGIN_TIMEOUT_INTEVAL = 1000;
    protected static boolean isShowStatusBar = true; // 是否显示状态栏
    // activity销毁时的回调
    private AttachActivtyDestroyCallback mDestroyCallback;
    public void setDestroyCallback(AttachActivtyDestroyCallback callback) {
    	mDestroyCallback = callback;
    }

    //系统常量
    private final int KITKAT = 19;
    private final int FLAG_TRANSLUCENT_STATUS = 67108864 ;
    
    protected final String tag = this.getClass().getSimpleName();

    // 提供子类 重写
    public void onTimeOut(){
    	
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
   
   public void netClose(){  
       if (mLoginLogingDlg != null && mLoginLogingDlg.isShowing()) {
           mLoginLogingDlg.dismiss();
           mLoginLogingDlg = null;
           if(timeCount != null){
               timeCount.cancel();
               timeCount = null;
           }

//           VDialog.getDialogInstance().toast(this,
//        		   App.getContext().getResources().getString(R.string.check_net_setting));
       }
       isReceiveMessage = false ;  
   }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }
    
    /* 
     * 改变系统TOP栏的颜色
     */
    private void changeTopColor(){
        if (Build.VERSION.SDK_INT >= KITKAT) {
            setTranslucentStatus(true);
        }

    }

    
    @TargetApi(19) 
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        //WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        final int bits = FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
    	 super.onDestroy();
    	 mEnterResumeFlag = false;
        // 退出的时候解注册广播  在设置界面中不能STOP就解注册
        unRegistBroadcast();
        VDialog.getDialogInstance().closePw(this);
        
        if (mDestroyCallback != null) {
        	mDestroyCallback.onDestroy();
        }
        
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        if (mLoginLogingDlg != null && mLoginLogingDlg.isShowing()) {
            mLoginLogingDlg.dismiss();
            mLoginLogingDlg = null;
        }
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }

    @Override
    protected void onPause() {
        super.onPause();
      
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(this.getClass().getSimpleName().equals("WiGuestureProtectActivity")) {
        	return ;
        }
        
    }
    
    public void setCallSystemAppFlag() {
        // App.isBackgroundByCallSystemApp = true;
    }

    public void resetCallSystemAppFlag() {
        // App.isBackgroundByCallSystemApp = false;
    }
    
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (VDialog.getDialogInstance().getPopupWindow() != null
                    && VDialog.getDialogInstance().getPopupWindow().isShowing()) {
                VDialog.getDialogInstance().closePw();
                return true;
            }
        }
        return false;
    }*/

    public void closeInputMethod(Activity act) {
        if(act == null){
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       if(inputMethodManager == null){
    	   return;
       }
        boolean isOpen = inputMethodManager.isActive();
        if (isOpen) {
            if (inputMethodManager != null) {
                if(act.getCurrentFocus() == null){
                    return;
                }
                inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /*
     * 超时计时器
     */
    public class TimeCount extends CountDownTimer {
        // private boolean reset = false;
        private Handler handler = new Handler();

        public TimeCount(long _millisInFuture, long _countDownInterval) {
            super(_millisInFuture, _countDownInterval);
        }

        /*
         * 重新计时，不关掉进度条，将计时器重新计时
         */
        public void resetTime() {
            // reset = true;
            this.cancel();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TimeCount.this.start();
                    isReceiveMessage = true;
                }
            }, 30);
        }

        @Override
        public void onFinish() {
            isReceiveMessage = false;
//            VDialog.getDialogInstance().toast(App.getContext(), App.getContext().getResources()
//                    .getString(R.string.server_no_response));

            if (mLoginLogingDlg != null && mLoginLogingDlg.isShowing()) {
                mLoginLogingDlg.dismiss();
                mLoginLogingDlg = null;
            }
            
            onTimeOut();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isReceiveMessage = true;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    
    
    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    /*
     * toast显示提示
     */
    protected void toast(final int strID) {
        VDialog.getDialogInstance().toast(this, this.getResources().getString(strID));
    }
    
    /*
     * toast显示提示
     */
    protected void toast(final String msg) {
    	if(!msg.startsWith("未知TOKEN"))
    		VDialog.getDialogInstance().toast(this, msg);
    }

    ////////////////// begin message 广播处理 /////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        // 注册广播
        registBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * @method: addReceiveAction
     * @Description: 添加activity中需要使用的actions
     * @param action: 就是原来监听的messageType 注意：
     *            如果要添加action，需要在onCreate中就设置，否则过了onStart()就不生效了
     */
    protected void addReceiveAction(int action) {
    }

    /**
     * @method: addReceiveAction
     * @Description: 添加activity中需要使用的actions
     * @param action: 可以是自己定义的action，比如: 更新通讯录、组织结构
     */
    protected void addReceiveAction(String action) {
        mReceiveActions.add(action);
    }

    /*
     * 如果子类activity中需要监听消息处理的结果， 重写这个方法， 从intent中获取想要的数据
     */
    protected void handleBroadcastReceiver(Context context, Intent intent) {
        
    }

    private ArrayList<String> mReceiveActions = new ArrayList<String>();

    
      /** 
      * @method: sendLocaBroadcast 
      * @Description: 发送本地广播通知UI
      * @param it
      * @throws 
      */
    public void sendLocalBroadcast(Intent it) {
        if (it != null) {
//            LocalBroadcastManager.getInstance(AppAdapter.getInstance().getContext()).sendBroadcast(it);
        }
    }
    
    /*
     * 监听添加结果广播
     */
    private void registBroadcast() {
        // 如果有需要监听的action， 就注册个广播，否则不注册
        if (mReceiveActions == null || mReceiveActions.isEmpty()) {
            //logd("[registBroadcast] -- no receive action registed, return *** ");
            return ;
        }
        if (mLocalReceiver != null) {
            //logd("[registBroadcast] -- receiver already registed, return *** ");
            return ;
        }
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        mLocalReceiver = new LocalBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        for (String action : mReceiveActions) {
            intentFilter.addAction(action);
        }
        lbm.registerReceiver(mLocalReceiver, intentFilter);
    }

    /*
     * 注销广播
     */
    private void unRegistBroadcast() {
        // 如果已经注册过广播，走的时候要记得“注销”
        if (mLocalReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalReceiver);
            //loge("[unRegistBroadcast] -- ************ ");
        }
    }

    private LocalBroadcastReceiver mLocalReceiver = null;

    // 协议事件广播接收
    private class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleBroadcastReceiver(context, intent);
        }
    }
    ////////////////// end message 广播处理 /////////////////////////////////
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean consumed = false;
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME ) {
            consumed = onBack();
        }
        return !consumed ? super.onKeyDown(keyCode, event) : consumed;
    }

    protected boolean onBack() {
        //logd("[onBack] -- you click back key *** ");
        return false;
    }
    
    /*
	 * 检查是否有网络无网络则给出提示
	 */
	protected boolean checkNetStatus(){
		if(NetStatusUtil.isOnline(App.mAppContext)){
			return true;
		}else{
			VDialog.getDialogInstance().toast(this, getResources().getString(R.string.check_net_setting));
		}
		return false;
	}
	
//    public static <T> String bean2Json(T bean) {
//        return WiJsonTools.bean2Json(bean);
//    }
//    
//    //json转换为bean
//    public static <T> T json2Bean(String json, Type type) {
//        return FastJsonTools.json2BeanObject(json, type);
//    }
//    
//    //json转换为list
//    public static <T> List<T> jsonToList(String json, Class<T> type) {
//        return FastJsonTools.jsonToList(json, type);
//    }
	
	//在BaseActivity或BaseFragment中添加字段
	protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

	@Override
	public String getHttpTaskKey() {
	    return HTTP_TASK_KEY;
	}
	
}
