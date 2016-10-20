package com.gzwanhong.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gzwanhong.utils.WhUtil;

/**
 * 根据实体类生成对应的mapper.xml文件
 * 
 * @author zaki
 * 
 */
public class CreateMapper {

	// 要生成mapper的实体集合
	// private static Class<?>[] clzzs = new Class<?>[] { Bill.class,
	// BillDetail.class, BillIn.class, Distribute.class, Enterprise.class,
	// Staff.class };
	// private static Class<?>[] clzzs = new Class<?>[] { Client.class,
	// Coupon.class, CouponStrategy.class, Department.class, Menu.class,
	// Record.class, RelationRoleMenu.class, Role.class, User.class };

	public static void main(String[] args) throws Exception {
		String domainPackage = "com.gzwanhong.domain";
		String mapperPackage = "com.gzwanhong.mapper";
		Field[] fields = null;
		String className = null;
		String fieldName = "";
		String fieldNameLine = "";
		StringBuffer xmlStr = new StringBuffer();
		String xmlPath = "";
		FileOutputStream fos = null;
		FileInputStream fis = null;
		BufferedReader br = null;
		File file = null;
		StringBuffer sqlMapConfig = new StringBuffer();
		sqlMapConfig
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>                                      \n");
		sqlMapConfig
				.append("<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"           \n");
		sqlMapConfig
				.append("\"http://mybatis.org/dtd/mybatis-3-config.dtd\">                                \n");
		sqlMapConfig
				.append("<configuration>                                                                 \n");
		sqlMapConfig
				.append("	<typeAliases>                                                                  \n");

		// 获取实体目录下的类
		String domainPath = System.getProperty("user.dir") + File.separator
				+ "src" + File.separator
				+ domainPackage.replace(".", File.separator);
		File dir = new File(domainPath);
		String[] fileNamess = dir.list();
		List<Class<?>> clzzs = new ArrayList<Class<?>>();
		Class<?> clzzTemp = null;
		for (String str : fileNamess) {
			if (!".svn".equals(str)) {
				clzzTemp = Class.forName(domainPackage + "."
						+ str.substring(0, str.indexOf(".")));
				clzzs.add(clzzTemp);
			}
		}

		// 循环实体
		for (Class<?> clzz : clzzs) {
			// 获取类名
			className = clzz.getSimpleName();
			// 获取变量集合
			fields = clzz.getDeclaredFields();

			String jdbcType = "";
			String columns = "";
			String insertVal = "";
			String insertVal2 = "";
			String updateVal = "";
			String updateVal2 = "";
			String resultMap = "    <resultMap type=\"" + clzz.getName()
					+ "\" id=\"" + WhUtil.lowerFirst(className) + "\"> \n";

			// 循环变量组装字符
			for (int i = 0; i < fields.length; i++) {
				fieldName = fields[i].getName();
				fieldNameLine = WhUtil.toUnderLine(fieldName);

				// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
				if (fields[i].toString().contains("static")) {
					continue;
				}

				// 设置resultMap
				if (fieldName.equals("id")) {
					resultMap += "    	<id property=\"" + fieldName
							+ "\" column=\"" + fieldNameLine + "\"/>\n";
				} else {
					resultMap += "    	<result property=\"" + fieldName
							+ "\" column=\"" + fieldNameLine + "\"/>\n";
				}

				if (fields[i].getType() == Date.class) {
					jdbcType = ",jdbcType=TIMESTAMP";
				} else if (fields[i].getType() == Integer.class
						|| fields[i].getType() == Double.class) {
					jdbcType = ",jdbcType=NUMERIC";
				} else {
					jdbcType = ",jdbcType=VARCHAR";
				}

				if (fieldName.equals("id") || fieldName.equals("createTime")) {
					if (WhUtil.isEmpty(columns)) {
						columns += fieldNameLine;
					} else {
						columns += "," + fieldNameLine;
					}

					if (WhUtil.isEmpty(insertVal)) {
						insertVal += "#{" + fieldName + jdbcType + "}";
					} else {
						insertVal += ",#{" + fieldName + jdbcType + "}";
					}

					if (WhUtil.isEmpty(insertVal2)) {
						insertVal2 += "#{item." + fieldName + jdbcType + "}";
					} else {
						insertVal2 += ",#{item." + fieldName + jdbcType + "}";
					}

					continue;
				}

				if (WhUtil.isEmpty(columns)) {
					columns += fieldNameLine;
				} else {
					columns += "," + fieldNameLine;
				}

				if (WhUtil.isEmpty(insertVal)) {
					insertVal += "#{" + fieldName + jdbcType + "}";
				} else {
					insertVal += ",#{" + fieldName + jdbcType + "}";
				}

				if (WhUtil.isEmpty(insertVal2)) {
					insertVal2 += "#{item." + fieldName + jdbcType + "}";
				} else {
					insertVal2 += ",#{item." + fieldName + jdbcType + "}";
				}

				if (WhUtil.isEmpty(updateVal)) {
					updateVal += fieldNameLine + " = #{" + fieldName + jdbcType
							+ "}";
				} else {
					updateVal += "," + fieldNameLine + " = #{" + fieldName
							+ jdbcType + "}";
				}

				if (WhUtil.isEmpty(updateVal2)) {
					updateVal2 += fieldNameLine + " = #{item." + fieldName
							+ jdbcType + "}";
				} else {
					updateVal2 += "," + fieldNameLine + " = #{item."
							+ fieldName + jdbcType + "}";
				}
			}

			// 设置resultMap尾
			resultMap += "    </resultMap>\n";

			xmlStr = new StringBuffer();
			xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>                                          \n");
			xmlStr.append("<!DOCTYPE mapper                                                                     \n");
			xmlStr.append("  PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"                                      \n");
			xmlStr.append("  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">                                   \n");
			xmlStr.append("<mapper namespace=\"" + mapperPackage + "."
					+ className + "Mapper\">                               \n");
			// xmlStr.append("	<cache></cache>                                                                     \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<sql id=\"dbName\"> "
					+ WhUtil.toUnderLine(className)
					+ " </sql>                                                     \n");
			xmlStr.append("	<sql id=\"columns\"> "
					+ columns
					+ " </sql>                                                        \n");
			xmlStr.append("	<sql id=\"insertVal\"> "
					+ insertVal
					+ " </sql>                                                      \n");
			xmlStr.append("	<sql id=\"insertVal2\"> "
					+ insertVal2
					+ " </sql>                                                     \n");
			xmlStr.append("	<sql id=\"updateVal\"> set "
					+ updateVal
					+ " </sql>                                                     \n");
			xmlStr.append("	<sql id=\"updateVal2\"> set "
					+ updateVal2
					+ " </sql>                                                    \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append(resultMap);
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<select id=\"queryById\" resultMap=\""
					+ WhUtil.lowerFirst(className)
					+ "\">                                        \n");
			xmlStr.append("		select * from                                                                   \n");
			xmlStr.append("		<include refid=\"dbName\" />                                                    \n");
			xmlStr.append("		where id =                                                                      \n");
			xmlStr.append("		#{id}                                                                           \n");
			xmlStr.append("	</select>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<select id=\"queryByIds\" resultMap=\""
					+ WhUtil.lowerFirst(className)
					+ "\">                                       \n");
			xmlStr.append("		select * from                                                                   \n");
			xmlStr.append("		<include refid=\"dbName\" />                                                    \n");
			xmlStr.append("		where id in                                                                     \n");
			xmlStr.append("		<foreach collection=\"list\" item=\"item\" open=\"(\" close=\")\"               \n");
			xmlStr.append("			separator=\",\">#{item}</foreach>                                           \n");
			xmlStr.append("	</select>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<insert id=\"save\">                                                                \n");
			xmlStr.append("		insert into                                                                     \n");
			xmlStr.append("		<include refid=\"dbName\" />                                                    \n");
			xmlStr.append("		(                                                                               \n");
			xmlStr.append("		<include refid=\"columns\" />                                                   \n");
			xmlStr.append("		) values                                                                        \n");
			xmlStr.append("		(                                                                               \n");
			xmlStr.append("		<include refid=\"insertVal\" />                                                 \n");
			xmlStr.append("		)                                                                               \n");
			xmlStr.append("	</insert>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<insert id=\"saveAll\">                                                             \n");
			xmlStr.append("		insert into <include refid=\"dbName\" /> (<include refid=\"columns\" />) values <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\"> (<include refid=\"insertVal2\" />) </foreach>\n");
			xmlStr.append("	</insert>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<delete id=\"removeById\">                                                          \n");
			xmlStr.append("		delete from                                                                     \n");
			xmlStr.append("		<include refid=\"dbName\" />                                                    \n");
			xmlStr.append("		where id = #{id}                                                                \n");
			xmlStr.append("	</delete>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<delete id=\"removeByIds\">                                                         \n");
			xmlStr.append("		delete from                                                                     \n");
			xmlStr.append("		<include refid=\"dbName\" />                                                    \n");
			xmlStr.append("		where id in                                                                     \n");
			xmlStr.append("		<foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\"           \n");
			xmlStr.append("			close=\")\">#{item}</foreach>                                               \n");
			xmlStr.append("	</delete>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<delete id=\"removeBySql\">                                   \n");
			xmlStr.append("		${sqlStr}                                                                       \n");
			xmlStr.append("	</delete>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<update id=\"update\">                                                              \n");
			xmlStr.append("		update                                                                          \n");
			xmlStr.append("		<include refid=\"dbName\" />                                                    \n");
			xmlStr.append("		<include refid=\"updateVal\" />                                                 \n");
			xmlStr.append("		where id = #{id}                                                                \n");
			xmlStr.append("	</update>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<update id=\"updateAll\">                                                           \n");
			xmlStr.append("		<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" close=\";\" separator=\";\">update <include refid=\"dbName\" /> <include refid=\"updateVal2\" /> where id = #{item.id}</foreach>");
			xmlStr.append("	</update>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<update id=\"updateBySql\">                                                         \n");
			xmlStr.append("		${sqlStr}                                                                       \n");
			xmlStr.append("	</update>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<select id=\"queryByExample\" resultMap=\""
					+ WhUtil.lowerFirst(className)
					+ "\">                                   \n");
			xmlStr.append("		${sqlStr}                                                                       \n");
			xmlStr.append("	</select>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<select id=\"queryBySql\" resultMap=\""
					+ WhUtil.lowerFirst(className)
					+ "\">                                       \n");
			xmlStr.append("		${sqlStr}                                                                       \n");
			xmlStr.append("	</select>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<select id=\"queryBySqlToMap\" resultType=\"map\">                                  \n");
			xmlStr.append("		${sqlStr}                                                                       \n");
			xmlStr.append("	</select>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<select id=\"queryCountByExample\" resultType=\"integer\">                          \n");
			xmlStr.append("		${sqlStr}                                                                       \n");
			xmlStr.append("	</select>                                                                           \n");
			xmlStr.append("                                                                                     \n");
			xmlStr.append("	<!-- 以上是自动生成的代码，自定义的查询什么的请写在下面 -->                         \n");

			xmlPath = System.getProperty("user.dir") + File.separator + "src"
					+ File.separator
					+ mapperPackage.replace(".", File.separator)
					+ File.separator + "xml" + File.separator + className
					+ "Mapper.xml";

			// 先看看有没该文件，有的话就把自定义的代码拿出来
			StringBuffer userStr = new StringBuffer();
			file = new File(xmlPath);
			if (file.exists()) {
				fis = new FileInputStream(file);
				br = new BufferedReader(new InputStreamReader(fis));
				Boolean canGet = false; // 记录是否取值，是自定义代码的话就取
				String strTemp = null;
				while ((strTemp = br.readLine()) != null) {
					if (canGet && !strTemp.contains("</mapper>")) {
						userStr.append(strTemp);
						if (!strTemp.contains("\n")) {
							userStr.append("\n");
						}
					}

					if (strTemp.contains("以上是自动生成的代码，自定义的查询什么的请写在下面")) {
						canGet = true;
					}
				}

				fis.close();
				xmlStr.append(userStr);
			}
			xmlStr.append("</mapper>             \n");

			System.out.println("已生成文件：" + xmlPath);

			fos = new FileOutputStream(new File(xmlPath));
			fos.write(xmlStr.toString().getBytes("utf8"));
			fos.flush();
			fos.close();

			// 生成mapper的java文件
			xmlStr = new StringBuffer();
			xmlStr.append("package " + mapperPackage
					+ ";                    \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("import java.util.List;                           \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("import " + domainPackage + "." + className
					+ ";                \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("public interface " + className
					+ "Mapper {                    \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public " + className
					+ " queryById(String id);               \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public List<" + className
					+ "> queryByIds(List<String> ids); \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public int save(" + className + " "
					+ className.toLowerCase() + ");                     \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public int saveAll(List<" + className
					+ "> list);            \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public int removeById(String id);               \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public int removeByIds(List<String> ids);       \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public int update(" + className + " "
					+ className.toLowerCase() + ");                   \n");
			xmlStr.append("                                                 \n");
			xmlStr.append("	public int updateAll(List<" + className
					+ "> list);          \n");
			xmlStr.append("}                                                \n");

			xmlPath = System.getProperty("user.dir") + File.separator + "src"
					+ File.separator
					+ mapperPackage.replace(".", File.separator)
					+ File.separator + className + "Mapper.java";
			System.out.println("已生成文件：" + xmlPath);

			fos = new FileOutputStream(new File(xmlPath));
			fos.write(xmlStr.toString().getBytes("utf8"));
			fos.flush();
			fos.close();

			// 生成sqlMapCofig.xml
			sqlMapConfig.append("		<typeAlias type=\"" + domainPackage + "."
					+ className + "\" alias=\"" + WhUtil.lowerFirst(className)
					+ "\" />\n");
		}

		sqlMapConfig
				.append("	</typeAliases>                                                                 \n");
		sqlMapConfig
				.append("</configuration>                                                                \n");

		xmlPath = System.getProperty("user.dir") + File.separator  + "config" + File.separator
				+ "sqlMapConfig.xml";

		fos = new FileOutputStream(new File(xmlPath));
		fos.write(sqlMapConfig.toString().getBytes("utf8"));
		fos.flush();
		fos.close();
		System.out.println("已生成文件：" + xmlPath);
	}
}
