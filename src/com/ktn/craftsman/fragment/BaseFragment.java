
package com.ktn.craftsman.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ktn.craftsman.App;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpTaskHandler;


/**
* @ClassName: BaseFragment 
* @Description: fragment 基础类， 主要提供了广播注册、 log函数等
* @date 2015年8月14日
 */
public class BaseFragment extends Fragment implements HttpCycleContext{
    public static int FragmentType_homeFragement = 1;
    public static int FragmentType_historyFragment = 2;
    public static int FragmentType_settingFragment = 3;
    public static int FragmentType_craftmanFragment = 4;
    public static int FragmentType_jobCategoryFragment = 5;
    
    public int getFragmentType() {
        //默认返回聊天界面
        return 0;
    }
    
    private final String tag = this.getClass().getSimpleName();
    protected boolean isDetach= true ;
    
    public void onShow(){
    	
    }
    
    public void onHide(){
    	
    }

    private LocalBroadcastReceiver mLocalReceiver = null; // 广播接收器
    // 要接收的actions(messageType)集合
    private ArrayList<String> mReceiveActions = new ArrayList<String>();

    // //////////////// begin message 广播处理 /////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        registBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegistBroadcast();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	isDetach =false ;
    }
    
    @Override
    public void onDetach() {
    	isDetach = true;
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * 如果子类activity中需要监听消息处理的结果， 重写这个方法， 从intent中获取想要的数据
     */
    protected void handleBroadcastReceiver(Context context, Intent intent) {

    }

    /*
     * 监听添加结果广播
     */
    private void registBroadcast() {
        // 如果有需要监听的action， 就注册个广播，否则不注册
        if (mReceiveActions == null || mReceiveActions.isEmpty()) {
            return;
        }
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(App.mAppContext);
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
            LocalBroadcastManager.getInstance(App.mAppContext).unregisterReceiver(mLocalReceiver);
        }
    }

    // 协议事件广播接收
    private class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleBroadcastReceiver(context, intent);
        }
    }
    // //////////////// end message 广播处理 /////////////////////////////////

    /*
     * 返回事件处理
     */
    public boolean onBack() {
		return false;
	}
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
	@Override
	public String getHttpTaskKey() {
		// TODO Auto-generated method stub
		return HTTP_TASK_KEY;
	}
	
}
