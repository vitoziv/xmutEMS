package com.vito.xmutems.utils;
import it.sauronsoftware.base64.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 * Base64编码解码工具
 * @author Administrator
 *
 */
public class Base64Util {
	/**
	 * 将对象序列化成base64格式的字符串
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String objectToString(Serializable obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		String base64 = new String(Base64.encode(baos.toByteArray()));
		oos.close();
		baos.close();
		return base64;
	}

	/**
	 * 将base64格式的字符串反序列化成对象
	 * @param base64
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object stringToObject(String base64) throws IOException,
			ClassNotFoundException {
		// 对Base64格式的字符串进行解码
		byte[] base64Bytes = Base64.decode(base64.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		// 从ObjectInputStream中读取Product对象
		return ois.readObject();
		
	}
}
