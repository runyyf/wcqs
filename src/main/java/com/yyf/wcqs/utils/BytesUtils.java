package com.yyf.wcqs.utils;

import java.net.Inet4Address;

/**
 * Author: tang_botao
 * DateTime: 14-5-8 上午9:54
 * Explain:
 */
public class BytesUtils {

	/**
	 * IP V4 地址转为字节数组
	 *
	 * @param ipAddressV4 正规的IP V4 地址，如 192.168.100.78
	 * @return 长度为4的字节数组
	 */
	public static byte[] ipAddressV4ToBytesL4(String ipAddressV4) {
		if (ipAddressV4 == null || ipAddressV4.trim().length() == 0) {
			return null;
		}
		try {
			return Inet4Address.getByName(ipAddressV4).getAddress();
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddressV4 + " is invalid IP");
		}
	}

	/**
	 * 字节数组转化为IPV4地址字符串
	 *
	 * @param bytesL4 字节数组,长度为4
	 * @return IPV4地址字符串，如：192.168.100.78
	 */
	public static String bytesL4ToIpAddressV4(byte[] bytesL4) {
		StringBuilder sb = new StringBuilder();
		sb.append(bytesL4[0] & 0xFF).append('.');
		sb.append(bytesL4[1] & 0xFF).append('.');
		sb.append(bytesL4[2] & 0xFF).append('.');
		sb.append(bytesL4[3] & 0xFF);
		return sb.toString();
	}

	/**
	 * 7字节的数字转为7字节的数组
	 *
	 * @param num int
	 * @return 4字节数组
	 */
	public static byte[] numToBytesL7(long num) {
		byte[] bytes = new byte[7];
		bytes[0] = (byte) (num >>> 48);
		bytes[1] = (byte) (num >>> 40);
		bytes[2] = (byte) (num >>> 32);
		bytes[3] = (byte) (num >>> 24);
		bytes[4] = (byte) (num >>> 16);
		bytes[5] = (byte) (num >>> 8);
		bytes[6] = (byte) (num);
		return bytes;
	}

	/**
	 * 64位的Long转为字节数组，占8字节
	 *
	 * @param num long
	 * @return 8字节数组
	 */
	public static byte[] longToBytesL8(long num) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) (num >>> 56);
		bytes[1] = (byte) (num >>> 48);
		bytes[2] = (byte) (num >>> 40);
		bytes[3] = (byte) (num >>> 32);
		bytes[4] = (byte) (num >>> 24);
		bytes[5] = (byte) (num >>> 16);
		bytes[6] = (byte) (num >>> 8);
		bytes[7] = (byte) (num);
		return bytes;
	}

	/**
	 * 32位的Int转为字节数组，占4字节
	 *
	 * @param num int
	 * @return 4字节数组
	 */
	public static byte[] numToBytesL4(long num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (num >>> 24);//取最高8位放到0下标
		bytes[1] = (byte) (num >>> 16);//取次高8为放到1下标
		bytes[2] = (byte) (num >>> 8); //取次低8位放到2下标
		bytes[3] = (byte) (num);      //取最低8位放到3下标
		return bytes;
	}

	/**
	 * 32位的Int转为字节数组，实际占2字节
	 *
	 * @param num int
	 * @return 2字节数组
	 */
	public static byte[] numToBytesL2(long num) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (num >>> 8); //取次低8位放到0下标
		bytes[1] = (byte) (num);      //取最低8位放到1下标
		return bytes;
	}

	/**
	 * 将最多4字节的byte数组转成一个int值
	 *
	 * @param bytes 4字节的byte数组,可小于4字节
	 * @return int数
	 */
	public static int bytesL4ToInt(byte[] bytes) {
		byte[] a = new byte[4];
		int i = a.length - 1;
		int j = bytes.length - 1;
		for (; i >= 0; i--, j--) {//从b的尾部(即int值的低位)开始copy数据
			if (j >= 0) {
				a[i] = bytes[j];
			} else {
				a[i] = 0;//如果b.length不足4,则将高位补0
			}
		}
		int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
		int v1 = (a[1] & 0xff) << 16;
		int v2 = (a[2] & 0xff) << 8;
		int v3 = (a[3] & 0xff);
		return v0 + v1 + v2 + v3;
	}

	/**
	 * 将最多2字节的byte数组转成一个int值
	 *
	 * @param bytes 2字节的byte数组,可小于4字节
	 * @return int数
	 */
	public static int bytesL2ToInt(byte[] bytes) {
		byte[] a = new byte[2];
		int i = a.length - 1;
		int j = bytes.length - 1;
		for (; i >= 0; i--, j--) {//从b的尾部(即int值的低位)开始copy数据
			if (j >= 0) {
				a[i] = bytes[j];
			} else {
				a[i] = 0;//如果b.length不足4,则将高位补0
			}
		}
		int v2 = (a[0] & 0xff) << 8;
		int v3 = (a[1] & 0xff);
		return  v2 + v3;
	}

	/**
	 * 将最多8字节的byte数组转成一个long值
	 *
	 * @param bytes 8字节的byte数组，可小于8字节
	 * @return int数
	 */
	public static long bytesL8ToLong(byte[] bytes) {
		long[] a = new long[8];
		int i = a.length - 1;
		int j = bytes.length - 1;
		for (; i >= 0; i--, j--) {//从b的尾部(即int值的低位)开始copy数据
			if (j >= 0) {
				a[i] = bytes[j];
			} else {
				a[i] = 0;//如果b.length不足8,则将高位补0
			}
		}
		//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
		long v0 = (a[0] & 0xff) << 56;
		long v1 = (a[1] & 0xff) << 48;
		long v2 = (a[2] & 0xff) << 40;
		long v3 = (a[3] & 0xff) << 32;
		long v4 = (a[4] & 0xff) << 24;
		long v5 = (a[5] & 0xff) << 16;
		long v6 = (a[6] & 0xff) << 8;
		long v7 = (a[7] & 0xff);
		return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
	}

	/**
	 * 16进制字符串转为byte数组,字符串长度应该为2的倍数否则会丢失最后一个
	 *
	 * @param hexString 16进制字符串
	 * @return byte数组，大小为字符串长度的一半
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return bytes;
	}

	/**
	 * char 字符转为byte
	 *
	 * @param c 字符
	 * @return byte，1字节
	 */
	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * byte数组转为16进制字符串
	 *
	 * @param bytes byte数组
	 * @return 16进制字符串，长度为数组大小的两倍
	 */
	public static String bytesToHexString(byte[] bytes) {

		StringBuilder stringBuilder = new StringBuilder("");
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		for (byte b : bytes) {
			int v = b & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static void main(String... args) {
		byte[] bytes = ipAddressV4ToBytesL4("192.168.100.78");
		for (byte b : bytes) {
			System.out.println(b);
		}
		System.out.println(bytesL4ToIpAddressV4(bytes));

		byte[] intBytes = numToBytesL4(Integer.MIN_VALUE);
		for (byte b : intBytes) {
			System.out.println(b);
		}
		System.out.println(bytesL4ToInt(intBytes));
		System.out.println("#######################");
		String str = "0abcffff";
		byte[] strBytes = hexStringToBytes(str);
		for (byte b : strBytes) {
			System.out.println(b);
		}
		System.out.println(bytesToHexString(strBytes));
	}

	/**
	 * 比较两个字节数字是否相等
	 *
	 * @param bytes1 字节数组1
	 * @param bytes2 字节数组2
	 * @return 是否相等
	 */
	public static boolean equalsBytes(byte[] bytes1, byte[] bytes2) {
		if (bytes1 == bytes2) {
			return true;
		}
		if (bytes1 == null) {
			return false;
		}
		if (bytes2 == null) {
			return false;
		}
		if (bytes1.length != bytes2.length) {
			return false;
		}
		for (int i = 0; i < bytes1.length; i++) {
			if (bytes1[i] != bytes2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将数字转为2字节数组并存入指令位置
	 *
	 * @param bytes  数组
	 * @param num    数字
	 * @param offset 存放数组偏移量
	 */
	public static void putNumberToBytesL2(long num, byte[] bytes, int offset) {
		byte[] bytesL2 = numToBytesL2(num);
		System.arraycopy(bytesL2, 0, bytes, offset, 2);
	}


	/**
	 * 将数字转为4字节数组并存入指令位置
	 *
	 * @param bytes  数组
	 * @param num    数字
	 * @param offset 存放数组偏移量
	 */
	public static void putNumberToBytesL4(long num, byte[] bytes, int offset) {
		byte[] bytesL4 = numToBytesL4(num);
		System.arraycopy(bytesL4, 0, bytes, offset, 4);
	}

	/**
	 * @Author: yuyf
	 * @Description: 截取数组
	 * @Date: Created in 15:58 2018/5/8
	*/
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		return bs;
	}
}
