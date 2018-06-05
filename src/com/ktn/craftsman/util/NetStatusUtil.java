package com.ktn.craftsman.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.util.Log;

/***
 * 检查网络
 * @author Administrator
 */
public class NetStatusUtil {

	private static ConnectivityManager mConnectivity;

	/**
	 * 检查网络状态
	 * @return true表示有网络可用，dalse表示无网络可用
	 */
	public static boolean isOnline(Context context) {
		if(context == null){
			return false;
		}
		mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		/**
		 * 检查网络连接，如果无网络可用，就不需要进行连网操作等
		 */
		
		NetworkInfo[]list =mConnectivity.getAllNetworkInfo();

		if (list == null) {
			return false;
		}
		
		for (int i = 0; i < list.length ; i++) {
			if (list[i].getState() == NetworkInfo.State.CONNECTED) {
			  return true ;
			}
		}
		return false;
	}
	
	/**
	 * 判断是什么网络
	 * 0是2G和3G     
	 * 1是WIFI
	 * 2是其他
	 */
	public static int CheckNetworkState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			return 0;
		}else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return 1;
		}else{
			return 2;
		}
	}
	public static String GetNetworkType(Context context)
	{
	    String strNetworkType = "";
	    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected())
	    {
	        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
	        {
	            strNetworkType = "WIFI";
	        }
	        else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
	        {
	            String _strSubTypeName = networkInfo.getSubtypeName();
	            
	            Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
	            
	            // TD-SCDMA   networkType is 17
	            int networkType = networkInfo.getSubtype();
	            switch (networkType) {
	                case TelephonyManager.NETWORK_TYPE_GPRS:
	                case TelephonyManager.NETWORK_TYPE_EDGE:
	                case TelephonyManager.NETWORK_TYPE_CDMA:
	                case TelephonyManager.NETWORK_TYPE_1xRTT:
	                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
	                    strNetworkType = "2G";
	                    break;
	                case TelephonyManager.NETWORK_TYPE_UMTS:
	                case TelephonyManager.NETWORK_TYPE_EVDO_0:
	                case TelephonyManager.NETWORK_TYPE_EVDO_A:
	                case TelephonyManager.NETWORK_TYPE_HSDPA:
	                case TelephonyManager.NETWORK_TYPE_HSUPA:
	                case TelephonyManager.NETWORK_TYPE_HSPA:
	                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
	                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
	                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
	                    strNetworkType = "3G";
	                    break;
	                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
	                    strNetworkType = "4G";
	                    break;
	                default:
	                    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
	                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) 
	                    {
	                        strNetworkType = "3G";
	                    }
	                    else
	                    {
	                        strNetworkType = _strSubTypeName;
	                    }
	                    
	                    break;
	             }
	             
	            Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
	        }
	    }
	    
	    Log.e("cocos2d-x", "Network Type : " + strNetworkType);
	    
	    return strNetworkType;
	}
}
