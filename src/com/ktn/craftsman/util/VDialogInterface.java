package com.ktn.craftsman.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.PopupWindow;


/**
  * 类名      ：VDialogInterface
  *
  * 描述      ：
  *
  * 创建人    ：WH1408008
  *
  * 日期      ：2016年3月17日
  * 
  */
 
public interface VDialogInterface {
    public void toast(Context context, String text);
    
    /*
     * 弹出AlertDialog 确定、 取消
     */
    public void showDoubleBtnAlertDialog(final Context ctx, String content, String okText, String cancelText, boolean isKickType,final Handler handler);
            
                /*
     * 弹出AlertDialog 只有 "确定" 按钮
     */
    public void showSingleBtnAlertDialog(final Activity context, String content, String okText, boolean isKickType,final Handler handler);
    
    public boolean isKickDlgType ();
    
//    public void CompanyInviteDialog(final Activity context, View parent, final Handler handler,ArrayList<CompanyIDInfor> companyList);
    
    /**
     * 
     * 函数名 ：CompanyInviteDialog 功能描述：选择企业进入 输入参数：@param context 输入参数：@param
     * parent 输入参数：@param conent 输入参数：@param handler 返回值 ：void 异常 ：无 创建人
     * ：WH1406011 日期 ：2015年8月5日
     */
//    public void chooseCompanyToEnterDialog(final Activity context, View parent, final Handler handler,
//            ArrayList<CompanyIDInfor> companyList);
    
    //public void showNoCompanyDialog(final Context ctx, String okText);
    
    /**
     * @method: closePw @Description: 关闭和释放资源 @throws
     */
    public void closePw();
    
    public void showConfirmDialog(final Activity context, View parent, String content, String leftButtonText,
            String rightButtonText, final Handler handler);
    
    public PopupWindow getPopupWindow();
    
    /*
     * 激活帐号，帐号激活成功提示
     */
    public void activateAccountTipPopupWindow(final Activity context, View parent, String tip);
    
    public void successDialog(final Context context, int resourceId);
    
    //版本升级
//    public void showUpDateVersionDlg(final Activity context, View parent, final Handler handler,
//    		UpInfo uninfo,boolean isForceUpdae);
//    public void showUpDateVersionDlgNew(final Activity context, View parent, final Handler handler,
//            UpInfo uninfo,boolean isForceUpdae);
    public void closePw(Activity ac);

    /**
     * opration显示菊花转圈
     */
    public void showLoadingDialog(Context mContext, int strResId, boolean cancelable, boolean autoClose,int type);
    
	public void showLoadingDialog(String content, boolean cancelable, boolean autoClose,int type);

    /**
     * opration隐藏菊花转圈
     */
    public boolean hideLoadingDialog(int type); 

    /**
     * 显示错误码信息
     */
    public void showErrorCode(String msg);
}
