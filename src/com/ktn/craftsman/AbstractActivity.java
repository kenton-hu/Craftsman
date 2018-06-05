
package com.ktn.craftsman;

import java.lang.reflect.Type;
import java.util.List;

import com.ktn.craftsman.util.NetStatusUtil;
import com.ktn.craftsman.util.VDialog;

import android.os.Bundle;
import android.view.View.OnClickListener;

public abstract class AbstractActivity extends BaseActivity{
	
	protected String TAG = "ui";
    protected int[] wh = new int[2];
    // 显示时长
    protected int showTime = 20;
    
    @Override
    protected void onCreate(Bundle arg0) {
    	requestWindowFeature();
        super.onCreate(arg0);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 初始化布局
        this.setContentView(loadLayout());
        setReceiveActions();
        // 查找控件
        findView();
        // 做oncreate的事情
        onCreate();
        // 设置监听
        setListener();
        // 加载数据
        loadData();
    }
    
    protected void requestWindowFeature() {
		
	}

    protected abstract int loadLayout();

    protected abstract void findView();

    protected abstract void onCreate();

    protected abstract void setListener();

    protected abstract void loadData();

	protected void setReceiveActions() {	
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
	
//	public static <T> String bean2Json(T bean) {
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
}
