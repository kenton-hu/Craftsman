
package com.ktn.craftsman.util;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import com.ktn.craftsman.BaseActivity;
import com.ktn.craftsman.BaseActivity.AttachActivtyDestroyCallback;
import com.ktn.craftsman.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * loding菊花弹框
 */
public class LoadingDialog extends AlertDialog implements Observer, AttachActivtyDestroyCallback{
    private int reid;
    private Activity mAttachActivity ; // 当前activity 依附的activity
    private boolean isAutoClose = false; // 是否自动关闭
    private int mTimeout = 30; // 超时关闭时间， 默认30s
    private int mDlgType ; // 
    public LoadingDialog(Activity context) {
        super(context);
    }

    /**
	 * @return the mDlgType
	 */
	public int getmDlgType() {
		return mDlgType;
	}

	/**
	 * @param mDlgType the mDlgType to set
	 */
	public void setmDlgType(int mDlgType) {
		this.mDlgType = mDlgType;
	}
	
	 public LoadingDialog(Activity context, int theme){
		 super(context, theme);
	 }

    /**
     * @param mContext
     * @param theme 设置样式
     */
    public LoadingDialog(Context mContext, int theme,int type){
        super(mContext, theme);
        setmDlgType(type);
    }

    
    boolean isRegistedNetObserver = false;
    /**
     * 需要调supper.dismiss();不然不会关闭弹出框
     */
    @Override
    public void dismiss(){
    	if (!isRegistedNetObserver) {
//            NetWorkStateObservalHelper.getInstance().removeNetworkObserver(this);
    	}
    	isRegistedNetObserver = false;

        if (isShowing()) {
            super.dismiss();
        }

        // 停止计时器
        cacelOverTimer();
    }

    public LoadingDialog(Context context, int theme, String message) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(reid);

        if (isSetTipContent) {
            TextView textView = (TextView) this.findViewById(R.id.toastName);
            if (textView != null) {
                textView.setText(resid);
            }

            TextView content = (TextView) this.findViewById(R.id.tipContent);
            if (content != null) {
                if (resid != 0) {
                    content.setText(resid);
                } else {
                    if (!TextUtils.isEmpty(tipContent)) {
                        content.setText(tipContent);
                    }
                }

            }
        }

//        NetWorkStateObservalHelper.getInstance().addNetworkObserver(this);
        isRegistedNetObserver = true;
    }

    public void setDialogLayout(int id) {
        reid = id;
    }

    private int resid;
    private boolean isSetTipContent = false;
    private String  tipContent;

    public void setDialogCanceable(boolean cancel) {
        this.setCancelable(cancel);
    }

    public void setTipContent(int stringID) {
        isSetTipContent = true;
        resid = stringID;
    }
    public void setTipContent(String content) {
        isSetTipContent = true;
        tipContent = content;
    }
    

    @Override
    public void update(Observable observable, Object data) {
        if (data != null && data instanceof Bundle) {
            Bundle d = (Bundle) data;
            // 网络中断
//            if (!d.getBoolean(NetworkStateObservable.DATA_KEY_NETWORK_STATUS,
//                    true)) {
//               if(mAttachActivity != null && mAttachActivity instanceof BaseActivity) {
//                   BaseActivity a = (BaseActivity)mAttachActivity;
//                   a.netClose();
//               }
//            }
        }
    }
    

    @Override
	public void show() {
		super.show();
		// 如果是自动关闭模式， 开启计时器
		if (isAutoClose) {
	        // 开启loding超时计时器
	        if(overTimer != null) {
	            overTimer.cancel();
	            overTimer = null;
	        }
	        overTimer = new Timer();

	        if(overTimerTask != null) {
	            overTimerTask.cancel();
	            overTimerTask = null;
	        }
	        overTimerTask = new OverTimerTask();

	        int overTimeCount = mTimeout * 1000;
	        overTimer.schedule(overTimerTask, overTimeCount);
		}
	}

    /**
     * 设置超时时间， 如果设置了时间，会到计时自动关闭
     * @param timeout
     */
    public void setAutoClose(boolean autoClose, int timeout) {
    	this.mTimeout = timeout;
    	this.isAutoClose = autoClose;
    }

    /**
     * 设置倒计时自动关闭(默认30s)， 如果设置了时间，会到计时自动关闭
     * @param timeout
     */
    public void setAutoClose(boolean autoClose) {
    	this.isAutoClose = autoClose;
    }

	@Override
	public void hide() {
		super.hide();
		cacelOverTimer();
	}

	@Override
	public void cancel() {
		super.cancel();
		cacelOverTimer();
	}

	/*
     * begin 用timer管理loding弹框的超时关闭操作 
     */
    private Timer overTimer = null;
    private OverTimerTask overTimerTask = null;

    // 计时器到时， 关闭弹框
    class OverTimerTask extends TimerTask {
        @Override
        public void run() {
            dismiss();
            if(mAttachActivity != null){
            	BaseActivity a = (BaseActivity)mAttachActivity;
            	a.onTimeOut();
            }
        }
    }

    public void cacelOverTimer() {
        if(overTimer != null) {
            overTimer.cancel();
            overTimer = null;
        }
        
        if(overTimerTask != null) {
            overTimerTask.cancel();
            overTimerTask = null;
        }
    }

	@Override
	public void onDestroy() {
		dismiss();
		//PLog.d("LoadingDialog", "LoadingDialog -- attacted activity is destroyed, so destroy dialog now *** ");
	}
}
