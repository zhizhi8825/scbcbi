package com.gzwanhong.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.BaseDao;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhCommon;
import com.gzwanhong.utils.WhException;
import com.gzwanhong.utils.WhUtil;

@Repository
public class BaseDaoImpl implements BaseDao {
	@Autowired
	protected SqlSession sqlSession;
	private String PACKAGE = "com.gzwanhong.mapper";
	private Logger log = Logger.getLogger(this.getClass());
	private String SEPARATOR = ".";
	private String USER = "User";
	private String MAPPER = "Mapper";
	private String SAVE = "save";
	private String SAVE_ALL = "saveAll";
	private String QUERY_BY_ID = "queryById";
	private String QUERY_BY_IDS = "queryByIds";
	private String REMOVE_BY_ID = "removeById";
	private String REMOVE_BY_IDS = "removeByIds";
	private String REMOVE_BY_SQL = "removeBySql";
	private String UPDATE = "update";
	private String UPDATE_ALL = "updateAll";
	private String UPDATE_BY_SQL = "updateBySql";
	private String QUERY_BY_EXAMPLE = "queryByExample";
	private String QUERY_BY_SQL = "queryBySql";
	private String QUERY_BY_SQL_TO_MAP = "queryBySqlToMap";
	private String QUERY_COUNT_BY_EXAMPLE = "queryCountByExample";

	public BaseDaoImpl() {

	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 保存一条数据
	 */
	public <T> T save(T t) throws Exception {
		String typeName = t.getClass().getSimpleName();
		Class<?> clzz = t.getClass();

		// 获取ID，看是否已有ID，没有ID的时候才设置ID，有的话就不设置
		Method method = clzz.getMethod("getId");
		if (WhUtil.isEmpty(method.invoke(t))) {
			// 设置ID
			String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
			method = clzz.getMethod("setId", String.class);
			method.invoke(t, uuid);
		}

		// 设置创建日期
		method = clzz.getMethod("setCreateTime", Date.class);
		method.invoke(t, new Date());

		sqlSession.insert(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + SAVE, t);

		return t;
	}

	/**
	 * 保存一个数据集合
	 */
	public <T> List<T> saveAll(List<T> list) throws Exception {

		if (list != null && list.size() > 0) {
			Class<?> clzz = list.get(0).getClass();
			String typeName = clzz.getSimpleName();
			String uuid;
			Method method;
			Date now = new Date();

			// 设置ID
			for (int i = 0; i < list.size(); i++) {
				// 获取ID，看是否已有ID，没有ID的时候才设置ID，有的话就不设置
				method = clzz.getMethod("getId");
				if (WhUtil.isEmpty(method.invoke(list.get(i)))) {
					// 设置ID
					uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
					method = clzz.getMethod("setId", String.class);
					method.invoke(list.get(i), uuid);
				}

				// 设置创建日期
				method = clzz.getMethod("setCreateTime", Date.class);
				method.invoke(list.get(i), now);
			}

			sqlSession.insert(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + SAVE_ALL, list);
		}

		return list;
	}

	/**
	 * 保存或修改，根据ID是否为空做判断
	 *
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> T saveOrUpdate(T t) throws Exception {
		String typeName = t.getClass().getSimpleName();
		Class<?> clzz = t.getClass();
		Method method;

		// 取出ID
		method = clzz.getMethod("getId");
		String uuid = WhUtil.toString(method.invoke(t));

		// 如果ID为空的话就做保存，不为空的话就做修改
		if (WhUtil.isEmpty(uuid)) {
			uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
			// 设置ID
			method = clzz.getMethod("setId", String.class);
			method.invoke(t, uuid);

			// 设置创建日期
			method = clzz.getMethod("setCreateTime", Date.class);
			method.invoke(t, new Date());

			sqlSession.insert(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + SAVE, t);
		} else {
			sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE, t);
		}

		return t;
	}

	public <T> List<T> saveOrUpdateAll(List<T> list) throws Exception {
		if (list != null && list.size() > 0) {
			Class<?> clzz = list.get(0).getClass();
			String typeName = clzz.getSimpleName();
			String uuid;
			Method method;
			List<T> saveList = new ArrayList<T>();
			List<T> updateList = new ArrayList<T>();

			// 循环处理出要保存跟修改的List
			for (int i = 0; i < list.size(); i++) {
				method = clzz.getMethod("getId");
				uuid = WhUtil.toString(method.invoke(list.get(i)));

				// ID为空的话就给它设置一个ID，然后放到备保存list里去
				if (WhUtil.isEmpty(uuid)) {
					uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
					method = clzz.getMethod("setId", String.class);
					method.invoke(list.get(i), uuid);

					// 设置创建日期
					method = clzz.getMethod("setCreateTime", Date.class);
					method.invoke(list.get(i), new Date());

					saveList.add(list.get(i));
				} else {
					updateList.add(list.get(i));
				}
			}

			if (saveList.size() > 0) {
				sqlSession.insert(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + SAVE_ALL, saveList);
			}

			if (updateList.size() > 0) {
				sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE_ALL, updateList);
			}

			saveList.addAll(updateList);
			list = saveList;
		}

		return list;
	}

	/**
	 * 修改一条数据
	 *
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> int update(T t) {
		int result = 0;
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();

		result = sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE, t);

		return result;
	}

	/**
	 * 批量修改
	 *
	 * @param list
	 * @return
	 */
	public <T> int updateAll(List<T> list) {
		int result = 0;
		if (list != null && list.size() > 0) {
			Class<?> clzz = list.get(0).getClass();
			String typeName = clzz.getSimpleName();

			result = sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE_ALL, list);
		}

		return result;
	}

	/**
	 * 自定义SQL修改数据
	 *
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public <T> int updateBySql(String sql, Map<String, Object> paramMap) {
		paramMap.put("sqlStr", sql);
		Integer result = sqlSession.update(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + UPDATE_BY_SQL, paramMap);

		return result;
	}

	/**
	 * 自定义SQL修改数据，不传参数
	 *
	 * @param sql
	 * @return
	 */
	public <T> int updateBySql(String sql) {
		return updateBySql(sql, new HashMap<String, Object>());
	}

	/**
	 * 自定义SQL修改数据，带参
	 *
	 * @param mapperClass
	 * @param id
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public <T> int updateBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap) throws Exception {
		String sql = "";
		Integer result = 0;
		if ("true".equals(WhCommon.config.get("debug"))) {
			// 如果系统是debug状态的话，就实时的去xml里取sql
			sql = WhUtil.getSqlFromMapper(mapperClass, id);
		} else {
			// 如果系统不是debug状态，那就去内存里取sql
			sql = WhCommon.sqlMap.get(mapperClass.getSimpleName() + "." + id);
		}

		if (!WhUtil.isEmpty(sql)) {
			sql = dealSql(sql, paramMap);

			paramMap.put("sqlStr", sql);
			log.info(sql);
			result = sqlSession.update(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + UPDATE_BY_SQL, paramMap);
		} else {
			throw new WhException("SQL为空，是否没定位到mapper xml 中的SQL");
		}

		return result;
	}

	/**
	 * 自定义SQL修改数据，不带参
	 *
	 * @param mapperClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> int updateBySql(Class<?> mapperClass, String id) throws Exception {
		return updateBySql(mapperClass, id, new HashMap<String, Object>());
	}

	/**
	 * 根据指定的列做where条件修改数据
	 */
	public <T> int updateByWhereColumn(T t, String[] columns) throws Exception {
		int result = 0;
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 把columns数组处理成map
		Map<String, Boolean> colMap = new HashMap<String, Boolean>();
		for (String col : columns) {
			colMap.put(col, true);
		}

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("update " + WhUtil.toUnderLine(typeName) + " set id=id ");

		StringBuffer where = new StringBuffer(" where 0=0 ");

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			if ("id".equals(field.getName()) || "createTime".equals(field.getName())) {
				continue;
			}

			// 把它对应的get方法取出来
			method = clzz.getMethod(WhUtil.toGet(field.getName()));

			// 取值
			val = method.invoke(t);

			if (!WhUtil.isEmpty(val)) {
				sb.append("," + fieldLine + " = #{" + field.getName() + "}");

				if (!WhUtil.isEmpty(colMap.get(field.getName()))) {
					where.append(" and " + fieldLine + " = #{" + field.getName() + "}");
				}
			} else {
				// 值为空的时候
				sb.append("," + fieldLine + "=null");

				if (!WhUtil.isEmpty(colMap.get(field.getName()))) {
					where.append(" and " + fieldLine + "=null");
				}
			}
		}

		// 当有where条件的时候才做修改，以免把所有数据都修改了
		if (!" where 0=0 ".equals(where.toString())) {
			sb.append(where);
			Map<String, Object> paramMap = JsonUtil.beanToMap(t);
			paramMap.put("sqlStr", sb.toString());

			log.info(sb.toString());
			result = sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE_BY_SQL, paramMap);
		}
		return result;
	}

	/**
	 * 修改指定的列，没指定的话就不执行
	 *
	 * @param t
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> int updateByColumn(T t, String[] columns) throws Exception {
		int result = 0;
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 对象不空和指定列不为空时才继续
		if (!WhUtil.isEmpty(t) && !WhUtil.isEmpty(columns) && columns.length > 0) {
			Object val = null;
			StringBuffer sb = new StringBuffer();
			sb.append("update " + WhUtil.toUnderLine(typeName) + " set id=id ");

			// 循环列，组装SQL
			String fieldLine = "";
			for (String column : columns) {
				fieldLine = WhUtil.toUnderLine(column);

				// 取出指定列的get方法
				method = clzz.getMethod(WhUtil.toGet(column));

				// 取值
				val = method.invoke(t);

				if (!WhUtil.isEmpty(val)) {
					sb.append("," + fieldLine + " = #{" + column + "}");
				} else {
					// 值为空的时候
					sb.append("," + fieldLine + "=null");
				}
			}

			sb.append(" where id=#{id}");

			Map<String, Object> paramMap = JsonUtil.beanToMap(t);
			paramMap.put("sqlStr", sb.toString());

			log.info(sb.toString());
			result = sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE_BY_SQL, paramMap);
		}

		return result;
	}

	/**
	 * 指定的列不做修改
	 *
	 * @param t
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> int updateByNotColumn(T t, String[] columns) throws Exception {
		int result = 0;
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 把columns数组处理成map
		Map<String, Boolean> colMap = new HashMap<String, Boolean>();
		for (String col : columns) {
			colMap.put(col, true);
		}

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("update " + WhUtil.toUnderLine(typeName) + " set id=id ");

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			// 遇到id,createTime和指定的列，就不做修改
			if ("id".equals(field.getName()) || "createTime".equals(field.getName())
					|| !WhUtil.isEmpty(colMap.get(field.getName()))) {
				continue;
			}

			// 把它对应的get方法取出来
			method = clzz.getMethod(WhUtil.toGet(field.getName()));

			// 取值
			val = method.invoke(t);

			if (!WhUtil.isEmpty(val)) {
				sb.append("," + fieldLine + " = #{" + field.getName() + "}");
			} else {
				// 值为空的时候
				sb.append("," + fieldLine + "=null");
			}
		}

		sb.append(" where id = #{id}");

		Map<String, Object> paramMap = JsonUtil.beanToMap(t);
		paramMap.put("sqlStr", sb.toString());

		log.info(sb.toString());
		result = sqlSession.update(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + UPDATE_BY_SQL, paramMap);

		return result;
	}

	/**
	 * 根据ID查询一条数据
	 */
	public <T> T queryById(String id, Class<?> clz) throws Exception {
		String typeName = clz.getSimpleName();

		T t = sqlSession.selectOne(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_ID, id);

		return t;
	}

	/**
	 * 根据ID集合查询多条数据
	 *
	 * @param ids
	 * @param clz
	 * @return
	 */
	public <T> List<T> queryByIds(List<String> ids, Class<?> clz) throws Exception {
		String typeName = clz.getSimpleName();

		List<T> list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_IDS, ids);

		return list;
	}

	/**
	 * 根据ID删除一条数据
	 */
	public <T> int removeById(String id, Class<?> clz) throws Exception {
		int count = 0;

		// ID不为空的时候才执行
		if (!WhUtil.isEmpty(id)) {
			String typeName = clz.getSimpleName();

			count = sqlSession.delete(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + REMOVE_BY_ID, id);
		}
		return count;
	}

	/**
	 * 根据ID集合删除多条数据
	 */
	public <T> int removeByIds(List<String> ids, Class<?> clz) throws Exception {
		String typeName = clz.getSimpleName();
		int count = 0;

		// ID不为空的时候才执行
		if (!WhUtil.isEmpty(ids) && ids.size() > 0) {
			count = sqlSession.delete(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + REMOVE_BY_IDS, ids);
		}

		return count;
	}

	/**
	 * 根据自定义SQL删除数据
	 */
	public <T> int removeBySql(String sql, Map<String, Object> paramMap) throws Exception {
		paramMap.put("sql", sql);
		Integer count = sqlSession.delete(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + REMOVE_BY_SQL, paramMap);

		return count;
	}

	/**
	 * 根据自定义SQL删除数据
	 */
	public <T> int removeBySql(String sql) throws Exception {
		return removeBySql(sql, new HashMap<String, Object>());
	}

	/**
	 *
	 * @param mapperClass
	 *            mapper接口类
	 * @param id
	 *            要执行的sql在mapper xml 里所配置的id
	 * @param paramMap
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public <T> int removeBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap) throws Exception {
		String sql = "";
		Integer count = 0;
		if ("true".equals(WhCommon.config.get("debug"))) {
			// 如果系统是debug状态的话，就实时的去xml里取sql
			sql = WhUtil.getSqlFromMapper(mapperClass, id);
		} else {
			// 如果系统不是debug状态，那就去内存里取sql
			sql = WhCommon.sqlMap.get(mapperClass.getSimpleName() + "." + id);
		}

		if (!WhUtil.isEmpty(sql)) {
			sql = dealSql(sql, paramMap);

			paramMap.put("sqlStr", sql);
			log.info(sql);
			count = sqlSession.delete(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + REMOVE_BY_SQL, paramMap);
		} else {
			throw new WhException("SQL为空，是否没定位到mapper xml 中的SQL");
		}

		return count;
	}

	/**
	 *
	 * @param mapperClass
	 *            mapper接口类
	 * @param id
	 *            要执行的sql在mapper xml 里所配置的id
	 * @return
	 * @throws Exception
	 */
	public <T> int removeBySql(Class<?> mapperClass, String id) throws Exception {
		return removeBySql(mapperClass, id, new HashMap<String, Object>());
	}

	/**
	 * 根据一个实体里不为空的值进行删除，如果都为空的话，那就不做删除操作，以免把整个表数据都删了
	 *
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> int removeByExample(T t) throws Exception {
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("delete from " + WhUtil.toUnderLine(typeName) + " where 0=0 ");
		String where = "";

		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			// 把它对应的get方法取出来
			method = clzz.getMethod(WhUtil.toGet(field.getName()));

			// 取值
			val = method.invoke(t);

			// 如果值不为空的话就组装进sql里去
			if (!WhUtil.isEmpty(val)) {
				where += " and " + WhUtil.toUnderLine(field.getName()) + "= #{" + field.getName() + "}";
			}
		}

		int count = 0;
		if (!WhUtil.isEmpty(where)) {
			sb.append(where);
			Map<String, Object> paramMap = JsonUtil.beanToMap(t);
			paramMap.put("sqlStr", sb.toString());
			log.info(sb.toString());
			count = sqlSession.delete(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + REMOVE_BY_SQL, paramMap);
		}

		return count;
	}

	public <T> List<T> queryByExample(T t) throws Exception {
		return queryByExample(t, null, null, null);
	}

	public <T> List<T> queryByExample(T t, Integer pageIndex, Integer pageSize) throws Exception {
		return queryByExample(t, pageIndex, pageSize, null);
	}

	public <T> List<T> queryByExample(T t, String orderByStr) throws Exception {
		return queryByExample(t, null, null, orderByStr);
	}

	public <T> List<T> queryByExample(T t, Integer pageIndex, Integer pageSize, String orderByStr) throws Exception {
		List<T> list = new ArrayList<T>();
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select t.* from " + WhUtil.toUnderLine(typeName) + " t where 0=0 ");

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			// 把它对应的get方法取出来
			method = clzz.getMethod(WhUtil.toGet(field.getName()));

			// 取值
			val = method.invoke(t);

			// 如果值不为空的话就组装进sql里去
			if (!WhUtil.isEmpty(val)) {
				sb.append(" and " + fieldLine + " = #{" + field.getName() + "}");
			}
		}

		// 设置排序
		if (!WhUtil.isEmpty(orderByStr)) {
			sb.append(" order by " + WhUtil.toUnderLine(orderByStr));
		}

		// 设置分页
		if (!WhUtil.isEmpty(pageIndex) && !WhUtil.isEmpty(pageSize)) {
			int startNum = (pageIndex - 1) * pageSize;
			sb.append(" limit " + startNum + " , " + pageSize);
		}

		Map<String, Object> paramMap = JsonUtil.beanToMap(t);
		paramMap.put("sqlStr", sb.toString());
		log.info(sb.toString());
		log.info(paramMap);
		list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_EXAMPLE, paramMap);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		return list;
	}

	public <T> DatagridEntity queryByExampleToDatagrid(T t, PageInfo pageInfo) throws Exception {
		return queryByExampleToDatagrid(t, pageInfo, null);
	}

	public <T> DatagridEntity queryByExampleToDatagrid(T t, PageInfo pageInfo, String orderByStr) throws Exception {
		List<T> list = new ArrayList<T>();
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select t.* from " + WhUtil.toUnderLine(typeName) + " t where 0=0 ");

		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select count(1) as TOTAL  from " + WhUtil.toUnderLine(typeName) + " t where 0=0 ");

		StringBuffer where = new StringBuffer();

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			// 把它对应的get方法取出来
			method = clzz.getMethod(WhUtil.toGet(field.getName()));

			// 取值
			val = method.invoke(t);

			// 如果值不为空的话就组装进sql里去
			if (!WhUtil.isEmpty(val)) {
				where.append(" and " + fieldLine + " = #{" + field.getName() + "}");
			}
		}

		sb.append(where);

		// 设置排序
		if (!WhUtil.isEmpty(orderByStr)) {
			sb.append(" order by " + WhUtil.toUnderLine(orderByStr));
		}

		if (!WhUtil.isEmpty(pageInfo)) {
			sb.append(" limit " + (pageInfo.getStartNum() - 1) + " , " + pageInfo.getRows());
		}

		Map<String, Object> paramMap = JsonUtil.beanToMap(t);
		paramMap.put("sqlStr", sb.toString());

		log.info(sb.toString());
		list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_EXAMPLE, paramMap);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		// 再查总数total
		sqlCount.append(where);
		paramMap.put("sqlStr", sqlCount.toString());
		log.info(sqlCount.toString());
		Map<String, Object> count = sqlSession
				.selectOne(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP, paramMap);
		DatagridEntity datagridEntity;
		if (WhUtil.isEmpty(pageInfo)) {
			datagridEntity = new DatagridEntity(list, WhUtil.toInteger(count.get("TOTAL")));
		} else {
			datagridEntity = new DatagridEntity(list, WhUtil.toInteger(count.get("TOTAL")), pageInfo.getPage());
		}
		return datagridEntity;
	}

	public <T> DatagridEntity queryByExampleToDatagrid(Map<String, Object> paramMap, Class<T> clz, PageInfo pageInfo)
			throws Exception {
		return queryByExampleToDatagrid(paramMap, clz, pageInfo, null);
	}

	public <T> DatagridEntity queryByExampleToDatagrid(Map<String, Object> paramMap, Class<T> clz, PageInfo pageInfo,
			String orderByStr) throws Exception {
		List<T> list = new ArrayList<T>();
		Class<?> clzz = clz;
		String typeName = clzz.getSimpleName();

		if (WhUtil.isEmpty(paramMap)) {
			paramMap = new HashMap<String, Object>();
		}

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select t.* from " + WhUtil.toUnderLine(typeName) + " t where 0=0 ");

		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select count(1) as TOTAL  from " + WhUtil.toUnderLine(typeName) + " t where 0=0 ");

		StringBuffer where = new StringBuffer();

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			if (!WhUtil.isEmpty(paramMap.get(field.getName()))) {
				val = paramMap.get(field.getName());

				// 如果值不为空的话就组装进sql里去
				if (!WhUtil.isEmpty(val)) {
					where.append(" and " + fieldLine + "= #{" + field.getName() + "}");
				}
			}
		}

		sb.append(where);

		// 设置排序
		if (!WhUtil.isEmpty(orderByStr)) {
			sb.append(" order by " + WhUtil.toUnderLine(orderByStr));
		}

		if (!WhUtil.isEmpty(pageInfo)) {
			sb.append(" limit " + (pageInfo.getStartNum() - 1) + " , " + pageInfo.getRows());
		}

		paramMap.put("sqlStr", sb.toString());

		log.info(sb.toString());
		list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_EXAMPLE, paramMap);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		// 再查总数total
		sqlCount.append(where);
		paramMap.put("sqlStr", sqlCount.toString());
		log.info(sqlCount.toString());
		Map<String, Object> count = sqlSession
				.selectOne(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP, paramMap);

		DatagridEntity datagridEntity = new DatagridEntity(list, WhUtil.toInteger(count.get("TOTAL")));

		return datagridEntity;
	}

	public <T> int queryCountByExample(T t) throws Exception {
		Class<?> clzz = t.getClass();
		String typeName = clzz.getSimpleName();
		Method method;

		// 把该对象类的变量都取出来
		Field[] fields = clzz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select count(id) as c from " + WhUtil.toUnderLine(typeName) + " where 0=0 ");

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			// 把它对应的get方法取出来
			method = clzz.getMethod(WhUtil.toGet(field.getName()));

			// 取值
			val = method.invoke(t);

			// 如果值不为空的话就组装进sql里去
			if (!WhUtil.isEmpty(val)) {
				sb.append(" and " + fieldLine + " = #{" + field.getName() + "}");
			}
		}

		Map<String, Object> paramMap = JsonUtil.beanToMap(t);
		paramMap.put("sqlStr", sb.toString());
		log.info(sb.toString());
		int count = sqlSession.selectOne(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_COUNT_BY_EXAMPLE,
				paramMap);

		return count;
	}

	/**
	 * 根据map的配置键值对查指定类型的数据
	 */
	public <T> List<T> queryByExample(Map<String, Object> param, Class<T> clz) throws Exception {
		List<T> list = new ArrayList<T>();
		String typeName = clz.getSimpleName();

		// 把该对象类的变量都取出来
		Field[] fields = clz.getDeclaredFields();

		// 循环组装sql
		Object val = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select * from " + WhUtil.toUnderLine(typeName) + " where 0=0 ");

		String fieldLine = "";
		for (Field field : fields) {
			// 排除掉static类型的字段，其实是为了排除掉serialVersionUID字段
			if (field.toString().contains("static")) {
				continue;
			}

			fieldLine = WhUtil.toUnderLine(field.getName());

			if (!WhUtil.isEmpty(param.get(field.getName()))) {
				val = param.get(field.getName());

				// 如果值不为空的话就组装进sql里去
				if (!WhUtil.isEmpty(val)) {
					sb.append(" and " + fieldLine + "= #{" + field.getName() + "}");
				}
			}
		}

		param.put("sqlStr", sb.toString());
		log.info(sb.toString());
		list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_EXAMPLE, param);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		return list;
	}

	public <T> List<T> queryBySql(String sql, Map<String, Object> paramMap, Class<T> clz) throws Exception {
		List<T> list = new ArrayList<T>();
		String typeName = clz.getSimpleName();

		paramMap.put("sqlStr", sql);
		log.info(sql);
		list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_SQL, paramMap);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		return list;
	}

	public <T> List<T> queryBySql(String sql, Class<T> clz) throws Exception {
		return queryBySql(sql, new HashMap<String, Object>(), clz);
	}

	public <T> List<T> queryBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap, Class<T> clz)
			throws Exception {
		List<T> list = new ArrayList<T>();
		String typeName = clz.getSimpleName();
		String sql = "";
		if ("true".equals(WhCommon.config.get("debug"))) {
			// 如果系统是debug状态的话，就实时的去xml里取sql
			sql = WhUtil.getSqlFromMapper(mapperClass, id);
		} else {
			// 如果系统不是debug状态，那就去内存里取sql
			sql = WhCommon.sqlMap.get(mapperClass.getSimpleName() + "." + id);
		}

		if (!WhUtil.isEmpty(sql)) {
			sql = dealSql(sql, paramMap);

			paramMap.put("sqlStr", sql);
			log.info(sql);
			list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_SQL, paramMap);

			if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
				list = new ArrayList<T>();
			}
		} else {
			throw new WhException("SQL为空，是否没定位到mapper xml 中的SQL");
		}

		return list;
	}

	public <T> List<T> queryBySql(Class<?> mapperClass, String id, Class<T> clz) throws Exception {
		return queryBySql(mapperClass, id, new HashMap<String, Object>(), clz);
	}

	/**
	 * 查询datagrid列表类型的数据，有规定数据是什么类型
	 */
	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo, Map<String, Object> paramMap,
			Class<T> clz) throws Exception {
		List<T> list = new ArrayList<T>();
		Map<String, Object> count = null;
		String typeName = clz.getSimpleName();

		// 处理传来的SQL，给它套个分页处理
		String sqlPaging = "";
		if (!WhUtil.isEmpty(pageInfo)) {
			sqlPaging = "select * from (" + sql + ") rs limit " + (pageInfo.getStartNum() - 1) + " , "
					+ pageInfo.getRows();
		} else {
			sqlPaging = sql;
		}

		// 查分页后的数据
		paramMap.put("sqlStr", sqlPaging);
		log.info(sqlPaging);
		list = sqlSession.selectList(PACKAGE + SEPARATOR + typeName + MAPPER + SEPARATOR + QUERY_BY_SQL, paramMap);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		// 查询数据总数
		String sqlCount = "select count(1) as TOTAL from (" + sql + ") rs";
		paramMap.put("sqlStr", sqlCount);
		log.info(sqlCount);
		count = sqlSession.selectOne(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP, paramMap);

		DatagridEntity datagridEntity = new DatagridEntity(list, WhUtil.toInteger(count.get("TOTAL")));

		return datagridEntity;
	}

	/**
	 * 查询datagrid列表类型的数据，没有规定数据是什么类型，就是map
	 */
	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo, Map<String, Object> paramMap)
			throws Exception {
		List<T> list = new ArrayList<T>();
		Map<String, Object> count = null;

		// 处理传来的SQL，给它套个分页处理
		String sqlPaging = "";
		if (!WhUtil.isEmpty(pageInfo)) {
			sqlPaging = "select * from (" + sql + ") rs limit " + (pageInfo.getStartNum() - 1) + " , "
					+ pageInfo.getRows();
		} else {
			sqlPaging = sql;
		}

		// 查分页后的数据
		paramMap.put("sqlStr", sqlPaging);
		log.info(sqlPaging);
		list = sqlSession.selectList(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP, paramMap);

		if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
			list = new ArrayList<T>();
		}

		// 查询数据总数
		String sqlCount = "select count(1) as TOTAL from (" + sql + ") rs";
		paramMap.put("sqlStr", sqlCount);
		log.info(sqlCount);
		count = sqlSession.selectOne(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP, paramMap);

		DatagridEntity datagridEntity = new DatagridEntity(list, WhUtil.toInteger(count.get("TOTAL")));

		return datagridEntity;
	}

	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo, Class<T> clz) throws Exception {
		return queryBySqlToDatagrid(sql, pageInfo, new HashMap<String, Object>(), clz);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo) throws Exception {
		return queryBySqlToDatagrid(sql, pageInfo, new HashMap<String, Object>());
	}

	public List<Map<String, Object>> queryBySql(String sql, Map<String, Object> paramMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		paramMap.put("sqlStr", sql);
		log.info(sql);
		list = sqlSession.selectList(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP, paramMap);

		return list;
	}

	public List<Map<String, Object>> queryBySql(String sql) throws Exception {
		return queryBySql(sql, new HashMap<String, Object>());
	}

	/**
	 * 取mapper xml 里配置的SQL查询
	 *
	 * @param mapperClass
	 * @param id
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap)
			throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "";
		if ("true".equals(WhCommon.config.get("debug"))) {
			// 如果系统是debug状态的话，就实时的去xml里取sql
			sql = WhUtil.getSqlFromMapper(mapperClass, id);
		} else {
			// 如果系统不是debug状态，那就去内存里取sql
			sql = WhCommon.sqlMap.get(mapperClass.getSimpleName() + "." + id);
		}

		if (!WhUtil.isEmpty(sql)) {
			sql = dealSql(sql, paramMap);

			paramMap.put("sqlStr", sql);
			log.info(sql);
			list = sqlSession.selectList(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP,
					paramMap);

			if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
				list = new ArrayList<Map<String, Object>>();
			}
		} else {
			throw new WhException("SQL为空，是否没定位到mapper xml 中的SQL");
		}

		return list;
	}

	public List<Map<String, Object>> queryBySql(Class<?> mapperClass, String id) throws Exception {
		return queryBySql(mapperClass, id, new HashMap<String, Object>());
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo,
			Map<String, Object> paramMap, Class<?> clz) throws Exception {
		DatagridEntity datagridEntity = null;
		List<T> list = new ArrayList<T>();
		Map<String, Object> count = null;

		if (WhUtil.isEmpty(paramMap)) {
			paramMap = new HashMap<String, Object>();
		}

		String sql = "";
		if ("true".equals(WhCommon.config.get("debug"))) {
			// 如果系统是debug状态的话，就实时的去xml里取sql
			sql = WhUtil.getSqlFromMapper(mapperClass, id);
		} else {
			// 如果系统不是debug状态，那就去内存里取sql
			sql = WhCommon.sqlMap.get(mapperClass.getSimpleName() + "." + id);
		}

		if (!WhUtil.isEmpty(sql)) {
			sql = dealSql(sql, paramMap);

			// 处理传来的SQL，给它套个分页处理
			String sqlPaging = "";
			if (!WhUtil.isEmpty(pageInfo)) {
				sqlPaging = "select * from (" + sql + ") rs limit " + (pageInfo.getStartNum() - 1) + " , "
						+ pageInfo.getRows();
			} else {
				sqlPaging = sql;
			}

			// 查分页后的数据
			paramMap.put("sqlStr", sqlPaging);
			log.info(sqlPaging);
			if (WhUtil.isEmpty(clz)) {
				list = sqlSession.selectList(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP,
						paramMap);
			} else {
				list = sqlSession.selectList(
						PACKAGE + SEPARATOR + clz.getSimpleName() + MAPPER + SEPARATOR + QUERY_BY_SQL, paramMap);
			}

			if (!WhUtil.isEmpty(list) && list.size() == 1 && WhUtil.isEmpty(list.get(0))) {
				list = new ArrayList<T>();
			}

			// 查询数据总数
			String sqlCount = "select count(1) as TOTAL from (" + sql + ") rs";
			paramMap.put("sqlStr", sqlCount);
			log.info(sqlCount);
			count = sqlSession.selectOne(PACKAGE + SEPARATOR + USER + MAPPER + SEPARATOR + QUERY_BY_SQL_TO_MAP,
					paramMap);

			datagridEntity = new DatagridEntity(list, WhUtil.toInteger(count.get("TOTAL")));
		} else {
			throw new WhException("SQL为空，是否没定位到mapper xml 中的SQL");
		}

		return datagridEntity;
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo,
			Map<String, Object> paramMap) throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, pageInfo, paramMap, null);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo)
			throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, pageInfo, new HashMap<String, Object>(), null);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, Map<String, Object> paramMap)
			throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, null, paramMap, null);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id) throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, null, new HashMap<String, Object>(), null);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo, Class<?> clz)
			throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, pageInfo, new HashMap<String, Object>(), clz);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, Map<String, Object> paramMap,
			Class<?> clz) throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, null, paramMap, clz);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, Class<?> clz) throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, null, new HashMap<String, Object>(), clz);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo,
			ParamEntity paramEntity) throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, pageInfo, JsonUtil.beanToMap(paramEntity), null);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, ParamEntity paramEntity)
			throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, null, JsonUtil.beanToMap(paramEntity), null);
	}

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, ParamEntity paramEntity,
			Class<?> clz) throws Exception {
		return queryBySqlToDatagrid(mapperClass, id, null, JsonUtil.beanToMap(paramEntity), clz);
	}

	public String dealSql(String sql, Map<String, Object> paramMap) throws Exception {
		// 定义正则表达式
		Pattern p = Pattern.compile("<<.*(?!>>).*>>");
		Matcher m = p.matcher(sql);
		Pattern p2 = Pattern.compile("#\\{[^}]+\\}");
		Matcher m2 = null;
		Pattern p3 = Pattern.compile("\\$\\{[^}]+\\}");
		Matcher m3 = null;
		Boolean need = false; // 标记是否需要<<>>中的条件用

		// 取出带有<<>>的部分，一个个要对，看里面的参数在paramMap里有没有值，没有的话就把它从sql中去掉
		String temp = "";
		String paramTemp = "";
		Object paramObject = null;
		List<?> paramList = null;
		while (m.find()) {
			temp = m.group();

			// 取出#{userName} 这样的参数
			m2 = p2.matcher(temp);
			need = false;

			// 循环的看在paramMap 中是否有该参数的值
			while (m2.find()) {
				paramTemp = m2.group();
				paramTemp = paramTemp.replace("#{", "").replace("}", "").trim();
				paramObject = paramMap.get(paramTemp);

				if (!WhUtil.isEmpty(paramObject)) {
					// 如果是数组的话，再看size是否为0
					if (paramObject instanceof String[] || paramObject instanceof Integer[]
							|| paramObject instanceof Double[] || paramObject instanceof List) {
						// 如果是数组的话，再看size是否为0
						if (paramObject instanceof String[] || paramObject instanceof Integer[]
								|| paramObject instanceof Double[]) {
							paramList = new ArrayList<Object>(Arrays.asList((Object[]) paramObject));
						} else {
							paramList = (List<?>) paramObject;
						}

						if (paramList.size() > 0) {
							need = true;
						}
					} else {
						need = true;
					}
				}
			}

			// 再取出#{userName} 这样的参数
			m3 = p3.matcher(temp);

			// 循环的看在paramMap 中是否有该参数的值
			while (m3.find()) {
				paramTemp = m3.group();
				paramObject = paramMap.get(paramTemp.replace("${", "").replace("}", "").trim());

				if (WhUtil.isEmpty(paramObject)) {
					// 没值，那就把该条件去掉
					sql = sql.replace(temp, "");
				} else {
					// 有值，那就把该值以字符串的形式加进去
					sql = sql.replace(temp,
							temp.replace("<<", "").replace(">>", "").replace(paramTemp, WhUtil.toString(paramObject)));
					need = true;
				}
			}

			if (need) {
				// 如果是需要该条件的话，那就从sql中把<<>>符号去掉
				sql = sql.replace(temp, temp.replace("<<", "").replace(">>", ""));
			} else {
				// 如果是不需要这个条件的话，那就把整<<>>包括里面的内容都去掉
				sql = sql.replace(temp, "");
			}
		}

		// 接下来处理 in 语句，特殊一点
		Pattern p4 = Pattern.compile("(in|IN)\\s*\\(\\s*#\\{[^}]+\\}\\s*\\)");
		Matcher m4 = p4.matcher(sql);

		// 循环处理正则匹配出来的 in (#{list}) 这类型的字符
		String inSql = "";
		Integer paramSize = null;
		String paramName = "";
		while (m4.find()) {
			temp = m4.group();
			paramTemp = temp.substring(temp.indexOf("#{") + 2, temp.indexOf("}")).trim(); // 处理出参数的名字

			// 如果传来的参数中有该参数的话，那就把它取出来处理到sql里去
			if (!WhUtil.isEmpty(paramMap.get(paramTemp))) {
				inSql = "";

				// 取出参数
				paramObject = paramMap.get(paramTemp);

				// 如果是数组的话就把它处理成集合
				if (paramObject instanceof String[] || paramObject instanceof Integer[]
						|| paramObject instanceof Double[]) {
					paramList = new ArrayList<Object>(Arrays.asList((Object[]) paramObject));
				} else {
					paramList = (List<?>) paramObject;
				}
				paramSize = paramList.size();

				// 如果集合为零，则跳出，不处理
				if (paramSize.intValue() == 0) {
					continue;
				}

				// 循环参数，设到sql的in里和paramMap 参数集合里
				for (int i = 0; i < paramSize; i++) {
					// 设置参数的名字
					paramName = paramTemp + i;
					if (i != 0) {
						inSql += ",#{" + paramName + "}";
					} else {
						inSql += "#{" + paramName + "}";
					}

					// 把集合里的该值设到参数集合里
					paramMap.put(paramName, paramList.get(i));
				}

				// 把处理好的inSql替换到sql里去
				sql = sql.replace(temp, "in (" + inSql + ")");
			}
		}

		return sql;
	}
}
