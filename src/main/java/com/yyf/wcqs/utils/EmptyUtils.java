package com.yyf.wcqs.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Author: tang_botao
 * DateTime: 13-12-9 上午11:26
 * Explain:校验为空的工具
 */
public class EmptyUtils {

	public static boolean isEmpty(Object obj) {
		return obj == null;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}


	public static boolean isEmpty(Object[] objects) {
		return objects == null || objects.length == 0;
	}

	public static boolean isNotEmpty(Object[] objects) {
		return !isEmpty(objects);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(String str, boolean isTrim) {
		if (isTrim && str != null) {
			str = str.trim();
		}
		return isEmpty(str);
	}

	public static boolean isNotEmpty(String str, boolean isTrim) {
		return !isEmpty(str, isTrim);
	}

	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.size() == 0;
	}

	public static boolean isNotEmpty(Collection collection) {
		return !isEmpty(collection);
	}

	public static boolean isEmpty(Map map) {
		return map == null || map.size() == 0;
	}

	public static boolean isNotEmpty(Map map) {
		return !isEmpty(map);
	}
}
