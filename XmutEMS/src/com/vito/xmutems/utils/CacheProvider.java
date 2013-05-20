package com.vito.xmutems.utils;

import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 缓存提供程序
 * @author Administrator
 *
 */
public class CacheProvider {
	private static String SharedPreferencesName = "xmut.cache";

	public static void put(Context context, String key, Serializable val) {
		String base64;
		try {
			base64 = Base64Util.objectToString(val);
			putToDisk(context, key, base64);
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println("保存缓存对象失败:" + e.getMessage());
		}
	}

	public static Object get(Context context, String key) {
		Object obj = null;
		try {
			obj = Base64Util.stringToObject(getFromDisk(context, key));
		} catch (Exception e) {
			//System.out.println("获取缓存对象失败:" + e.getMessage());
		}
		return obj;
	}

	public static void remove(Context context, String key) {
		removeFromDisk(context, key);
	}

	private static void putToDisk(Context context, String key, String value) {

		SharedPreferences sp = context.getSharedPreferences(
				SharedPreferencesName, Context.MODE_PRIVATE);
		
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private static String getFromDisk(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				SharedPreferencesName, Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	private static void removeFromDisk(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				SharedPreferencesName, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}
}
