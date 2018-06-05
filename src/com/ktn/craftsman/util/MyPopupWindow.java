package com.ktn.craftsman.util;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;

public class MyPopupWindow extends PopupWindow{
	
	// dialog 所依附的对话框
	private Activity mAttachActivity ;
	
	private int popType;
	
	public MyPopupWindow(Activity activity,View layout,int width,int height,boolean canBeFocus){
		super(layout, width, height, canBeFocus);
		setAttachActivity(activity);
	}
	
	public MyPopupWindow(Activity activity,View layout,int width,int height,boolean canBeFocus,int type){
		super(layout, width, height, canBeFocus);
		setAttachActivity(activity);
		popType = type;
	}
	
	public Activity getmAttachActivity() {
		return mAttachActivity;
	}

	public void setmAttachActivity(Activity mAttachActivity) {
		this.mAttachActivity = mAttachActivity;
	}

	public int getPopType() {
		return popType;
	}

	public void setPopType(int popType) {
		this.popType = popType;
	}

	protected void setAttachActivity(Activity a ){
		mAttachActivity =a ;
	}
	
	protected Activity getAttachActivity(){
		return mAttachActivity ;
	}
	
	protected void closePopupWindow(){
		if(mAttachActivity != null ){
			View contentViwe =getContentView();
			if(contentViwe != null){
				contentViwe.setBackgroundDrawable(null);
				contentViwe.destroyDrawingCache();
				contentViwe=null;
			}
			dismiss();
			mAttachActivity = null;
		}
	}
	
	
	
}
 