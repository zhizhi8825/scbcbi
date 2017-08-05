package com.gzwanhong.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
	public static String encrypt(String encryptStr){
		return DigestUtils.md5Hex(encryptStr);
	}
	
	public static String randonLetter(Integer num){
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String value = "";
		for(int i =0 ;i<num;i++){
			value += chars.charAt((int)(Math.random() * 26));
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(randonLetter(6));
	}
}
