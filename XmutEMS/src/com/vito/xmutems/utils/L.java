package com.vito.xmutems.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;


public class L {
	private static final String TAG				= "XmutEMS";
	/**
	 * log file name
	 */
	public static final String  PERSIST_PATH	= Constant.LOGS_DIR + File.separator+ "log";
	
	public static void p(String log)
	{
		if(Constant.DEBUG)
		{

			System.out.println(log);
		}
		L.d(log);
	}
	
	public static void v(String text)
	{
		v(TAG,text);
	}
	public static void v(String tag, String text)
	{
		print(tag, text, Log.VERBOSE);
	}
	
	public static void d(String text)
	{
		d(TAG, text);
	}
	public static void d(String tag, String text)
	{
		print(tag, text, Log.DEBUG);
	}
	
	public static void i(String text)
	{
		i(TAG, text);
	}
	public static void i(String tag, String text)
	{
		print(tag, text, Log.INFO);
	}
	
	public static void w(String text)
	{
		w(TAG, text);
	}
	public static void w(String tag, String text)
	{
		print(tag, text, Log.WARN);
	}
	public static void w(String tag, String text,Throwable throwable)
	{
		print(tag, text+"#message:"+throwable.getMessage(),Log.WARN);
		StackTraceElement[] elements = throwable.getStackTrace();
		for(StackTraceElement e : elements)
		{
			print(tag, e.toString(), Log.WARN);
		}
	}
	public static void e(String text)
	{
		e(TAG, text);
	}
	public static void e(String tag, String text)
	{
		print(tag, text, Log.ERROR);
	}
	
	public static void e(String tag, String text,Throwable throwable)
	{
		print(tag, text+"#message:"+throwable.getMessage(),Log.ERROR);
		StackTraceElement[] elements = throwable.getStackTrace();
		for(StackTraceElement e : elements)
		{
			print(tag, e.toString(), Log.ERROR);
		}
	}
	private static synchronized void print(final String tag,final String text, final int level){
		if(Util.isEmpty(text))
		{
			return;
		}
		if(Constant.DEBUG)
		{

			switch (level) {
			case Log.VERBOSE:
					Log.v(tag, text);
				break;
			case Log.DEBUG:
					Log.d(tag, text);
				break;
			case Log.INFO:
					Log.i(tag, text);
				break;
			case Log.WARN:
					Log.w(tag, text);
				break;
			case Log.ERROR:
					Log.e(tag, text);
				break;
			}
		}
		if(Constant.PERSISTLOG)
		{
			ThreadPoolUtil.execute(new Runnable() {
				
				@Override
				public void run() {
					writeLog(text, level);
				}
			});
			
		}
	}
	
	/**
	 * write the log into the file
	 * @param text
	 * @param level
	 */
	private static synchronized void writeLog(String text, int level )
	{
		StringBuilder sb = new StringBuilder();
		sb.append("["+DateUtil.toTime(System.currentTimeMillis(),DateUtil.DATE_FORMATE_HOUR_MINUTE_SECOND)+"]");
		switch (level) {
		case Log.VERBOSE:
			sb.append("[VERBOSE]\t");
			break;
		case Log.DEBUG:
			sb.append("[DEBUG]\t");
			break;
		case Log.INFO:
			sb.append("[INFO ]\t");
			break;
		case Log.WARN:
			sb.append("[WARN ]\t");
			break;
		case Log.ERROR:
			sb.append("[ERROR]\t");
			break;
		}
		
		RandomAccessFile raf = null;
		try {
			String fileName = PERSIST_PATH+"_"+DateUtil.toTime(System.currentTimeMillis(), DateUtil.DATE_DEFAULT_FORMATE);
			File logFile = new File(fileName);
			if(!logFile.exists())
			{
				Util.initExternalDir(false);
				logFile.createNewFile();
			}
			raf = new RandomAccessFile(fileName, "rw");
			raf.seek(raf.length());
			raf.writeBytes(sb.toString()+text+"\r\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if(raf!=null)
			{
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
}
