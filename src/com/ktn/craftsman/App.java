/**
 *-----------------------------------------------------------------------
 * <copyright file="App.java" company="SINOSUN">
 *     Copyright (c) Sinosun Technology Co., Ltd. All rights reserved.
 * </copyright>
 * <author>WH1406004</author>
 * <summary>This is the Widget class.</summary>
 * -----------------------------------------------------------------------
 */

package com.ktn.craftsman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.baidu.mapapi.SDKInitializer;
import com.ktn.craftsman.bean.Baseinfo;
import com.ktn.craftsman.bean.Job;
import com.ktn.craftsman.bean.JobCategory;
import com.ktn.craftsman.bean.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * 类名 ：App 描述 ：程序运行类 
 */

public class App extends Application {
	// APPID
	public static Context mAppContext;
    protected boolean bRemoteApp = false;

	public static boolean isLogin = false; // 是否是登录
	public static User user =  new User();
	public static Baseinfo baseinfo = new Baseinfo();
	
	public static final String JUMPFROM = "jumpform"; // 0 注册  2修改密码
	
	public static final String SUCCESS = "success"; // 成功
	public static final String FAILURE = "failure"; // 失败
	
	public static boolean CHOOSECITY = false;
	
	public static String TYPE = "type"; // 类型
	public static String PHONE = "phone";
	public static String CODE = "IdentifyingCode";
	public static String PW = "password";
	
	public static String JOBNAME = "";
	public static int JOBID = 0;
	
	public static String JOBCATEGORYNAME = "";
	public static int JOBCATEGORYINDEX = 0;
	public static int JOBCATEGORYID = 0;
	
	public static String cityName = "";
	public static double longitude; 
	public static double latitude;

	private static App instance;
	
	public static ArrayList<JobCategory> jobCategoryList;
//	public static ArrayList<Job> jobList;
	
	public static final String USER = "consumer"; // 用户
	public static final String CRAFTMAN = "worker"; // 工匠
	
	public static final String AppPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/craft/";
	public static final String firstADimg = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/craft/firstAD.png";
	
	public static String customServiceNum= "400-0264-602"; 
	
	public static final String serverURL = "http://www.huizhongjiaju.com/"; 
//	public static final String serverURL = "http://gjapp.jxlnxx.com/"; 
	
	public static final String API_message = "app/message/verify.jhtml";
	public static final String API_message_register = "register";
	public static final String API_message_forget = "forgotten";
	public static final String API_message_change = "changePhone";
	public static final String API_message_login = "login";
	
	public static final String API_message_auth = "app/message/auth.jhtml";
	public static final String API_register = "app/register.jhtml";
	public static final String API_login = "app/login.jhtml";
	public static final String API_joblist = "app/job/list.jhtml";
	public static final String API_jobcategorylist = "app/job_category/list.jhtml";
	public static final String API_historylist = "app/history/list.jhtml";
	public static final String API_memberlist = "app/member/list.jhtml";
	public static final String API_memberDetail = "app/member/detail.jhtml";
	public static final String API_reviewlist = "app/review/list.jhtml";
	public static final String API_review = "app/review/save.jhtml";
	public static final String API_baseInfo = "app/member/baseInfo.jhtml";
	public static final String API_edit = "app/member/edit.jhtml";
	public static final String API_bindPhone = "app/member/bindPhone.jhtml";
	public static final String API_idauth = "app/auth/id.jhtml";
	public static final String API_workauth = "app/auth/work.jhtml";
	public static final String API_depositlist = "app/deposit/list.jhtml";
	public static final String API_banklist = "app/bank/list.jhtml";
	public static final String API_bindbank = "app/member_bank/bind.jhtml";
	public static final String API_memberbanklist = "app/member_bank/list.jhtml";
	public static final String API_atmsave = "app/atm/save.jhtml";
	public static final String API_upload = "app/file/upload.jhtml";
	public static final String API_favorite = "app/favorite/save.jhtml";
	public static final String API_favoritelist = "app/favorite/list.jhtml";
	public static final String API_favoriteexsit = "app/favorite/exsit.jhtml";
	public static final String API_favoritedelete = "app/favorite/delete.jhtml";
	public static final String API_nameauth = "app/auth/id.jhtml";
	public static final String API_adlist = "app/ad/list.jhtml";
	public static final String API_notifylist = "app/notify/list.jhtml";
	public static final String API_notifydetail = "app/notify/detail.jhtml";
	public static final String API_jobsearch = "app/job/search.jhtml";
	public static final String API_historysave = "app/history/save.jhtml";
	public static final String API_getauth = "app/auth/get.jhtml";
	public static final String API_gps = "app/gps.jhtml";
	public static final String API_addcase = "app/case/save.jhtml";
	// 可全局使用的“主线程handler”
	public static Handler sGlobalHandler = new Handler();
	private MainActivity mainActivity;// MainActivity的实例

	@Override
	public void onCreate() {
		super.onCreate();
//		 SDKInitializer.initialize(getApplicationContext()); 
		 SDKInitializer.initialize(this);
		OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
		builder.setTimeout(15000);
		OkHttpFinal.getInstance().init(builder.build());
		DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.drawable.defalut) 
                .showImageOnFail(R.drawable.defalut) 
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
	}
	
    public App() {
		App.instance = this;
	}
    
    public static int getScreenWidth() {
		return App.getInstance().getResources().getDisplayMetrics().widthPixels;
	}
	public static int getScreenHeight() {
		return App.getInstance().getResources().getDisplayMetrics().heightPixels;
	}
    
    /**
	 * 获得程序对象
	 * 
	 * @return
	 */
	public static App getInstance() {
		return instance;
	}
    
	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	/** 
	 * 验证手机格式 
	 */  
	public static boolean isMobileNO(String mobiles) {  
	    /* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(12[0-9])|(14[0-9])|(17[0-9]))\\d{8}$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
   } 
	
	/**
     * 验证身份证号是否符合规则
     * @param text 身份证号
     * @return
     */
     public static boolean personIdValidation(String text) {
          String regx = "[0-9]{17}x";
          String reg1 = "[0-9]{15}";
          String regex = "[0-9]{18}";
          return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }
	
	public static boolean isIDNO(String id) {  
	    /* 
	    */  
	    String telRegex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
	    if (TextUtils.isEmpty(id)) return false;  
	    else return id.matches(telRegex);  
   } 
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(float dpValue) {  
        final float scale = App.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(float pxValue) {  
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
    private static final double EARTH_RADIUS = 6378137.0;
    
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {  
		  
    	  double Lat1 = rad(lat1);
		  double Lat2 = rad(lat2);
		  double a = Lat1 - Lat2;
		  double b = rad(lon1) - rad(lon2);
		  double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
		    + Math.cos(Lat1) * Math.cos(Lat2)
		    * Math.pow(Math.sin(b / 2), 2)));
		  s = s * EARTH_RADIUS;
		  s = Math.round(s * 10000) / 10000;
		  return s;
	}
    
    private static double rad(double d) {
    	  return d * Math.PI / 180.0;
    }
    
    public static String Date2String(long time){
    	Date date = new Date(time);
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy年");
    	return sf.format(date);
    }
    
    public static String Date2StringMMDD(long time){
    	Date date = new Date(time);
    	SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
    	return sf.format(date);
    }
    
    public static String Date2StringYYMMDD(long time){
    	Date date = new Date(time);
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
    	return sf.format(date);
    }
    
    public void load(ImageView iv , String path) {
		if (new File(path).exists()){
			iv.setImageBitmap(getLoacalBitmap(path, iv.getWidth(), iv.getHeight()));
		}
	}
    
    public void load(ImageView iv , String path, boolean fillparent) {
		if (new File(path).exists()){
			iv.setImageBitmap(getLoacalBitmap(path, 0, 0));
		}
	}
    
    public void load(ImageView iv, int defaultImage, String path) {
		if (new File(path).exists()){
			iv.setImageBitmap(getLoacalBitmap(path, iv.getWidth(), iv.getHeight()));
		}
		else {
			iv.setImageResource(defaultImage);
		}
	}
    
    public Bitmap getLoacalBitmap(String path, int width, int height) {
		try {
			try {
				if (width == 0)
					width = App.getInstance().getMainActivity().getResources().getDisplayMetrics().widthPixels;
				if (height == 0)
					height = App.getInstance().getMainActivity().getResources().getDisplayMetrics().heightPixels;
			}catch (Exception e) {
			}
			if (width == 0)
				width = 640;
			if (height == 0)
				height = 480;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			if (options.outHeight > 0 && options.outWidth > 0) {// 加载图片优化
				int a = Math.round(Math.max(options.outHeight/(float)height, options.outWidth/(float)width));
				if (a < 1)
					a = 1;
				options = new BitmapFactory.Options();
				options.inSampleSize = a;
				return BitmapFactory.decodeFile(path, options);
			} else {
				return BitmapFactory.decodeFile(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
	/**
	 * 压缩图片
	 * @param srcFilePath 源图片文件路径
	 * @param toSize 目标大小
	 * @return Bitmap
	 */
	public static Bitmap resizePic(String srcFilePath, int toSize) {
		try {
			// 读取原始图片大小
			BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(srcFilePath, options);

            // 根据原始图像大小,计算需要载入的图像最接近toSize又不失真的大小
			int width = options.outWidth;
			int height = options.outHeight;
			options.inJustDecodeBounds = false;

			int toWidth = toSize;
			int toHeight = toSize;
			int be = 1;
			if (width > height) {
				toHeight = toSize * height / width;
				toHeight = toHeight % 2 == 0 ? toHeight : toHeight + 1;
				be = width * 10 / toSize;
			} else {
				toWidth = toSize * width / height;
				toWidth = toWidth % 2 == 0 ? toWidth : toWidth + 1;
				be = height * 10 / toSize;
			}
			options.inSampleSize = be / 10;
			
			// 按目标大小读取图片,最小化内存占用
			Bitmap bitmapOrg = null;
			FileInputStream fiStream = new FileInputStream(srcFilePath);
			try {
				// 使用decodeStream可更好的避免内存溢出
				bitmapOrg = BitmapFactory.decodeStream(fiStream, null, options);
				if (bitmapOrg.getWidth() > toWidth || bitmapOrg.getHeight() > toHeight) {
					bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, toWidth, toHeight, true);
				}
			} catch(OutOfMemoryError er) {
				if(null != bitmapOrg){
					bitmapOrg.recycle();
					bitmapOrg = null;
				}
				be = (width > height ? width : height) / 640;
				be = be % 10 != 0 ? be + 1 : be;
				options.inSampleSize = be;
				try {
					bitmapOrg = BitmapFactory.decodeStream(fiStream, null, options);
				} catch(OutOfMemoryError er2) {
					return null;
				}
			}
			return bitmapOrg;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据源图片生成压缩图片
	 * @param srcFilePath 源图片文件路径
	 * @param distFilePath 目标压缩文件的保存路径
	 * @param toSize 目标大小
	 * @return 是否成功
	 */
	public static boolean resizePic(String srcFilePath, String distFilePath, int toSize) {
		Bitmap bitmap = resizePic(srcFilePath,toSize);
		if (bitmap == null) {
			return false;
		} else {
			// 图片压缩后的图片质量，当前固定为80
			int compressQuality4Resize = 80;
			FileOutputStream outStream = null;
			try {
				File distFile = new File(distFilePath);
				outStream = new FileOutputStream(distFile);
				boolean flag = bitmap.compress(CompressFormat.JPEG, compressQuality4Resize, outStream);
				return flag;
			} catch (Exception e) {
				return false;
			} finally {
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					outStream = null;
				}
			}
		}
	}
	
	public static int getStatusBarHeight(Context context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return -100;
        }
        
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar + 3;  
    } 
	
}
