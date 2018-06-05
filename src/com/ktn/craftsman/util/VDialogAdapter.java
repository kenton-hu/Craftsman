package com.ktn.craftsman.util;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.PopupWindow;


/**
  * 类名      ：VDialogAdapter
  *
  * 描述      ：
  *
  * 创建人    ：WH1408008
  *
  * 日期      ：2016年3月17日
  * 
  */
 
public class VDialogAdapter implements VDialogInterface{
    public static final int OK = 101;
    public static final int CANCEL = 100;
    
    private static VDialogAdapter instance;
    
    private VDialogInterface vDialogInterface;
    private VDialogAdapter() {
        
    }
    
    public static VDialogAdapter getDialogInstance() {
        if (null == instance) {
            instance = new VDialogAdapter();
        }
        
        return instance;
    }
    
    public void setInterface(VDialogInterface vDialogInterface) {
        this.vDialogInterface = vDialogInterface;
    }
    
    public void toast(Context context, String text) {
        if(vDialogInterface != null) {
            vDialogInterface.toast(context, text);
        }
    }
    
    /*
     * 弹出AlertDialog 确定、 取消
     */
    public void showDoubleBtnAlertDialog(final Context ctx, String content, String okText, String cancelText,
    		boolean isKickType,  final Handler handler) {
        if(vDialogInterface != null) {
            vDialogInterface.showDoubleBtnAlertDialog(ctx, content, okText, cancelText,isKickType, handler);
        }
    }
            
    /*
     * 弹出AlertDialog 只有 "确定" 按钮
     */
    public void showSingleBtnAlertDialog(final Activity context, String content, String okText, boolean isKickType,final Handler handler) {
        if(vDialogInterface != null) {
            vDialogInterface.showSingleBtnAlertDialog(context, content, okText,isKickType, handler);
        }
    }
    
//    public void CompanyInviteDialog(final Activity context, View parent, final Handler handler,
//            ArrayList<CompanyIDInfor> companyList) {
//        if(vDialogInterface != null) {
//            vDialogInterface.CompanyInviteDialog(context, parent, handler,companyList);
//        }
//    }
    
//    public void chooseCompanyToEnterDialog(final Activity context, View parent, final Handler handler,
//            ArrayList<CompanyIDInfor> companyList) {
//        if(vDialogInterface != null) {
//            vDialogInterface.chooseCompanyToEnterDialog(context, parent, handler,companyList);
//        }
//    }
    
//    public void showNoCompanyDialog(final Context ctx, String okText) {
//        if(vDialogInterface != null) {
//            vDialogInterface.showNoCompanyDialog(ctx, okText);
//        }
//    }
    
    public void closePw() {
        if(vDialogInterface != null) {
            vDialogInterface.closePw();
        }
    }
    
    public void showConfirmDialog(final Activity context, View parent, String content, String leftButtonText,
            String rightButtonText, final Handler handler) {
        if(vDialogInterface != null) {
            vDialogInterface.showConfirmDialog(context,parent,content,leftButtonText,rightButtonText,handler);
        }
    }
    
    public PopupWindow getPopupWindow() {
        if(vDialogInterface != null) {
            return vDialogInterface.getPopupWindow();
        }
        
        return null;
    }
    
    
    public void activateAccountTipPopupWindow(final Activity context, View parent, String tip) {
        if(vDialogInterface != null) {
            vDialogInterface.activateAccountTipPopupWindow(context,parent,tip);
        }
    }
    
    public void successDialog(final Context context, int resourceId) {
        if(vDialogInterface != null) {
            vDialogInterface.successDialog(context, resourceId);
        } 
    }
    
    public void closePw(Activity ac) {
        if(vDialogInterface != null) {
            vDialogInterface.closePw(ac);
        }
    }

	@Override
	public void showLoadingDialog(Context mContext, int strResId, boolean cancelable, boolean autoClose,int type) {
        if(vDialogInterface != null) {
            vDialogInterface.showLoadingDialog(mContext, strResId, cancelable, autoClose,type);
        }
	}

	@Override
	public boolean hideLoadingDialog(int type) {
        if(vDialogInterface != null) {
           return vDialogInterface.hideLoadingDialog(type);
        }
        return false;
	}

	@Override
	public void showErrorCode(String msg) {
		if (vDialogInterface != null) {
			vDialogInterface.showErrorCode(msg);
		}
	}

	@Override
	public void showLoadingDialog(String content, boolean cancelable,
			boolean autoClose,int type) {
		  if(vDialogInterface != null) {
	            vDialogInterface.showLoadingDialog(content, cancelable, autoClose, type);
	        }
	}

	@Override
	public boolean isKickDlgType() {
		boolean isKickDlgType = false ;
		  if(vDialogInterface != null) {
			  isKickDlgType= vDialogInterface.isKickDlgType();
	        }
		  return isKickDlgType ;
	}

//	@Override
//	public void showUpDateVersionDlg(Activity context, View parent, Handler handler, UpInfo uninfo,boolean isForceUpdae) {
//		if(vDialogInterface != null) {
//            vDialogInterface.showUpDateVersionDlg(context, parent, handler, uninfo,isForceUpdae);
//        }
//	}

//    @Override
//    public void showUpDateVersionDlgNew(Activity context, View parent, Handler handler, UpInfo uninfo,
//            boolean isForceUpdae) {
//        if(vDialogInterface != null) {
//            vDialogInterface.showUpDateVersionDlgNew(context, parent, handler, uninfo,isForceUpdae);
//        }
//    }

}
