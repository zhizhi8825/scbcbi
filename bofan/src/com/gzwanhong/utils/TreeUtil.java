/**
 * 
 */
package com.gzwanhong.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtil<T> {
	/**
	 * 递归处理出树结构的部门数据
	 * 
	 * @param treeMap
	 *            一个树的根
	 * @param treeDataMap
	 *            把集合处理成map后的结构
	 * @param elementsMap
	 *            树中每一个参数对应的方法，如text,getName
	 * @param attributesMap
	 *            属性对象，key为参数数，value为方法名,当key中是以逗号分隔的多个参数时，意思就是多个值加起来的字符串
	 * @param stateStatus
	 *            是否展开
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> dealTree(Map<String, Object> treeMap,
			Map<String, List<T>> tMap, Class<?> clz,
			Map<String, String> elementsMap, Map<String, String> attributesMap,
			Boolean stateStatus) throws Exception {

		List<T> list = tMap.get(WhUtil.toString(treeMap.get("id")));

		if (!WhUtil.isEmpty(list)) {
			List<Object> childrenList = new ArrayList<Object>();
			Map<String, Object> treeMapTemp = null;
			Map<String, Object> attributes = null;
			for (T t : list) {
				treeMapTemp = new HashMap<String, Object>();
				attributes = new HashMap<String, Object>();
				clz = t.getClass();
				for (String key : elementsMap.keySet()) {

					// 处理逗号
					String column = elementsMap.get(key); // 获取column,判断是否有逗号
					if (column.contains(",")) {
						String[] columns = column.split(",");
						String valStr = "";
						for (String c : columns) {
							Method method = clz.getMethod(c);
							String tval = WhUtil.isEmpty(method.invoke(t) + "") ? ""
									: method.invoke(t) + "";
							valStr = WhUtil.isEmpty(valStr) ? tval + " "
									: valStr + tval;
						}
						treeMapTemp.put(key, valStr);
					} else {

						Method method = clz.getMethod(elementsMap.get(key));
						treeMapTemp.put(key, method.invoke(t));
					}
				}
				if (!WhUtil.isEmpty(attributesMap)) {
					for (String key : attributesMap.keySet()) {
						if ("this".equals(attributesMap.get(key))) {
							attributes.put(key, t);
							treeMapTemp.put("attributes", attributes);
						} else {
							Method method = clz.getMethod(attributesMap
									.get(key));
							attributes.put(key, method.invoke(t));
							treeMapTemp.put("attributes", attributes);
						}
					}

				}
				if (stateStatus) {
					if (WhUtil.isEmpty(((Map<String, String>) treeMapTemp
							.get("attributes")).get("url"))) {
						treeMapTemp.put("state", "closed");
					}
				}
				treeMapTemp = dealTree(treeMapTemp, tMap, clz, elementsMap,
						attributesMap, stateStatus);
				childrenList.add(treeMapTemp);
			}

			treeMap.put("children", childrenList);
		}

		return treeMap;
	}
}
