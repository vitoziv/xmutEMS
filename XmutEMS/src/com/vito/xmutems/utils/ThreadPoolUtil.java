/**
 * 
 */
package com.vito.xmutems.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ThreadPoolUtil {
	private static final ExecutorService pool;
	
	static {
		pool = Executors.newCachedThreadPool();
//		pool = Executors.newFixedThreadPool(4);
//		pool = Executors.newSingleThreadExecutor();
//		pool = Executors.newScheduledThreadPool(4);
	}
	
	/**
	 * 
	 * @param command
	 */
	public static void execute(Runnable command) {
		pool.execute(command);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param task
	 * @return
	 */
	public static <T> Future<T> submit(Callable<T> task) {
		return pool.submit(task);
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	public static Future<?> submit(Runnable task) {
		return pool.submit(task);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param task
	 * @param result
	 * @return
	 */
	public static <T> Future<T> submit(Runnable task, T result) {
		return pool.submit(task, result);
	}
}
