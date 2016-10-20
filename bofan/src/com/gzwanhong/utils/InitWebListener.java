package com.gzwanhong.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gzwanhong.mapper.UserMapper;

public class InitWebListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			System.out.println("加载初始化数据...");
			// 加载配置文件在内存
			InputStream is = getClass().getClassLoader().getResourceAsStream("/config.properties");
			WhCommon.config.load(is);
			is.close();

			if ("false".equals(WhCommon.config.get("debug"))) {
				// 如果不是debug状态，那就把mapperXml 加载在内存里
				// 以UserMapper类为目标，先获取到mapper包的路径
				File userMapper = new File(URLDecoder.decode(UserMapper.class.getResource("").getPath()));
				// 组装出xml的路径
				String mapperXmlPath = userMapper.getPath() + File.separator + "xml";

				File[] mappers = new File(mapperXmlPath).listFiles();
				String mapperName = "";
				SAXReader reader = new SAXReader();
				reader.setValidation(false);
				reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				Document doc = null;
				Element root = null;
				List<Element> nodes = null;
				Element node = null;
				String id = null;
				for (File mapper : mappers) {
					mapperName = mapper.getName();
					mapperName = mapperName.substring(0, mapperName.indexOf(".")); // 把.xml去掉

					doc = reader.read(mapper);
					root = doc.getRootElement();
					nodes = root.elements();

					for (int i = 0; i < nodes.size(); i++) {
						node = nodes.get(i);
						if ("select".equals(node.getName().toLowerCase())
								|| "update".equals(node.getName().toLowerCase())
								|| "delete".equals(node.getName().toLowerCase())
								|| "insert".equals(node.getName().toLowerCase())) {
							id = node.attributeValue("id");

							// 除掉一些系统本来定义的sql
							if (!("queryById".equals(id) || "queryByIds".equals(id) || "save".equals(id)
									|| "saveAll".equals(id) || "removeById".equals(id) || "removeByIds".equals(id)
									|| "removeBySql".equals(id) || "update".equals(id) || "updateAll".equals(id)
									|| "updateBySql".equals(id) || "queryByExample".equals(id)
									|| "queryBySql".equals(id) || "queryBySqlToMap".equals(id)
									|| "queryCountByExample".equals(id)))
								WhCommon.sqlMap.put(mapperName + "." + id, node.getText());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("初始化数据加载完成！");
	}

}
