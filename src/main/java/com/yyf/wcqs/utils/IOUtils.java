package com.yyf.wcqs.utils;

import java.io.*;

/**
 * Author: tang_botao
 * DateTime: 14-4-28 下午4:57
 * Explain:
 */
public class IOUtils extends org.apache.commons.io.IOUtils {

	public static boolean write2File(File file, byte[] bytes, int length) throws IOException {
		if (length > bytes.length) {
			length = bytes.length;
		}
		FileOutputStream fos = null;
		try {
			if (!file.exists() && !file.createNewFile()) {//没有权限创建文件
				return false;
			}
			fos = new FileOutputStream(file);
			fos.write(bytes, 0, length);
			fos.flush();
			return true;
		} finally {
			CloseableUtil.close(fos);
		}
	}

	/**
	 * 从输入流中读取所有的字节
	 *
	 * @param inputStream 输入流
	 * @return 字节数组
	 * @throws IOException IO异常
	 */
	public static byte[] readFully(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, length);
		}
		return byteArrayOutputStream.toByteArray();
	}
}
