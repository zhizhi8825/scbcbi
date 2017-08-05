package com.gzwanhong.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.gzwanhong.utils.WhUtil;

public class CreateClass {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws Exception {
		// 这个是必填，PO类
		Class<?>[] clzzs = new Class<?>[] {};
		// 这个也是必填，生成到目的包位置
		String targetPackage = "com.wh.tax.system";
		String baseDaoPackage = "com.wh.tax.common.dao";

		String className = null;
		String str = "";
		String path = "";
		FileOutputStream fos = null;

		// 循环实体
		for (Class<?> clzz : clzzs) {
			// 获取类名
			className = clzz.getSimpleName();

			// 生成action类
			str = "";
			str += "package " + targetPackage + ".action;                                    \n";
			str += "                                                                 \n";
			str += "import org.springframework.beans.factory.annotation.Autowired;   \n";
			str += "import org.springframework.context.annotation.Scope;   \n";
			str += "import org.springframework.stereotype.Action;                \n";
			str += "                                                                 \n";
			str += "import com.wh.tax.common.controller.BaseController;                \n";
			str += "import " + targetPackage + ".logic." + className + "Logic;                            \n";
			str += "                                                                 \n";
			str += "@Controller                                                      \n";
			str += "@Scope(value = \"prototype\")                                                      \n";
			str += "public class " + className + "Ctrl  extends BaseController{                    \n";
			str += "	@Autowired                                                   \n";
			str += "	private " + className + "Logic " + WhUtil.lowerFirst(className)
					+ "Logic;                                 \n";
			str += "                                                                 \n";
			str += "	public " + className + "Logic get" + className + "Logic() {                            \n";
			str += "		return " + WhUtil.lowerFirst(className)
					+ "Logic;                                        \n";
			str += "	}                                                            \n";
			str += "                                                                 \n";
			str += "	public void set" + className + "Logic(" + className + "Logic "
					+ WhUtil.lowerFirst(className) + "Logic) {              \n";
			str += "		this." + WhUtil.lowerFirst(className) + "Logic = " + WhUtil.lowerFirst(className)
					+ "Logic;                              \n";
			str += "	}                                                            \n";
			str += "                                                                 \n";
			str += "}                                                                \n";

			path = System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ targetPackage.replace(".", File.separator) + File.separator + "action" + File.separator
					+ className + "Ctrl.java";

			if (!new File(path).exists()) {
				fos = new FileOutputStream(new File(path));
				fos.write(str.toString().getBytes("utf8"));
				fos.flush();
				fos.close();
				System.out.println("已生成文件：" + path);
			}

			// 生成logic类
			str = "";
			str += "package " + targetPackage + ".logic;  \n";
			str += "                              \n";
			str += "public interface " + className + "Logic {  \n";
			str += "                              \n";
			str += "}                             \n";

			path = System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ targetPackage.replace(".", File.separator) + File.separator + "logic" + File.separator
					+ className + "Logic.java";

			if (!new File(path).exists()) {
				fos = new FileOutputStream(new File(path));
				fos.write(str.toString().getBytes("utf8"));
				fos.flush();
				fos.close();
				System.out.println("已生成文件：" + path);
			}

			// 生成logicImpl类
			str = "";
			str += "package " + targetPackage + ".logic.impl;                                \n";
			str += "                                                                 \n";
			str += "import org.springframework.beans.factory.annotation.Autowired;   \n";
			str += "import org.springframework.context.annotation.Scope;             \n";
			str += "import org.springframework.stereotype.Logic;                   \n";
			str += "                                                                 \n";
			str += "import " + targetPackage + ".dao." + className + "Dao;                                \n";
			str += "import " + targetPackage + ".logic." + className + "Logic;                            \n";
			str += "                                                                 \n";
			str += "@Service                                                         \n";
			str += "@Scope(value = \"prototype\")                                      \n";
			str += "public class " + className + "LogicImpl implements " + className + "Logic {                \n";
			str += "	@Autowired                                                   \n";
			str += "	private " + className + "Dao " + WhUtil.lowerFirst(className)
					+ "Dao;                                     \n";
			str += "                                                                 \n";
			str += "	public " + className + "Dao get" + className + "Dao() {                                \n";
			str += "		return " + WhUtil.lowerFirst(className)
					+ "Dao;                                          \n";
			str += "	}                                                            \n";
			str += "                                                                 \n";
			str += "	public void set" + className + "Dao(" + className + "Dao " + WhUtil.lowerFirst(className)
					+ "Dao) {                    \n";
			str += "		this." + WhUtil.lowerFirst(className) + "Dao = " + WhUtil.lowerFirst(className)
					+ "Dao;                                  \n";
			str += "	}                                                            \n";
			str += "                                                                 \n";
			str += "}                                                                \n";

			path = System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ targetPackage.replace(".", File.separator) + File.separator + "logic" + File.separator + "impl"
					+ File.separator + className + "LogicImpl.java";

			if (!new File(path).exists()) {
				fos = new FileOutputStream(new File(path));
				fos.write(str.toString().getBytes("utf8"));
				fos.flush();
				fos.close();
				System.out.println("已生成文件：" + path);
			}

			// 生成dao类
			str = "";
			str += "package " + targetPackage + ".dao;                    \n";
			str += "                                              \n";
			str += "import " + baseDaoPackage + ".BaseDao;\n";
			str += "                                              \n";
			str += "public interface " + className + "Dao extends BaseDao {    \n";
			str += "                                              \n";
			str += "}                                             \n";

			path = System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ targetPackage.replace(".", File.separator) + File.separator + "dao" + File.separator + className
					+ "Dao.java";

			if (!new File(path).exists()) {
				fos = new FileOutputStream(new File(path));
				fos.write(str.toString().getBytes("utf8"));
				fos.flush();
				fos.close();
				System.out.println("已生成文件：" + path);
			}

			// 生成daoImpl类
			str = "";
			str += "package " + targetPackage + ".dao.impl;                                    \n";
			str += "                                                                   \n";
			str += "import org.springframework.context.annotation.Scope;               \n";
			str += "import org.springframework.stereotype.Repository;                  \n";
			str += "                                                                   \n";
			str += "import " + baseDaoPackage + ".impl.BaseDaoImpl;\n";
			str += "import " + targetPackage + ".dao." + className + "Dao;                                  \n";
			str += "                                                                   \n";
			str += "@Repository                                                        \n";
			str += "@Scope(value = \"prototype\")                                        \n";
			str += "public class " + className + "DaoImpl extends BaseDaoImpl implements " + className + "Dao {  \n";
			str += "                                                                   \n";
			str += "}                                                                  \n";

			path = System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ targetPackage.replace(".", File.separator) + File.separator + "dao" + File.separator + "impl"
					+ File.separator + className + "DaoImpl.java";

			if (!new File(path).exists()) {
				fos = new FileOutputStream(new File(path));
				fos.write(str.toString().getBytes("utf8"));
				fos.flush();
				fos.close();
				System.out.println("已生成文件：" + path);
			}
		}

	}
}
