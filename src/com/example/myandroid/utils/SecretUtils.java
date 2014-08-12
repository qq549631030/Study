package com.example.myandroid.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.util.Base64;

public class SecretUtils {
	/**
	 * 基于口令的对称加密
	 * 
	 * @param keyword
	 *            口令
	 * @param data
	 *            　要加密的数据
	 * @return　加密后的数据
	 */
	public static byte[] secretEncrypt(String keyword, byte[] data) {

		// 将要加密的数据传递进去，返回加密后的数据
		byte[] results = null;
		try {
			// 实例化工具
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			// 使用该工具将基于密码的形式生成Key
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(new PBEKeySpec(keyword.toCharArray()));
			PBEParameterSpec parameterspec = new PBEParameterSpec(new byte[] {
					1, 2, 3, 4, 5, 6, 7, 8 }, 1000);
			// 初始化加密操作，同时传递加密的算法
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterspec);
			// 先对数据进行base64加密
			byte[] base64Data = Base64.encode(data, Base64.DEFAULT);
			results = cipher.doFinal(base64Data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * 基于口令的对称解密
	 * 
	 * @param keyword
	 *            口令(要与加密口令一致)
	 * @param base64Data
	 *            要解密的数据
	 * @return　解密后的数据
	 */
	public static byte[] secretDecrypt(String keyword, byte[] base64Data) {
		byte[] result = null;
		try {
			// 实例化工具
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			// 使用该工具将基于密码的形式生成Key
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(new PBEKeySpec(keyword.toCharArray()));
			PBEParameterSpec parameterspec = new PBEParameterSpec(new byte[] {
					1, 2, 3, 4, 5, 6, 7, 8 }, 1000);
			// 初始化解密操作，同时传递解密的算法
			cipher.init(Cipher.DECRYPT_MODE, key, parameterspec);
			result = cipher.doFinal(base64Data);
			// 对数据进行base64解密
			result = Base64.decode(result, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
