package com.gzwanhong.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.monitor.FileAlterationMonitor;

public class WhCommon {
	public static final Map<String, String> sqlMap = new HashMap<String, String>();
	public static final Properties config = new Properties();
	public static Boolean isOn = false; // 记录是否开启了同步功能，默认是关闭的
	public static FileAlterationMonitor monitor = new FileAlterationMonitor(3); // 文件目录监控对象
	public static List<Integer> watchIdList = null;
	public static Map<String, List<Map<String, Object>>> checkFileMap = null;
	public static Map<String, Thread> checkFileThreadMap = new HashMap<String, Thread>();
}
