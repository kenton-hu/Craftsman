package com.ktn.craftsman.util;

import java.io.File;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.App;
import com.ktn.craftsman.RegisterAgreementActivity;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.Setting;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class AppVersionUpdate {

	 // 默认版本号
    public static final String DEFAULT_VERSION = "1.0";
    // 正在下载的版本 download Id， 调用mDownloadMgr时，系统分配的
    private long sDownloadId = -1;
    // 下载的apk文件路径
    private String mApkPath = "";
    // 系统下载管理模块
    private DownloadManager mDownloadMgr = null;
    //单实例对象创建
    private static AppVersionUpdate instance  = new AppVersionUpdate();
    //版本请求回应
    private Setting resultSetting;
    
    private AppVersionUpdate() {
        // 注册系统下载监听广播
        App.getInstance().registerReceiver(mDownloadReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    
	public static AppVersionUpdate getInstance() {
		return instance;
	}
	
	 public void checkAppVersion() {
		 getSetting("app/setting.jhtml");
	 }
	 
	 /*
     * 获取app版本号
     */
    public String getAppVersion() {
        try {
            PackageManager manager = App.getInstance().getMainActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.getInstance().getMainActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private boolean isNeedUpdate(){
    	if (resultSetting == null) {
			return false;
		}
    	String serverVersion = resultSetting.getAppVersion();
    	String curVersion = getAppVersion();
    	
    	return VersionComparison(serverVersion, curVersion);
    }
    
    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(sDownloadId);
            Cursor c = mDownloadMgr.query(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL: {
                            startInstall(mApkPath);
                            break;
                        }
                        case DownloadManager.STATUS_FAILED: {
                            break;
                        }
                    }
                    sDownloadId = -1;
                }
            }
        }
    };
    
    /** 
     *  
     * @param version1 
     * @param version2 
     * @return if version1 > version2, return 1, if equal, return 0, else return 
     *         -1 
     */  
    public boolean VersionComparison(String versionServer, String versionLocal) {  
        String version1 = versionServer;  
        String version2 = versionLocal; 
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)  
            throw new IllegalArgumentException("Invalid parameter!");  
  
        int index1 = 0;  
        int index2 = 0;  
        while (index1 < version1.length() && index2 < version2.length()) {  
            int[] number1 = getValue(version1, index1);  
            int[] number2 = getValue(version2, index2);  
  
            if (number1[0] < number2[0]){  
                return false;  
            }  
            else if (number1[0] > number2[0]){  
                return true;  
            }  
            else {  
                index1 = number1[1] + 1;  
                index2 = number2[1] + 1;  
            }  
        }  
        if (index1 == version1.length() && index2 == version2.length())  
            return false;  
        if (index1 < version1.length())  
            return true;  
        else  
            return false;  
    }
    
    /** 
     *  
     * @param version 
     * @param index 
     *            the starting point 
     * @return the number between two dots, and the index of the dot 
     */  
    public static int[] getValue(String version, int index) {  
        int[] value_index = new int[2];  
        StringBuilder sb = new StringBuilder();  
        while (index < version.length() && version.charAt(index) != '.') {  
            sb.append(version.charAt(index));  
            index++;  
        }  
        value_index[0] = Integer.parseInt(sb.toString());  
        value_index[1] = index;  
  
        return value_index;  
    }  
    
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == VDialogAdapter.OK) {
                // 确定下载
                startDownload();
                // 如果是“强制升级”， 下载交给系统后，退出app
//                if (isForceUpdae) {
//                    //弹出所有的activity
//                    MyActivityManager.getActivityManager().popAllActivity();
//                    //MobclickAgent.onKillProcess(AppAdapter.getInstance().getContext());
//                }
            } else if (msg.what == VDialogAdapter.CANCEL) {
                // 如果是“强制升级”， 取消就要退去app
                // TODO, 需要修改
            } else {
            }
        }
    };
    
    /*
     * 调用系统下载新版本
     */
	private void startDownload() {
        if (resultSetting != null) {
            String url = resultSetting.getDownloadAndroid();
            String newsVersion = resultSetting.getAppVersion();

            // sdcard的目录下的download文件夹
            String apkName = getApkName(url, newsVersion);
            String path = App.AppPath;
            // 保存apk路径
            mApkPath = path + apkName;

            // 如果apk文件已经存在本地了， 就不用下载了，直接安装
            File apkFile = new File(mApkPath);
            if (apkFile.isFile() && apkFile.exists()) {
                if(!getUninatllApkInfo(mApkPath)) {
                    //安装包不完整，正在下载中
                    //Toast.makeText(AppAdapter.getInstance().getContext(), "安装包正在下载中，请稍候...", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    startInstall(mApkPath);
                    return;
                }
                
            }

            //logd("[startDownload -- ] UpInfo : " + info.toString());
            mDownloadMgr = getDownloadMgr();

            Uri resource = Uri.parse(url);
            DownloadManager.Request request = null;
            try {
            	request = new DownloadManager.Request(resource);
			} catch (Exception e) {
				Toast.makeText(App.getInstance().getMainActivity(), "下载路径错误，请重试...", Toast.LENGTH_LONG).show();
				return;
			}
            
            // 设置文件类型
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                    .getFileExtensionFromUrl(url));
            request.setMimeType(mimeString);
            //logd("[startDownload] -- mimeString : " + mimeString);
            // 在通知栏中显示
            request.setVisibleInDownloadsUi(false);

            request.setDestinationInExternalPublicDir("/craft", apkName);
            // notify titile ： "T信 版本升级：xxxx"
//                String title = String.format(getString(CPResourceUtil.getStringId("notify_title_app_update")), 
//                        getString(CPResourceUtil.getStringId("app_name")), newsVersion);
            String title ="琪坤工匠 版本升级：" + newsVersion;
            request.setTitle(title);

            sDownloadId = mDownloadMgr.enqueue(request);
        }
    }
    
    private DownloadManager getDownloadMgr() {
        if (mDownloadMgr == null) {
            mDownloadMgr = (DownloadManager) App.getInstance().getMainActivity()
                    .getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return mDownloadMgr;
    }
    
    /*
     * 安装apk版本文件
     */
    private void startInstall(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        
        // apk 文件不存在  直接返回
        if(!FileUtil.exited(filePath)){
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(filePath)),
                "application/vnd.android.package-archive");
        
        Context currActy = App.getInstance().getMainActivity();
        if (currActy != null) {
            currActy.startActivity(intent);
        }
    }
    
    public static boolean getUninatllApkInfo(String filePath) {
        try {
            PackageManager pm = App.getInstance().getMainActivity().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    /*
     * 根据url或版本号好，获取apkName
     */
    private String getApkName(final String url, final String version) {
        String apk_name = "";
        if (!TextUtils.isEmpty(url)) {
            if (url.endsWith(".apk")) {
                int index = url.lastIndexOf("/");
                if (index > -1) {
                    apk_name = url.substring(index + 1);
                }
            }
        }

        if (TextUtils.isEmpty(apk_name)) {
            if (!TextUtils.isEmpty(version)) {
                apk_name = version + ".apk";
            } else {
                apk_name = "craftman.apk";
            }
        }

        return apk_name;
    }
    
    public void getSetting(String api){
		RequestParams params = new RequestParams(App.getInstance().getMainActivity());
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
				VDialog.getDialogInstance().showLoadingDialog(App.getInstance().getMainActivity(),
						"正在加载数据",true,true,0);
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				VDialog.getDialogInstance().closeLoadingDialog();
				VDialog.getDialogInstance().toast(App.getInstance().getMainActivity(), "网络异常，请检查你的网络是否连接后再试");
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
					resultSetting = JSON.parseObject(t.getResults().toString(), Setting.class);
					if(resultSetting != null && resultSetting.getPhone() != null){
						App.customServiceNum = resultSetting.getPhone();
						SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.CUSTOM_SERVICE, resultSetting.getPhone());
					}
					if(isNeedUpdate()){
						VDialog.getDialogInstance().showUpDateVersionDlg(App.getInstance().getMainActivity(), 
								App.getInstance().getMainActivity().getCurrentFocus(), handler, resultSetting);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
					VDialog.getDialogInstance().toast(App.getInstance().getMainActivity(), t.getContent());
				}
			}
		});
	}
}
