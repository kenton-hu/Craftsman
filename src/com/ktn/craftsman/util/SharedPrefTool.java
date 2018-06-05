package com.ktn.craftsman.util;

import java.util.ArrayList;
import java.util.List;

import com.ktn.craftsman.App;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具类.
 * 
 * 说明：
 * spKey=SharedPreferences的key
 * vKey=spKey对应的SharedPreferences中存储的key
 * 
 *
 */
public class SharedPrefTool {

	/**
	 * 获取单个值
	 * @param spKey
	 * @param vKey
	 * @param defaultV 默认值
	 * @return
	 */
	public static String getValue(String spKey, String vKey, String defaultV) {
		SharedPreferences apkSp = App.getInstance().getSharedPreferences(spKey,0);
		return apkSp.getString(vKey, defaultV);
	}
	
	public static boolean getValue(String spKey, String vKey, Boolean defaultV) {
		SharedPreferences apkSp = App.getInstance().getSharedPreferences(spKey,0);
		return apkSp.getBoolean(vKey, defaultV);
	}
	
	/**
	 * 保存单个值
	 * @param spKey
	 * @param vKey
	 * @param value 欲保存的值
	 */
	public static void setValue(String spKey, String vKey, String value) {
		SharedPreferences sp = App.getInstance().getSharedPreferences(spKey, 0);
		Editor spedit = sp.edit();
		spedit.putString(vKey, value);
		spedit.commit();		
	}
	
	public static void setValue(String spKey, String vKey, boolean value) {
		SharedPreferences sp = App.getInstance().getSharedPreferences(spKey, 0);
		Editor spedit = sp.edit();
		spedit.putBoolean(vKey, value);
		spedit.commit();		
	}
	
	/**
	 * 根据一组vKey来获取数据
	 * @param spKey
	 * @param vKeys vKey的列表
	 * @return 值的列表，顺序与vKey列表对应
	 */
	public static List<String> getValueList(String spKey, List<String> vKeys) {
		List<String> strList = new ArrayList<String>();
		if (vKeys == null || vKeys.size() <= 0) {
			return strList;
		}
		SharedPreferences apkSp = App.getInstance().getSharedPreferences(spKey,0);
		for (int i = 0; i < vKeys.size(); i++) {
			String temp = apkSp.getString(vKeys.get(i), "");
			if (temp != null && !"".equals(temp)) {
				strList.add(temp);
			}
		}
		return strList;
	}

	/**
	 * 将数组存入SharedPreferences.
	 * @param spKey
	 * @param vKeys key的数组
	 * @param values 值的数组
	 */
	public static void setValueArray(String spKey, String[] vKeys,  String[] values) {
		if(vKeys != null && values != null && vKeys.length == values.length) {
			SharedPreferences sp = App.getInstance().getSharedPreferences(spKey, 0);
			Editor spedit = sp.edit();
			for (int i = 0; i < vKeys.length; i++) {
				spedit.putString(vKeys[i], values[i]);
			}
			spedit.commit();	
		}
	}
	
	/**
	 * 根据一组key获取一组value
	 * @param spKey 
	 * @param vKeys key的数组
	 * @return 值的数组
	 */
	public static String[] getValueArray(String spKey, String[] vKeys) {
		if (vKeys == null || vKeys.length <= 0) {
			return null;
		}
		
		String[] values = new String[vKeys.length];
		SharedPreferences apkSp = App.getInstance().getSharedPreferences(spKey,0);
		for (int i = 0; i < vKeys.length; i++) {
			values[i] = apkSp.getString(vKeys[i], "");
		}
		return values;
	}
	
}
