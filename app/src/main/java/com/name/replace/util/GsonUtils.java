package com.name.replace.util;

import java.lang.reflect.Type;
import com.google.gson.Gson;

/**
 * 使用Gson进行json的转换
 * 
 * @author Lee
 */
public class GsonUtils {

	public static <T> T parseJSON(String json, Class<T> clazz) {
		Gson gson = new Gson();
		T info = gson.fromJson(json, clazz);
		return info;
	}
	
	public static <T> T parseJSON(String json, Type type) {
		Gson gson = new Gson();
		T info = gson.fromJson(json, type);
		return info;
	}
	
	/**
	 * Type type = new 
			TypeToken&lt;ArrayList&lt;TypeInfo>>(){}.getType();
	   <br>Type所在的包：java.lang.reflect
	   <br>TypeToken所在的包：com.google.gson.reflect.TypeToken	
	 * @param jsonArr
	 * @param type
	 * @return
	 */
	public static <T> T parseJSONArray(String jsonArr, Type type) {
		Gson gson = new Gson();
		T infos = gson.fromJson(jsonArr, type);
		return infos;
	}

	/*
	 * @description:把对象数据解析成Gson可读的json格式
	 *
	 * @param object:需要解析成json格式的数据集,classParam类对应的一个实例对象
	 *
	 * @param c:数据对应的Class<?>
	 *
	 * @return String:解析成功后返回的结果
	 */
	public static String toJson(Object object, Class<?> c) {
		Gson gson = new Gson();
		return gson.toJson(object, c);
	}
	
	private GsonUtils(){}

}
