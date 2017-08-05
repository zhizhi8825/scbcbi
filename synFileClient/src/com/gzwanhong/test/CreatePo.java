package com.gzwanhong.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gzwanhong.utils.WhUtil;

public class CreatePo {

	public static void main(String[] args) throws Exception {
		String packageStr = "com.gzwanhong.domain";
		String path = System.getProperty("user.dir");
		Properties prop = new Properties();
		prop.load(new FileInputStream(path + File.separator + "config" + File.separator + "jdbc.properties"));

		String driverClassName = prop.getProperty("jdbc.driverClassName");
		String url = prop.getProperty("jdbc.url");
		String username = prop.getProperty("jdbc.username");
		String password = prop.getProperty("jdbc.password");

		Class.forName(driverClassName);
		Connection conn = DriverManager.getConnection(url + "&user=" + username + "&password=" + password);

		// 先获取这个数据库的所有表名
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getTables(null, "%", "%", new String[] { "TABLE" });

		List<String> tableList = new ArrayList<String>();
		while (rs.next()) {
			tableList.add(rs.getString("TABLE_NAME"));
		}

		// 根据表名再获取它对应的字段类型与名称，并产生PO类文件
		String fileStr = "";
		String importStr = "";
		String proStr = "";
		String getSetStr = "";
		String typeTemp = "";
		String columnTemp = "";
		String dirPath = path + File.separator + "src" + File.separator + packageStr.replace(".", File.separator);
		BufferedWriter bw = null;
		for (String tableName : tableList) {
			fileStr = "";
			importStr = "";
			proStr = "";
			getSetStr = "";
			rs = metaData.getColumns(null, "%", tableName, "%");

			while (rs.next()) {
				typeTemp = rs.getString("TYPE_NAME").toUpperCase();
				columnTemp = WhUtil.toCamelCaseString(rs.getString("COLUMN_NAME").toLowerCase());

				if (typeTemp.contains("INT")) {
					typeTemp = "Integer";
					proStr += "	private Integer " + columnTemp + ";\n";
				} else if (typeTemp.contains("DOUBLE") || typeTemp.contains("DECIMAL")) {
					typeTemp = "Double";
					proStr += "	private Double " + columnTemp + ";\n";
				} else if (typeTemp.contains("DATETIME")) {
					typeTemp = "Date";
					proStr += "	private Date " + columnTemp + ";\n";

					if (!importStr.contains("java.util.Date")) {
						importStr += "import java.util.Date;\n";
					}
				} else {
					typeTemp = "String";
					proStr += "	private String " + columnTemp + ";\n";
				}

				getSetStr += "	public " + typeTemp + " " + WhUtil.toGet(columnTemp) + "() {\n";
				getSetStr += "		return " + columnTemp + ";\n";
				getSetStr += "	}\n";
				getSetStr += "\n";
				getSetStr += "	public void " + WhUtil.toSet(columnTemp) + "(" + typeTemp + " " + columnTemp + ") {\n";
				getSetStr += "		this." + columnTemp + " = " + columnTemp + ";\n";
				getSetStr += "	}\n";
				getSetStr += "\n";
			}

			fileStr += "package " + packageStr + ";\n";
			fileStr += "\n";
			fileStr += "import java.io.Serializable;\n";
			fileStr += importStr;
			fileStr += "\n";
			fileStr += "public class " + WhUtil.toCamelCaseString(tableName.toLowerCase(), true)
					+ " implements Serializable {\n";
			fileStr += "	private static final long serialVersionUID = 1L;\n";
			fileStr += proStr + "\n";
			fileStr += getSetStr;
			fileStr += "	public static long getSerialversionuid() {\n";
			fileStr += "		return serialVersionUID;\n";
			fileStr += "	}\n";
			fileStr += "}";

			bw = new BufferedWriter(new FileWriter(
					dirPath + File.separator + WhUtil.toCamelCaseString(tableName.toLowerCase(), true) + ".java"));
			bw.write(fileStr);
			bw.flush();
			bw.close();
			System.out.println("已生成文件：" + dirPath + File.separator
					+ WhUtil.toCamelCaseString(tableName.toLowerCase(), true) + ".java");
		}

		conn.close();
	}

}
