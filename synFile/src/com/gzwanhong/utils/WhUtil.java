package com.gzwanhong.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gzwanhong.mapper.UserMapper;

public class WhUtil {
	public static String YYYY_MM_DD = "yyyy-MM-dd";
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static String YYYY_MM_DD_HH_MM_SSS = "yyyy-MM-dd HH:mm:sss";
	public static String YYYYMMDDHHMMSSS = "yyyyMMddHHmmsss";
	private static final char SEPARATOR = '_';

	public static Integer toInteger(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			if (obj.toString().contains(".")) {
				return Double.valueOf(obj.toString()).intValue();
			} else {
				return Integer.valueOf(obj.toString());
			}
		}
	}

	public static Float toFloat(Object obj) {
		if (obj == null) {
			return 0.0F;
		} else {
			return Float.valueOf(obj.toString());
		}
	}

	public static Long toLong(Object obj) {
		if (obj == null) {
			return 0L;
		} else {
			if (obj.toString().contains(".")) {
				return Double.valueOf(obj.toString()).longValue();
			} else {
				return Long.valueOf(obj.toString());
			}
		}
	}

	public static Double toDouble(Object obj) {
		if (obj == null) {
			return 0D;
		} else {
			return Double.valueOf(obj.toString());
		}
	}

	public static Integer doubleToInteger(Double val) {
		Integer result = 0;

		if (val != null) {
			String str = val.toString();
			str = str.substring(0, str.indexOf("."));
			result = Integer.valueOf(str);
		}

		return result;
	}

	public static Boolean toBoolean(Object obj) {
		if (obj == null) {
			return false;
		} else {
			return Boolean.valueOf(obj.toString());
		}
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	public static Date toDate(Object obj) {
		if (obj == null) {
			return null;
		} else {
			return (Date) obj;
		}
	}

	public static Timestamp toTimestamp(Date date) {
		if (date == null) {
			return null;
		} else {
			return new Timestamp(date.getTime());
		}
	}

	/**
	 * 判断是否为空 if null || "" return true else false;
	 *
	 * @return
	 */
	public static boolean isEmpty(String obj) {
		if (obj == null || "".equals(obj) || "undefined".equals(obj) || "null".equals(obj)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			if ("".equals(obj.toString()) || "undefined".equals(obj) || "null".equals(obj)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @param source
	 *            YYYY-MM-DD
	 * @return
	 */
	public static Date toDate(String source) {
		Date d = null;
		if (isEmpty(source)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static Date toDate(String source, String sdf) {
		Date d = null;
		if (isEmpty(source)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(sdf);
		try {
			d = dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * @param source
	 *            YYYY-MM-DD
	 * @return
	 */
	public static Timestamp toTimestamp(String source) {
		Timestamp timestamp = null;
		if (isEmpty(source)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = dateFormat.parse(source);
			timestamp = new Timestamp(d.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	/**
	 * 格式化对象，传来的数据不够位数就在前面被指定的字符
	 *
	 * @param obj
	 *            要格式化的对象
	 * @param length
	 *            指定的固定长度
	 * @param replaceChar
	 *            不够位数时在前面被的字符
	 * @return
	 */
	public static String fillUp(Object obj, int length, char replaceChar) {
		String resultl = "";

		if (obj != null) {
			String str = obj.toString();
			for (int i = length - str.length(); i > 0; i--) {
				resultl += replaceChar;
			}
			resultl += str;
		} else {
			return null;
		}

		return resultl;
	}

	/**
	 * 格式化对象，这个主要是给本项目的发票号码用的
	 *
	 * @param obj
	 * @return
	 */
	public static String fillUp(Object obj) {
		return fillUp(obj, 8, '0');
	}

	/**
	 * 把一个字条串首字母大写并在前面加个set
	 *
	 * @param name
	 * @return
	 */
	public static String toSet(String name) {
		if (name != null && name.length() > 0) {
			name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		return name;
	}

	/**
	 * 把一个字条串首字母大写并在前面加个get
	 *
	 * @param name
	 * @return
	 */
	public static String toGet(String name) {
		if (name != null && name.length() > 0) {
			name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		return name;
	}

	/**
	 * 去掉两边的空格
	 *
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (str != null) {
			return str.trim();
		} else {
			return str;
		}
	}

	/**
	 * 把一个日期类型的数据转成字符串
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.format(date);
		} else {
			return null;
		}
	}

	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	/**
	 * 把日期转为时分秒为零的数据
	 *
	 * @param date
	 * @return
	 */
	public static Date dateToDate(Date date) {
		return toDate(dateToString(date));
	}

	/**
	 * 获取指定文件的文本内容
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getFileText(String path) throws Exception {
		StringBuffer sb = new StringBuffer();
		File file = new File(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		String temp = null;
		while ((temp = br.readLine()) != null) {
			sb.append(temp + "\n");
		}
		br.close();

		return sb.toString();
	}

	public static void writeFileText(String path, String text) throws Exception {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
		bw.write(text);
		bw.flush();
		bw.close();
	}

	public static String toSqlIn(List<?> list) {
		String sql = "";

		Object obj = null;
		for (int i = 0; i < list.size(); i++) {
			obj = list.get(i);

			if (i == list.size() - 1) {
				if (obj instanceof String) {
					sql += "'" + obj + "'";
				} else {
					sql += obj;
				}
			} else {
				if (obj instanceof String) {
					sql += "'" + obj + "',";
				} else {
					sql += obj + ",";
				}
			}
		}

		return sql;
	}

	public <T> T mapToEntity(Map<String, Object> map, Class<T> clzz) throws Exception {
		T t = null;

		if (map != null) {
			t = clzz.newInstance();

			// 取出实体类的所有变量
			Field[] fields = clzz.getDeclaredFields();
			String fieldName = null;
			Class<?> fieldType = null;
			Method method = null;
			Object val = null;

			// 遍历变量，设置值
			for (Field field : fields) {
				fieldName = field.getName();
				fieldType = field.getType();

				val = map.get(fieldName);

				if (isEmpty(val)) {
					method = clzz.getMethod(toSet(fieldName), fieldType);

					if (fieldType == Integer.class) {
						method.invoke(t, toInteger(val));
					} else if (fieldType == Date.class) {
						method.invoke(t, toDate(val.toString()));
					} else if (fieldType == Double.class) {
						method.invoke(t, toDouble(val));
					} else {
						method.invoke(t, val);
					}
				}
			}

		}

		return t;
	}

	/**
	 * 把字符串的首字母变成小写
	 *
	 * @param str
	 * @return
	 */
	public static String lowerFirst(String str) {
		if (str != null) {
			str = str.substring(0, 1).toLowerCase() + str.substring(1);
		}

		return str;
	}

	/**
	 * 获取一个配置文件里的一个值
	 *
	 * @param fileName
	 * @param keyName
	 * @return
	 * @throws IOException
	 */
	public static String getPropertiesValue(String fileName, String keyName) throws IOException {
		Properties prop = new Properties();
		InputStream is = WhUtil.class.getClassLoader().getResourceAsStream(fileName);
		prop.load(is);
		return prop.getProperty(keyName);
	}

	/**
	 * 获取指定路径下的一个txt文件，并把它后缀名改为.doing
	 *
	 * @param filePath
	 * @return
	 */
	public static synchronized File getFileFromDir(String filePath) {
		File file = null;
		File uploadFileDir = new File(filePath);

		FilenameFilter ff = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".txt")) {
					return true;
				}

				return false;
			}
		};
		File[] files = uploadFileDir.listFiles(ff);

		if (files != null && files.length > 0) {
			file = files[0];
			String fileName = file.getPath() + ".doing";
			File newFile = new File(fileName);
			file.renameTo(newFile);
			file = newFile;
		}

		return file;
	}

	/**
	 * 把驼峰式的名字转成下划线式的
	 *
	 * @param inputString
	 * @return
	 */
	public static String toUnderLine(String inputString) {

		if (inputString == null)
			return null;
		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < inputString.length(); i++) {
			char c = inputString.charAt(i);

			boolean nextUpperCase = true;

			if (i < (inputString.length() - 1)) {
				nextUpperCase = Character.isUpperCase(inputString.charAt(i + 1));
			}

			if ((i >= 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0)
						sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 将驼峰字段转成属性字符串<br>
	 * (例:branch_no -> branchNo )<br>
	 *
	 * @param inputString
	 *            输入字符串
	 * @return
	 */
	public static String toCamelCaseString(String inputString) {
		return toCamelCaseString(inputString, false);
	}

	public static void copyBean(Object target, Object source) throws Exception {
		copyBean(target, source, new HashMap<String, Boolean>());
	}

	/**
	 * 将驼峰字段转成属性字符串<br>
	 * (例:branch_no -> branchNo )<br>
	 *
	 * @param inputString
	 *            输入字符串
	 * @param firstCharacterUppercase
	 *            是否首字母大写
	 * @return
	 */
	public static String toCamelCaseString(String inputString, boolean firstCharacterUppercase) {
		if (inputString == null)
			return null;
		StringBuilder sb = new StringBuilder();
		boolean nextUpperCase = false;
		for (int i = 0; i < inputString.length(); i++) {
			char c = inputString.charAt(i);

			switch (c) {
			case '_':
			case '-':
			case '@':
			case '$':
			case '#':
			case ' ':
			case '/':
			case '&':
			case '*':
				if (sb.length() > 0) {
					nextUpperCase = true;
				}
				break;

			default:
				if (nextUpperCase) {
					sb.append(Character.toUpperCase(c));
					nextUpperCase = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
				break;
			}
		}

		if (firstCharacterUppercase) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 *
	 * @param target
	 * @param source
	 * @param notMap
	 *            设置不复制的变量名
	 * @throws Exception
	 */
	public static void copyBean(Object target, Object source, Map<String, Boolean> notMap) throws Exception {
		if (WhUtil.isEmpty(source)) {
			throw new WhException("复制对象时源为空");
		}
		if (WhUtil.isEmpty(source)) {
			throw new WhException("复制对象时目标为空");
		}

		// 获取源的类型、变量、方法
		Class<?> sourceClass = source.getClass();
		Method[] sourceMethods = sourceClass.getDeclaredMethods();

		// 获取目标的类型与变量
		Class<?> targetClass = target.getClass();
		Method[] targetMethods = targetClass.getDeclaredMethods();

		// 把目前的方法处理成map
		Map<String, Method> targetMethodMap = new HashMap<String, Method>();
		for (Method m : targetMethods) {
			targetMethodMap.put(m.getName(), m);
		}

		// 循环源类型的变量，往目标里设值
		Method setMethod = null;
		String setName = null;
		String getName = null;
		Object val = null;
		for (Method sourceMethod : sourceMethods) {
			// 只处理源的get方法
			if (sourceMethod.getName().startsWith("get")) {
				getName = sourceMethod.getName();
				setName = "s" + getName.substring(1);

				// 看否有设置不处理
				if (!WhUtil.isEmpty(notMap.get(getName))) {
					continue;
				}

				// 看目标有没对应的set方法，没有的话就跳过不处理
				setMethod = targetMethodMap.get(setName);
				if (!WhUtil.isEmpty(setMethod)) {
					// 获取值
					val = sourceMethod.invoke(source);
					// 设值
					setMethod.invoke(target, val);
				} else {
					continue;
				}
			}
		}
	}

	// 获取上个月的第一天
	public static String lastMonthFristDay() {
		Calendar calendar = Calendar.getInstance();// 获取当前日期
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return WhUtil.dateToString(calendar.getTime());
	}

	// 获取上个月的最后一天
	public static String lastMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		return WhUtil.dateToString(calendar.getTime());
	}

	// 获取当前月第一天：
	public static String currentMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return WhUtil.dateToString(calendar.getTime());
	}

	// 获取当前月最后一天
	public static String currentMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return WhUtil.dateToString(calendar.getTime());
	}

	/**
	 * 获取mybatis 的 mapper xml 配置文件里的SQL字符串
	 *
	 * @param mapperClass
	 * @param idStr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String getSqlFromMapper2(Class<?> mapperClass, String idStr) throws Exception {
		String clzzName = mapperClass.getSimpleName();
		String sqlStr = "";
		// 以UserMapper类为目标，先获取到mapper包的路径
		File userMapper = new File(UserMapper.class.getResource("").getPath());
		// 组装出该mapper的路径
		String mapperXmlPath = userMapper.getPath() + File.separator + "xml" + File.separator + clzzName + ".xml";
		mapperXmlPath = mapperXmlPath.replaceAll("%20", " ");

		Document doc = null;
		Element root = null;
		List<Element> nodes = null;
		Element node = null;
		String id = null;

		SAXReader reader = new SAXReader();
		reader.setValidation(false);
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		File mapper = new File(mapperXmlPath);
		doc = reader.read(mapper);
		root = doc.getRootElement();
		nodes = root.elements();

		for (int i = 0; i < nodes.size(); i++) {
			node = nodes.get(i);
			if ("select".equals(node.getName().toLowerCase()) || "update".equals(node.getName().toLowerCase())
					|| "delete".equals(node.getName().toLowerCase()) || "insert".equals(node.getName().toLowerCase())) {
				id = node.attributeValue("id");

				if (idStr.equals(id)) {
					sqlStr = node.getText();
					break;
				}
			}
		}

		return sqlStr;
	}

	/**
	 * 获取mybatis 的 mapper xml
	 * 配置文件里的SQL字符串，这个方法没有用解释xml的方式，而是直接一行行的读，遇到指写id的标签时就取出sql字符串
	 *
	 * @param mapperClass
	 * @param idStr
	 * @return
	 * @throws Exception
	 */
	public static String getSqlFromMapper(Class<?> mapperClass, String idStr) throws Exception {
		String clzzName = mapperClass.getSimpleName();
		String sqlStr = "";
		// 以UserMapper类为目标，先获取到mapper包的路径
		File userMapper = new File(UserMapper.class.getResource("").getPath());
		// 组装出该mapper的路径
		String mapperXmlPath = userMapper.getPath() + File.separator + "xml" + File.separator + clzzName + ".xml";
		mapperXmlPath = mapperXmlPath.replaceAll("%20", " ");

		File mapper = new File(mapperXmlPath);
		BufferedReader br = new BufferedReader(new FileReader(mapper));

		String str = null;
		Boolean isGet = false;
		while ((str = br.readLine()) != null) {
			if (str.contains("\"" + idStr + "\"")) {
				isGet = true;
				continue;
			}

			if (isGet && (str.contains("</select>") || str.contains("</update>") || str.contains("</delete>")
					|| str.contains("</insert>"))) {
				isGet = false;
				continue;
			}

			if (isGet && !str.contains("<![CDATA[") && !str.contains("]]>")) {
				sqlStr += str + "\n";
			}
		}

		br.close();

		return sqlStr;
	}

	/**
	 * 根据IP返回所在地的地名
	 *
	 * @param ip
	 * @return
	 */
	public static String ipLookup(String ip) {

		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String httpUrl = "http://apis.baidu.com/apistore/iplookupservice/iplookup" + "?ip=" + ip;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", "46ffcb7e2f9d7a3f4d6538df44abf4a3");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			Map<String, Object> resultMap = JsonUtil.jsonToMap(result, false);
			for (String key : resultMap.keySet()) {
				Object o = resultMap.get(key);
				o = new String(o.toString());
			}

			result = JsonUtil.beanToJson(resultMap.get("retData"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
