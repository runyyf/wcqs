package com.yyf.wcqs.utils;

import java.io.Closeable;
import java.util.Date;

/**
 * Author: tang_botao
 * DateTime: 14-4-22 下午2:21
 * Explain:
 */
public class CloseableUtil {
	public static void close(Closeable... closes) {
		if (closes == null || closes.length == 0) {
			return;
		}
		try {
			for (Closeable closeable : closes) {
				if (closeable != null) {
					closeable.close();
				}
			}
		} catch (Exception e) {
			System.out.println(new Date().toString() + "CloseableUtil.close(...)异常:" + e.getMessage());
		}
	}
}
