package com.ktn.craftsman.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.ktn.craftsman.App;
import com.ktn.craftsman.service.ICallback.Stub;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.ValueKeys;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class LocateService extends Service {

	private LocationClient locationClient = null;
	private RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<ICallback>();  
	public class MyBinder extends Binder{
		public LocateService getService() {
			return LocateService.this;
		}
	}
	
	 private IService.Stub mBinder = new IService.Stub() {  
		  
	        @Override  
	        public void unregisterCallback(ICallback cb) throws RemoteException {  
	            if (cb != null)   
	                mCallbacks.unregister(cb);  
	        }  
	  
	        @Override  
	        public void registerCallback(ICallback cb) throws RemoteException {  
	            if (cb != null)   
	                mCallbacks.register(cb);  
	        }  
	    };  
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initGps();
		
		
	}
	
	private void callBack(String cityName) {  
        int n = mCallbacks.beginBroadcast();  
        try {  
            for (int i = 0; i < n; i++) {  
                mCallbacks.getBroadcastItem(i)  
                        .showResult(cityName);  
            }  
        } catch (RemoteException e) {  
        }  
        mCallbacks.finishBroadcast();  
    }  
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		locationClient.stop();
	}
	
	
	private void initGps() {
		try{
			MyLocationListenner myListener = new MyLocationListenner();
			locationClient = new LocationClient(this); 
			locationClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);
			option.setAddrType("all");
			option.setCoorType("bd09ll");
			option.setScanSpan(5000);
			option.disableCache(true);
			option.setPriority(LocationClientOption.GpsFirst);
			locationClient.setLocOption(option);
			locationClient.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)
				return;
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				saveLocateInfo(location);
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				saveLocateInfo(location);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}
	
	private void saveLocateInfo(BDLocation location){
		App.cityName = location.getCity();
		App.longitude = location.getLongitude();
		App.latitude = location.getLatitude();
		SharedPrefTool.setValue(SPKeys.SP_LOCATE_CITY, ValueKeys.LOCATE_CITY_NAME, location.getCity());
		SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.LATITUDE, location.getLatitude()+"");
		SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.LONGITUDE, location.getLongitude()+"");
		callBack(location.getCity());
	}

}
