package com.ktn.craftsman.util;

import java.util.ArrayList;
import java.util.Timer;

import com.ktn.craftsman.App;
import com.ktn.craftsman.R;
import com.ktn.craftsman.bean.Bank;
import com.ktn.craftsman.bean.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VDialog implements VDialogInterface{
    private MyPopupWindow pw;
    public static final int OK = 101;
    public static final int CANCEL = 100;
    private Toast mToast;
    private static VDialog instance = null;
    private Timer mTimer;

    //公司选择弹出框
    private AlertDialog comSelDlg = null;
    //webview错误弹出框
    private AlertDialog webviewErrorDlg = null;
    
    public static synchronized VDialog getDialogInstance() {
        if (null == instance) {
            instance = new VDialog();
            VDialogAdapter.getDialogInstance().setInterface(instance);
        }
        return instance;
    }

    public PopupWindow getPopupWindow() {
        return pw;
    }

    private VDialog() {
        // 获取屏幕尺寸数据
//        getDeviceScreenSize();
    }

	@Override
	public void showDoubleBtnAlertDialog(Context ctx, String content, String okText, String cancelText,
			boolean isKickType, Handler handler) {
		
	}

	@Override
	public void showSingleBtnAlertDialog(Activity context, String content, String okText, boolean isKickType,
			Handler handler) {
		
	}
	
	@SuppressLint("NewApi")
	public void showDeleteConfirmDialog(Activity context, final Handler handler) {
      comSelDlg = new AlertDialog.Builder(context,Window.FEATURE_NO_TITLE)
    		  .setTitle("删除？")
    		  .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.sendEmptyMessage(1);
					comSelDlg.cancel();
				}
			})
    		  .setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					comSelDlg.cancel();
				}
			})
    		  .create();
      comSelDlg.setCanceledOnTouchOutside(false);
      comSelDlg.setCancelable(false);
      comSelDlg.show();

	}
	
	//更新弹出框
    private AlertDialog updateDlg = null;
	
	/**
     * 
     * 函数名 ：showUpgradeTipDlg 功能描述：版本升级弹出框
     
     * ：WH1509024 日期 ：2016年6月22日
     */
    public void showUpDateVersionDlg(final Activity context, View parent, final Handler handler,
    		Setting uninfo) {
        if(context != null && !context.isFinishing()) {
           if(parent !=null && parent.getWindowToken() != null) {
               LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               updateDlg = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT).create();
               updateDlg.setCanceledOnTouchOutside(false);
               updateDlg.setCancelable(false);
               updateDlg.show();
               final View view = inflater.inflate(R.layout.showupgradetipdlg_layout, null, false);
               WindowManager windowManager = context.getWindowManager();    
               Display display = windowManager.getDefaultDisplay();    
               WindowManager.LayoutParams lp = updateDlg.getWindow().getAttributes();    
               lp.width = (int)(display.getWidth()); //设置宽度    
               lp.height = (int)(display.getHeight()); //设置高度    
               updateDlg.getWindow().setAttributes(lp);    
               updateDlg.getWindow().setContentView(view);
               
               TextView updateVersion = (TextView)view.findViewById(R.id.tv_update_version);
               updateVersion.setText("版本号:"+uninfo.getAppVersion());
               
               // 现在更新
               TextView updateNow = (TextView) view.findViewById(R.id.update);
               updateNow.setOnClickListener(new OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Message msgs = new Message();
                       msgs.what = VDialogAdapter.OK;
                       handler.sendMessage(msgs);
                       closeUpdateDlg();
                   }
               });
               ImageView cancle = (ImageView) view.findViewById(R.id.cancle);
               cancle.setOnClickListener(new OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       closeUpdateDlg();
                   }
               });
           }
        }
    }
	
    public void closeUpdateDlg() {
        if(updateDlg != null) {
            updateDlg.cancel();
            updateDlg = null;
        }
    }
    
	@SuppressLint("NewApi")
	public void showQuitConfirmDialog(Activity context, final Handler handler) {
      comSelDlg = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
    		  .setTitle("确定退出？")
    		  .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.sendEmptyMessage(1);
					comSelDlg.cancel();
				}
			})
    		  .setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					comSelDlg.cancel();
				}
			})
    		  .create();
      comSelDlg.setCanceledOnTouchOutside(false);
      comSelDlg.setCancelable(false);
      comSelDlg.show();
      
	}
	

	@Override
	public boolean isKickDlgType() {
		return false;
	}

	@Override
	public void showConfirmDialog(Activity context, View parent, String content, String leftButtonText,
			String rightButtonText, Handler handler) {
		
	}

	@Override
	public void activateAccountTipPopupWindow(Activity context, View parent, String tip) {
		
	}

	@Override
	public void successDialog(Context context, int resourceId) {
			
	}

    public void release() {
        closePw();
        if(mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public boolean isShow() {
        if (pw != null && pw.isShowing()) {
            return true;
        }
        return false;
    }
    //判断Dialog是不是弹出来了
    public boolean isDialogShow() {
        if (comSelDlg != null && comSelDlg.isShowing()) {
            return true;
        }
        return false;
    }
    

    public boolean isHasShow() {
        if (pw != null) {
            // return pw.isShowing();
            closePw();
        }
        return false;
    }

    /**
     * 提示框
     * 
     * @param mContext
     * @param text
     */
    Handler handler = App.sGlobalHandler;

    private class ExcuteRunnable implements Runnable {
        private String text;
        private Context mContext;

        ExcuteRunnable(Context mContext,String text) {
            this.text = text;
            this.mContext = mContext;
        }

        @Override
        public void run() {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            }
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
    /**
     * showTimeType 0 表示短显示， 1表示长显示
     * */
    private class ExcuteRunnable1 implements Runnable {
        private String text;
        private int showTimeType;
        private Context mContext;

        ExcuteRunnable1(Context mContext,String text,int showTimeType) {
            this.text = text;
            this.showTimeType = showTimeType;
            this.mContext = mContext;
        }

        @Override
        public void run() {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, text, showTimeType);
            }
            mToast.setText(text);
            mToast.setDuration(showTimeType);
            mToast.show();
        }
    }
    // 避免重复弹出toast的函数
    public void toast(Context context, String text) {
        if(mToast != null){
            mToast.cancel();
            mToast = null;
        }
        if(!text.equals("未知TOKEN"))
        	handler.post(new ExcuteRunnable(context,text));
    }
    
    // 避免重复弹出toast的函数，显示长toast
    public void toastLong(Context context, String text,int showTimeType) {
        if(mToast != null){
            mToast.cancel();
            mToast = null;
        }
        handler.post(new ExcuteRunnable1(context,text,showTimeType));
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public void cancelToastWithLayout() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
//    // 设置阴影效果
//    private static void applyFunction(View root) {
//        if (root != null) {
//            RelativeLayout rApplyBg = (RelativeLayout) root.findViewById(R.id.rApplyBg);
//            if (rApplyBg != null) {
//                rApplyBg.getBackground().setAlpha(50);
//            }
//        }
//    }
//
//    private void setOutAnimation(View view, final OnDialogDismissListener listener) {
//        ScaleAnimation mPopupMenuOutAnimation = null;
//        if (mPopupMenuOutAnimation == null) {
//            // 创建弹出菜单收起时使用的动画
//            mPopupMenuOutAnimation = new ScaleAnimation(1.0f, 1.0f, 1f, 0f);
//            mPopupMenuOutAnimation.setDuration(200);
//            mPopupMenuOutAnimation.setAnimationListener(new AnimationListener() {
//                public void onAnimationStart(Animation arg0) {
//                }
//
//                public void onAnimationRepeat(Animation arg0) {
//                }
//
//                public void onAnimationEnd(Animation arg0) {
//                    closePw();
//                    if (listener != null) {
//                        listener.onDismiss();
//                    }
//                }
//
//            });
//        }
//
//        if (view != null) {
//            view.startAnimation(mPopupMenuOutAnimation);
//        }
//    }
//
//
//    /**
//     * 自定义弹出系统菜单
//     * 
//     * @param context
//     * @param parent
//     */
//    public void showSystemDialog(final Activity context, View parent, String conent, final Handler handler) {
//        if (isHasShow()) {
//            return;
//        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow = inflater.inflate(R.layout.show_sytem_dialog, null, false);
//        pw = new MyPopupWindow(context, vPopupWindow, deviceWidth, deviceHeight, true);
//
//        pw.setAnimationStyle(R.style.AnimationPreview);
//        pw.setFocusable(true);
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//        final TextView mContent = (TextView) vPopupWindow.findViewById(R.id.mContent);
//        mContent.setText(conent);
//
//        RelativeLayout cancel = (RelativeLayout) vPopupWindow.findViewById(R.id.confirm_calcel);
//        cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closePw();
//                sendMessage(handler, CANCEL, "");
//            }
//        });
//
//        RelativeLayout ok = (RelativeLayout) vPopupWindow.findViewById(R.id.confirm_ok);
//        ok.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {// 确定
//                closePw();
//                sendMessage(handler, OK, "");
//            }
//        });
//
//        LinearLayout relativedownload = (LinearLayout) vPopupWindow.findViewById(R.id.cj_popup);
//        relativedownload.setBackgroundDrawable(new BitmapDrawable());
//        relativedownload.setBackgroundColor(context.getResources().getColor(R.color.btm));
//        relativedownload.setFocusable(false);
//        relativedownload.setFocusableInTouchMode(false);
//        relativedownload.setOnKeyListener(new OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    // pw.dismiss();
//                }
//                return true;
//            }
//        });
//    }
//
//    /**
//     * 自定义弹出窗口
//     * 
//     * @param context
//     * @param parent
//     * @parm content
//     * @parm leftButtonText
//     * @parm rightButtonText
//     * @parm handler
//     */
//    public void showConfirmDialog(final Activity context, View parent, String content, String leftButtonText,
//            String rightButtonText, final Handler handler) {
//        if (isHasShow()) {
//            return;
//        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow = inflater.inflate(R.layout.show_confirm_dialog, null, false);
//        pw = new MyPopupWindow(context, vPopupWindow, deviceWidth, deviceHeight, true);
//
//        pw.setAnimationStyle(R.style.AnimationPreview);
//        pw.setFocusable(true);
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//        final TextView mContent = (TextView) vPopupWindow.findViewById(R.id.mContent);
//        mContent.setText(content);
//
//        final TextView mLeftButtonText = (TextView) vPopupWindow.findViewById(R.id.mqd);
//        mLeftButtonText.setText(leftButtonText);
//
//        final TextView mRightButtonText = (TextView) vPopupWindow.findViewById(R.id.mqx);
//        mRightButtonText.setText(rightButtonText);
//
//        RelativeLayout cancel = (RelativeLayout) vPopupWindow.findViewById(R.id.rCalcel);
//        cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closePw();
//                sendMessage(handler, CANCEL, "");
//            }
//        });
//
//        RelativeLayout ok = (RelativeLayout) vPopupWindow.findViewById(R.id.rOk);
//        ok.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {// 确定
//                closePw();
//                sendMessage(handler, OK, "");
//            }
//        });
//
//        LinearLayout relativedownload = (LinearLayout) vPopupWindow.findViewById(R.id.cj_popup);
//        relativedownload.setBackgroundDrawable(new BitmapDrawable());
//        relativedownload.setBackgroundColor(context.getResources().getColor(R.color.btm));
//        relativedownload.setFocusable(false);
//        relativedownload.setFocusableInTouchMode(false);
//        relativedownload.setOnKeyListener(new OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    // pw.dismiss();
//                }
//                return true;
//            }
//        });
//    }
//
//
//    /**
//     * 自定义弹出系统菜单
//     * 
//     * @param context
//     * @param parent
//     */
//    public void showSystemDialogText(final Activity context, View parent, String conent, final Handler handler,
//            String text) {
//        if (isHasShow()) {
//            return;
//        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow = inflater.inflate(R.layout.show_sytem_dialog, null, false);
//        pw = new MyPopupWindow(context, vPopupWindow, deviceWidth, deviceHeight, true);
//
//        pw.setAnimationStyle(R.style.AnimationPreview);
//        pw.setFocusable(true);
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//        final TextView mContent = (TextView) vPopupWindow.findViewById(R.id.mContent);
//        mContent.setText(conent);
//
//        RelativeLayout cancel = (RelativeLayout) vPopupWindow.findViewById(R.id.confirm_calcel);
//        cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closePw();
//                sendMessage(handler, CANCEL, "");
//            }
//        });
//
//        TextView mqd = (TextView) vPopupWindow.findViewById(R.id.mqd);
//        mqd.setText(text);
//
//        RelativeLayout ok = (RelativeLayout) vPopupWindow.findViewById(R.id.confirm_ok);
//        ok.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {// 确定
//                closePw();
//                sendMessage(handler, OK, "");
//            }
//        });
//
//        LinearLayout relativedownload = (LinearLayout) vPopupWindow.findViewById(R.id.cj_popup);
//        relativedownload.setBackgroundDrawable(new BitmapDrawable());
//        relativedownload.setBackgroundColor(context.getResources().getColor(R.color.btm));
//        relativedownload.setFocusable(false);
//        relativedownload.setFocusableInTouchMode(false);
//        relativedownload.setOnKeyListener(new OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    // pw.dismiss();
//                }
//                return true;
//            }
//        });
//    }
//
//
    public void showBankAlertDialog(final Context ctx, final ArrayList<Bank> banklist,final Handler handler) {
        if (ctx != null&&!((Activity)ctx).isFinishing()) {
            
            comSelDlg = new AlertDialog.Builder(ctx,AlertDialog.THEME_HOLO_LIGHT).create();
            comSelDlg.setCanceledOnTouchOutside(false);
            comSelDlg.setCancelable(false);
            comSelDlg.show();
           
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.banklist, null, false);
            comSelDlg.getWindow().setContentView(view);
            ListView listView = (ListView)view.findViewById(R.id.list_view);
            ArrayList<String> list = new ArrayList<String>();
            for (Bank bank : banklist) {
				list.add(bank.getName());
			}
            listView.setAdapter(new ArrayAdapter<String>(ctx, R.layout.simple_item, list));
           

           listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Bank bank = banklist.get(position);
				
				 Message msg = null;
                   msg = handler.obtainMessage(OK);
                   msg.arg1 = bank.getId();
                   Bundle bundle = new Bundle();
                   bundle.putString("name", bank.getName());
                   msg.setData(bundle);
                   comSelDlg.cancel();
               if (msg != null) {
                   msg.sendToTarget();
               }
			}
		});

            
        }
    }
//    
//    
//    
//    /*
//     * 弹出AlertDialog 退出系统
//     */
//    public void showExitDialog(final Context ctx, String content, String okText, String cancelText,String title,
//            final Handler handler) {
//        if (ctx != null) {
//            final AlertDialog dlg = new AlertDialog.Builder(ctx).create();
//            dlg.setCanceledOnTouchOutside(false);
//            dlg.setCancelable(false);
//            dlg.show();
//
//            LayoutInflater inflater = (LayoutInflater) App.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final View view = inflater.inflate(R.layout.show_system_dialog_new, null, false);
//            dlg.getWindow().setContentView(view);
//
//            final TextView mContent = (TextView) view.findViewById(R.id.mContent);
//            final TextView mTitle = (TextView) view.findViewById(R.id.show_title);
//            // mContent.setTextSize(14);
//            mContent.setText(content);
//            mTitle.setText(title);
//            
//            final TextView leftBtn = (TextView) view.findViewById(R.id.mqd);
//            leftBtn.setText(cancelText);
//
//            final TextView rightBtn = (TextView) view.findViewById(R.id.mqx);
//            rightBtn.setText(okText);
//
//            RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.rCalcel);
//
//            RelativeLayout ok = (RelativeLayout) view.findViewById(R.id.rOk);
//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v != null) {
//                        Message msg = null;
//                        switch (v.getId()) {
//                        case R.id.rCalcel: {
//                            msg = handler.obtainMessage(OK);
//                            break;
//                        }
//                        case R.id.rOk: {
//                            msg = handler.obtainMessage(CANCEL);
//                        }
//                        }
//                        dlg.cancel();
//                        if (msg != null) {
//                            msg.sendToTarget();
//                        }
//                    }
//                }
//            };
//            cancel.setOnClickListener(clickListener);
//            ok.setOnClickListener(clickListener);
//        }
//    }
//    
//    /*
//     * 弹出AlertDialog 只有 "确定" 按钮
//     */
//    public void showSingleBtnAlertDialog(final Activity context, String content, String okText, final boolean isKickType ,final Handler handler) {
//        if (context != null) {
//            final AlertDialog dlg = new AlertDialog.Builder(context).create();
//            dlg.setCanceledOnTouchOutside(false);
//            dlg.setCancelable(false);
//            dlg.show();
//
//            LayoutInflater inflater = (LayoutInflater) App.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final View view = inflater.inflate(R.layout.show_sytem_dialog_only_ok, null, false);
//            dlg.getWindow().setContentView(view);
//
//            final TextView mContent = (TextView) view.findViewById(R.id.mContent);
//            // 设置确认按钮的值
//            final TextView mOk = (TextView) view.findViewById(R.id.btn_ok);
//            mContent.setText(content);
//            if (!TextUtils.isEmpty(okText)) {
//                mOk.setText(okText);
//                mOk.setTextColor(context.getResources().getColor(R.color.emphasize_color));
//            }
//
//            RelativeLayout okBtn = (RelativeLayout) view.findViewById(R.id.lyt_ok);
//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v != null) {
//                        isKickDlgType = false ;
//                        Message msg = null;
//                        switch (v.getId()) {
//                        case R.id.lyt_ok: {
//                            if(DateUtil.equalsDelTime()){
//                                return;
//                            }
//                            msg = handler.obtainMessage(OK);
//                        }
//                        }
//                        dlg.cancel();
//                        if (msg != null) {
//                            msg.sendToTarget();
//                        }
//                    }
//                }
//            };
//            okBtn.setOnClickListener(clickListener);
//            isKickDlgType = isKickType;
//        }
//    }
//    
//    /*
//     * 弹出AlertDialog 只有 "确定" 按钮
//     */
//    public void showPermissionAlertDialog(final Activity context, final Handler handler) {
//        if (context != null && !context.isFinishing()) {
//            View rootView = ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
//            if(rootView == null||rootView.getWindowToken() == null) {//如果activity依附的View返回为null，直接返回
//                return ;
//            }
//            final AlertDialog dlg = new AlertDialog.Builder(context).create();
//            dlg.setCanceledOnTouchOutside(false);
//            dlg.setCancelable(false);
//            dlg.show();
//
//            LayoutInflater inflater = (LayoutInflater) App.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final View view = inflater.inflate(R.layout.meet_permission_layout, null, false);
//            dlg.getWindow().setContentView(view);
//            Button okBtn = (Button) view.findViewById(R.id.permission_iknow);
//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v != null) {
//                        Message msg = null;
//                        switch (v.getId()) {
//                        case R.id.permission_iknow: {
//                            msg = handler.obtainMessage(OK);
//                        }
//                        }
//                        dlg.cancel();
//                        if (msg != null) {
//                            msg.sendToTarget();
//                        }
//                    }
//                }
//            };
//            okBtn.setOnClickListener(clickListener);
//        }
//    }
//
//    /**
//     * 自定义弹出系统菜单,带button设置功能
//     * 
//     * @author WH1407054 zhuxiang
//     * @param context
//     * @param parent
//     */
//    public void showSystemDialog(final Activity context, View parent, String conent, String btnLeft, String btnRight,
//            final Handler handler) {
//        if (isHasShow()) {
//            return;
//        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow = inflater.inflate(R.layout.show_sytem_dialog, null, false);
//        pw = new MyPopupWindow(context, vPopupWindow, deviceWidth, deviceHeight, true);
//
//        pw.setAnimationStyle(R.style.AnimationPreview);
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//        final TextView mContent = (TextView) vPopupWindow.findViewById(R.id.mContent);
//        mContent.setText(conent);
//
//        final TextView leftBtn = (TextView) vPopupWindow.findViewById(R.id.mqx);
//        leftBtn.setText(btnLeft);
//
//        final TextView rightBtn = (TextView) vPopupWindow.findViewById(R.id.mqd);
//        rightBtn.setText(btnRight);
//
//        RelativeLayout cancel = (RelativeLayout) vPopupWindow.findViewById(R.id.confirm_calcel);
//        cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closePw();
//                sendMessage(handler, CANCEL, "");
//            }
//        });
//
//        RelativeLayout ok = (RelativeLayout) vPopupWindow.findViewById(R.id.confirm_ok);
//        ok.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {// 确定
//                closePw();
//                sendMessage(handler, OK, "");
//            }
//        });
//
//        final LinearLayout relativedownload = (LinearLayout) vPopupWindow.findViewById(R.id.cj_popup);
//        relativedownload.setBackgroundDrawable(new BitmapDrawable());
//        relativedownload.setBackgroundColor(context.getResources().getColor(R.color.btm));
//        relativedownload.setFocusable(true);
//        relativedownload.setFocusableInTouchMode(true);
//
//        // 监听时要和外面一致 KeyEvent.ACTION_DOWN
//        relativedownload.setOnKeyListener(new OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && KeyEvent.ACTION_DOWN == event.getAction()
//                        && event.getRepeatCount() == 0) {
//                    closePw();
//                }
//                return true;
//            }
//        });
//    }
    /**
     * @method: closePw @Description: 关闭和释放资源 @throws
     */
    public void closePw() {
        if (pw != null) {
            Activity attach = pw.getAttachActivity();
            if (attach != null && !attach.isFinishing()) {
                pw.closePopupWindow();
                pw = null;
            }
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    
    //关闭所有对话框
    public void closeAllPw() {
    	closePw();
    	
    	if(comSelDlg != null) {
    		comSelDlg.cancel();
    		comSelDlg = null;
    	}
    	
    	if(webviewErrorDlg != null) {
    		webviewErrorDlg.cancel();
    		webviewErrorDlg = null;
    	}
    }
    
    public void closePw(int type){
        if (pw != null) {
            Activity attach = pw.getAttachActivity();
            if (attach != null && !attach.isFinishing() && type == pw.getPopType()) {
                pw.closePopupWindow();
                pw = null;
            }
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void closePw(Activity ac) {
        if (pw != null && ac != null) {
            Activity attach = pw.getAttachActivity();

            if (ac == attach && !ac.isFinishing()) {
                pw.closePopupWindow();
                pw = null;
            }
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
//
//    /*
//     * 输入弹出框 editHint: 输入框中默认显示的字符串 emptyTips：输入框为空时点击确定弹出的提示 handler:
//     * 结果回调cancelWhat, okWhat,OK时回调的msg.obj为输入框内容(String类型) okWhat、cancelWhat
//     * 确定或取消时回调的msg.what值
//     */
//    public void showEditDialog(final Activity context, String editHint, final String emptyTips, final Handler handler,
//            final int okWhat, final int cancelWhat) {
//        if (context != null) {
//            // 如果用的是AlertDialog， 弹出框中的EditText，点击不能弹出软键盘,只能用Dialog
//            // final AlertDialog dlg = new AlertDialog.Builder(ctx).create();
//            final Dialog dlg = new Dialog(context);
//            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            dlg.setCanceledOnTouchOutside(false);
//            dlg.setCancelable(false);
//            dlg.show();
//
//            LayoutInflater inflater = (LayoutInflater) App.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final View view = inflater.inflate(R.layout.show_edit_dialog, null, false);
//            dlg.getWindow().setContentView(view);
//
//            // 输入框
//            final EditText editContent = (EditText) view.findViewById(R.id.edit_content);
//            editContent.setHint(editHint);
//            editContent.requestFocus();
//            editContent.setClickable(true);
//
//            RelativeLayout btnCancel = (RelativeLayout) view.findViewById(R.id.rCalcel);
//            RelativeLayout btnOk = (RelativeLayout) view.findViewById(R.id.rOk);
//
//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v != null) {
//                        Message msg = null;
//                        switch (v.getId()) {
//                        case R.id.rCalcel: {
//                            msg = handler.obtainMessage(cancelWhat);
//                            break;
//                        }
//                        case R.id.rOk: {
//                            String content = "";
//                            if (editContent != null) {
//                                content = editContent.getText().toString();
//                                if (TextUtils.isEmpty(content)) {
//                                    toast(context, emptyTips);
//                                    return;
//                                }
//                            }
//                            msg = handler.obtainMessage(okWhat, content);
//                            break;
//                        }
//                        }
//                        dlg.cancel();
//                        if (msg != null) {
//                            msg.sendToTarget();
//                        }
//                    }
//                }
//            };
//
//            btnCancel.setOnClickListener(clickListener);
//            btnOk.setOnClickListener(clickListener);
//        }
//    }
//
//    /*
//     * 获取验证码请求发送成功后，弹出dlg提示用户
//     */
//    public void showGetVerifyCodeDialog(final Activity context, String content) {
//        if (context != null) {
//            final Dialog dlg = new Dialog(context);
//            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            dlg.setCanceledOnTouchOutside(false);
//            dlg.setCancelable(false);
//            dlg.show();
//
//            LayoutInflater inflater = (LayoutInflater) App.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final View view = inflater.inflate(R.layout.dialog_test, null, false);
//
//            dlg.getWindow().setContentView(view);
//
//            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
//            tv_content.setText(content);
//
//            TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
//            btnOk.setText("关闭");
//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v != null) {
//                        switch (v.getId()) {
//                        case R.id.btn_ok: {
//                            closePw();
//                            if (dlg != null) {
//                                dlg.dismiss();
//                            }
//                            break;
//                        }
//                        }
//                    }
//                }
//            };
//            btnOk.setOnClickListener(clickListener);
//        }
//    }
//
//
//
//
//    private void setListViewHeight(ListView listView, int itemHeight, int size) {
//        if (listView == null || itemHeight <= 0 || size < 1) {
//            return;
//        }
//
//        int listViewHeight = size * itemHeight;
//
//        LayoutParams layoutParams = listView.getLayoutParams();
//        layoutParams.height = listViewHeight;
//        listView.setLayoutParams(layoutParams);
//    }
//
//
//
//    
//    
//    
//    /**
//     * 
//     * 函数名 ：messageDetailPictureDescDialog 功能描述：个人信息图像弹出框 输入参数：@param context
//     * 输入参数：@param parent 输入参数：@param conent 输入参数：@param handler 返回值 ：void 异常 ：无
//     * 创建人 ：WH1406011 日期 ：2015年8月5日
//     */
//    public void uploadLoadDialog(final Activity context, View parent, final Handler handler) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow = inflater.inflate(R.layout.upload_load_dialog, null, false);
//        // pw = new PopupWindow(vMyPopupWindow, deviceWidth, deviceHeight,
//        // true);
//        pw = new MyPopupWindow(context, vPopupWindow, WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.FILL_PARENT, true);
//
//        // pw.setAnimationStyle(R.style.AnimationPreview);
//        pw.setAnimationStyle(R.style.Animation_REVER_DWAWER);
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//        // 删除
//        RelativeLayout one = (RelativeLayout) vPopupWindow.findViewById(R.id.one);
//        // 是否是默认头像
//        boolean bDefaultImg = false;
//        if (context instanceof PersonDetailEditActivity) {
//            // 来自设置我的页面
//            bDefaultImg = ((PersonDetailEditActivity) context).isDefaultHeardImg();
//        }
//        if (bDefaultImg) {
//            // 设置该项不可用，不能删除默认头像
//            one.setEnabled(false);
//            TextView tv = (TextView) vPopupWindow.findViewById(R.id.mPz);
//            if (tv != null) {
//                tv.setTextColor(context.getResources().getColor(R.color.huise_bg));
//            }
//        } else {
//            // 非默认头像，则可以删除处理
//            one.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    Message msgs = new Message();
//                    msgs.what = 1;
//                    handler.sendMessage(msgs);
//                    closePw();
//                }
//            });
//        }
//
//        // 拍摄
//        RelativeLayout two = (RelativeLayout) vPopupWindow.findViewById(R.id.two);
//        two.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Message msgs = new Message();
//                msgs.what = 2;
//                handler.sendMessage(msgs);
//                closePw();
//            }
//        });
//
//        // 从相册里选择
//        RelativeLayout three = (RelativeLayout) vPopupWindow.findViewById(R.id.three);
//        three.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message msgs = new Message();
//                msgs.what = 3;
//                handler.sendMessage(msgs);
//                closePw();
//            }
//        });
//
//        // 取消
//        RelativeLayout rlCancel = (RelativeLayout) vPopupWindow.findViewById(R.id.rlCancel);
//        rlCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closePw();
//            }
//        });
//
//        final RelativeLayout relativedownload = (RelativeLayout) vPopupWindow.findViewById(R.id.cj_popup);
//        relativedownload.setBackgroundDrawable(new BitmapDrawable());
//        relativedownload.setBackgroundColor(context.getResources().getColor(R.color.btm));
//        vPopupWindow.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    closePw();
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//
    /**
     * @method: showSelectSexDlg
     * @Description: 显示选择性别弹出框
     * @param context
     * @param parent
     * @param handler
     * @param
     */
//    public void showSelectSexDlg(final Activity context, View parent, final Handler handler) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow = inflater.inflate(R.layout.lyt_select_sex_dlg, null, false);
//        // pw = new PopupWindow(vPopupWindow, deviceWidth, deviceHeight, true);
//        pw = new MyPopupWindow(context, vPopupWindow, WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.FILL_PARENT, true);
//
//        // pw.setAnimationStyle(R.style.AnimationPreview);
//        pw.setAnimationStyle(R.style.Animation_REVER_DWAWER);
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//        android.view.View.OnClickListener clickListener = new android.view.View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message msg = null;
//                switch (v.getId()) {
//                case R.id.lyt_male: { // 男
//                    msg = handler.obtainMessage(OK);
//                    msg.arg1 = SEX.MALE;
//                    break;
//                }
//                case R.id.lyt_female: { // 女
//                    msg = handler.obtainMessage(OK);
//                    msg.arg1 = SEX.FEMALE;
//                    break;
//                }
//                case R.id.rlCancel: {
//                    msg = handler.obtainMessage(CANCEL);
//                    break;
//                }
//                }
//                if (msg != null) {
//                    msg.sendToTarget();
//                }
//                closePw();
//            }
//
//        };
//
//        View viewMale = vPopupWindow.findViewById(R.id.lyt_male); // 男
//        View viewFemale = vPopupWindow.findViewById(R.id.lyt_female); // 女
//        View rlCancel = vPopupWindow.findViewById(R.id.rlCancel);
//        rlCancel.setOnClickListener(clickListener);
//        viewMale.setOnClickListener(clickListener);
//        viewFemale.setOnClickListener(clickListener);
//
//        final RelativeLayout relativedownload = (RelativeLayout) vPopupWindow.findViewById(R.id.cj_popup);
//        relativedownload.setBackgroundDrawable(new BitmapDrawable());
//        relativedownload.setBackgroundColor(context.getResources().getColor(R.color.btm));
//        vPopupWindow.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    closePw();
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//
//    
//    // 设置gridView的数据源
//    private void setGridView(final Activity context, final GridView gridView, final TextView mPz,
//            final boolean isSingleSelect) {
//        new AsyncTask<Void, Void, ArrayList<String>>() {
//            @Override
//            protected ArrayList<String> doInBackground(Void... params) {
//                ArrayList<String> imagePath = ChatHelper.getInstance().getLatestImagePaths(context, 9);
//                return imagePath;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<String> imagePath) {
//                if (imagePath != null) {
//                    int size = imagePath.size();
//                    int length = 80;
//                    DisplayMetrics dm = new DisplayMetrics();
//                    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
//                    float density = dm.density;
//                    int gridviewWidth = (int) (size * (length + 4) * density);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth,
//                            LinearLayout.LayoutParams.FILL_PARENT);
//                    gridView.setLayoutParams(params); // 重点
//                    gridView.setColumnWidth(SystemUtil.dip2px(context, 80)); // 重点
//                    gridView.setHorizontalSpacing(3); // 间距
//                    gridView.setStretchMode(GridView.NO_STRETCH);
//                    gridView.setNumColumns(size); // 重点
//
//                    SrcoolGridViewAdapter adapter = new SrcoolGridViewAdapter(context, imagePath,
//                            new OnChangeListener() {
//                        @Override
//                        public void onSelectChangeListener() {
//                            int count = ConfigData.getInstance().getSelectedCount();
//                            if (count >= 1) {
//                                mPz.setText(context.getResources().getString(R.string.send) + " (" + count + ")" + " "
//                                        + context.getResources().getString(R.string.look));
//                                mPz.setTextColor(context.getResources().getColor(R.color.emphasize_color));
//                            } else {
//                                mPz.setText(context.getResources().getString(R.string.select_img));
//                                mPz.setTextColor(context.getResources().getColor(R.color.important_characters));
//                            }
//                        }
//                    }, isSingleSelect);
//                    gridView.setAdapter(adapter);
//                }
//            }
//
//        }.execute();
//
//    }

    /**
     * 显示自定义的toast
     * @param msg
     */
    public static void showCustomToast(Context mContext,String msg) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.error_tip, null);
        TextView txt = (TextView) view.findViewById(R.id.tipContent);
        if (txt != null) {
            txt.setText(msg);
        }
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }

    /**
     * 显示错误码提示，2s后关闭
     * @param msg
     */
    public void showErrorCode(final Context mContext,final String msg) {
        Runnable post = new Runnable() {
            @Override
            public void run() {
                showCustomToast(mContext,msg);
            }
        };
        App.sGlobalHandler.post(post);
    }

    private LoadingDialog  mLoadingDlg = null;

    /**
     * 显示loding框
     */
    @Override
    public void showLoadingDialog(Context mContext, int strResId, boolean cancelable, boolean autoClose,int msgType) {
        String content =  mContext.getResources().getString(strResId);
        showLoadingDialog(mContext,content,cancelable,autoClose,msgType);
    }
      /**
     * 显示loding框
     */
    public void showLoadingDialog(Context mContext, String content, boolean cancelable, boolean autoClose,int msgType) {
        hideLoadingDialog(-1);
        mLoadingDlg = new LoadingDialog(mContext, R.style.myDialog,msgType);
        mLoadingDlg.setDialogLayout(R.layout.loading_tip);
        mLoadingDlg.setTipContent(content);
        // 如果上面传递下来是TRUE则不消失框
        mLoadingDlg.setDialogCanceable(cancelable);
        mLoadingDlg.setAutoClose(autoClose); // 是否需要自动消息
        mLoadingDlg.show();
    }
//    
    /*
     * 关闭正在显示的弹出框
     */
    public void closeLoadingDialog(){
        if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
            mLoadingDlg.dismiss();
            mLoadingDlg = null;
        }
    }
    
    /**
     * 隐藏loding框
     * 返回true，表示有dlg显示， false 没有dlg在显示
     */
    @Override
    public boolean hideLoadingDialog(int msgType) {
        if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
            
            //  force close
            if(msgType == -1){
                mLoadingDlg.dismiss();
                mLoadingDlg = null;
            }else {
                if(msgType ==mLoadingDlg.getmDlgType()){
                    mLoadingDlg.dismiss();
                    mLoadingDlg = null;
                }
            }
            return true;
        }
        return false;
    }

	@Override
	public void showLoadingDialog(String content, boolean cancelable, boolean autoClose, int type) {
		
	}

	@Override
	public void showErrorCode(String msg) {
		
	}
    
    Toast toast;

}
