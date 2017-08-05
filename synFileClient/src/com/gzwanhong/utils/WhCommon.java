package com.gzwanhong.utils;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WhCommon {
	public static final Map<String, String> sqlMap = new HashMap<String, String>();
	public static final Properties config = new Properties();
	public static Boolean isOn = false; // 记录是否开启了同步功能，默认是关闭的
	public static Boolean isWorking = false; // 记录是否开启了同步功能，默认是关闭的
	public static String flag = null; // 记录本机标识
	public static String clientName = null; // 记录本机名字
	public static String backupDir = null; // 记录备份的目的目录
	public static String workTime = null; // 记录备份的目的目录
	public static Integer hours = null; // 记录查询多少小时内的变动文件
	public static List<Map<String, Date>> workTimeList; // 记录工作时间段
	public static Thread timerThead; // 时间定时器
	public static Thread backupThead; // 备份文件线程
	public static String webUrl = ""; // 服务器地址
	public static String webserviceUrl = "/service/serviceInterface?wsdl"; // webservice地址
	public static String downloadUrl = "/synFileOptionAction/downloadFile.action"; // 下载文件的action地址
	public static Boolean isDownAllFile = false; // 记录整站备份是否在进行
	public static List<Thread> downAllFileThread; // 记录整站备份的线程
	public static Integer theadCount; // 配置整站备份的线程数
	public static Thread getAllFileThead; // 整站备份时，去服务端取文件的线程

}
