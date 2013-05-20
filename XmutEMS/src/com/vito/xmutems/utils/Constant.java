package com.vito.xmutems.utils;

import java.io.File;

public class Constant {

	public static final String BASE_DOMAIN 	= "http://jxgl.xmut.edu.cn/";
	public static final String MAIN_URL	 	= "xs_main.aspx?xh=";
	public static final String USER_COOKIE 	= "USER_COOKIE";

	public static final boolean DEBUG 					= true;
	public static final boolean PERSISTLOG				= false;
	public static final String   APP_NAME					= "XmutEMS";
	public static 		  String   EXTERNAL_DIR 			= Util.getExternalStoragePath()+File.separator+APP_NAME;
	public static  	  String   CACHE_DIR 				= EXTERNAL_DIR+File.separator+"cache";
	public static  	  String   LOGS_DIR 				= EXTERNAL_DIR+File.separator+"logs";
	public static  	  String   FILES_DIR 				= EXTERNAL_DIR+File.separator+"files";
	public static  	  String   DATA_DIR 				= EXTERNAL_DIR+File.separator+"data";
	
}
