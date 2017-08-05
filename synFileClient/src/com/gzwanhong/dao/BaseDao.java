package com.gzwanhong.dao;

import java.util.List;
import java.util.Map;

import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;

public interface BaseDao {
	public <T> T save(T t) throws Exception;

	/**
	 * 保存一个数据集合
	 */
	public <T> List<T> saveAll(List<T> list) throws Exception;

	/**
	 * 保存或修改，根据ID是否为空做判断
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> T saveOrUpdate(T t) throws Exception;

	public <T> List<T> saveOrUpdateAll(List<T> list) throws Exception;

	/**
	 * 修改一条数据
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> int update(T t) throws Exception;

	/**
	 * 批量修改
	 * 
	 * @param list
	 * @return
	 */
	public <T> int updateAll(List<T> list) throws Exception;

	/**
	 * 自定义SQL修改数据
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public <T> int updateBySql(String sql, Map<String, Object> paramMap) throws Exception;

	/**
	 * 自定义SQL修改数据，不传参数
	 * 
	 * @param sql
	 * @return
	 */
	public <T> int updateBySql(String sql) throws Exception;

	/**
	 * 自定义SQL修改数据，带参
	 * 
	 * @param mapperClass
	 * @param id
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public <T> int updateBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap) throws Exception;

	/**
	 * 自定义SQL修改数据，不带参
	 * 
	 * @param mapperClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> int updateBySql(Class<?> mapperClass, String id) throws Exception;

	/**
	 * 根据指定的列做where条件修改数据
	 */
	public <T> int updateByWhereColumn(T t, String[] columns) throws Exception;

	/**
	 * 修改指定的列，没指定的话就不执行
	 * 
	 * @param t
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> int updateByColumn(T t, String[] columns) throws Exception;

	/**
	 * 指定的列不做修改
	 * 
	 * @param t
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public <T> int updateByNotColumn(T t, String[] columns) throws Exception;

	/**
	 * 根据ID查询一条数据
	 */
	public <T> T queryById(String id, Class<?> clz) throws Exception;

	/**
	 * 根据ID集合查询多条数据
	 * 
	 * @param ids
	 * @param clz
	 * @return
	 */
	public <T> List<T> queryByIds(List<String> ids, Class<?> clz) throws Exception;

	/**
	 * 根据ID删除一条数据
	 */
	public <T> int removeById(String id, Class<?> clz) throws Exception;

	/**
	 * 根据ID集合删除多条数据
	 */
	public <T> int removeByIds(List<String> ids, Class<?> clz) throws Exception;

	/**
	 * 根据自定义SQL删除数据
	 */
	public <T> int removeBySql(String sql, Map<String, Object> paramMap) throws Exception;

	/**
	 * 根据自定义SQL删除数据
	 */
	public <T> int removeBySql(String sql) throws Exception;

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
	public <T> int removeBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap) throws Exception;

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
	public <T> int removeBySql(Class<?> mapperClass, String id) throws Exception;

	/**
	 * 根据一个实体里不为空的值进行删除，如果都为空的话，那就不做删除操作，以免把整个表数据都删了
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> int removeByExample(T t) throws Exception;

	public <T> List<T> queryByExample(T t) throws Exception;

	public <T> List<T> queryByExample(T t, Integer pageIndex, Integer pageSize) throws Exception;

	public <T> List<T> queryByExample(T t, String orderByStr) throws Exception;

	public <T> List<T> queryByExample(T t, Integer pageIndex, Integer pageSize, String orderByStr) throws Exception;

	public <T> DatagridEntity queryByExampleToDatagrid(T t, PageInfo pageInfo) throws Exception;

	public <T> DatagridEntity queryByExampleToDatagrid(T t, PageInfo pageInfo, String orderByStr) throws Exception;

	public <T> DatagridEntity queryByExampleToDatagrid(Map<String, Object> paramMap, Class<T> clz, PageInfo pageInfo)
			throws Exception;

	public <T> DatagridEntity queryByExampleToDatagrid(Map<String, Object> paramMap, Class<T> clz, PageInfo pageInfo,
			String orderByStr) throws Exception;

	public <T> int queryCountByExample(T t) throws Exception;

	/**
	 * 根据map的配置键值对查指定类型的数据
	 */
	public <T> List<T> queryByExample(Map<String, Object> param, Class<T> clz) throws Exception;

	public <T> List<T> queryBySql(String sql, Map<String, Object> paramMap, Class<T> clz) throws Exception;

	public <T> List<T> queryBySql(String sql, Class<T> clz) throws Exception;

	public <T> List<T> queryBySql(Class<?> mapperClass, String id, Map<String, Object> paramMap, Class<T> clz)
			throws Exception;

	public <T> List<T> queryBySql(Class<?> mapperClass, String id, Class<T> clz) throws Exception;

	/**
	 * 查询datagrid列表类型的数据，有规定数据是什么类型
	 */
	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo, Map<String, Object> paramMap,
			Class<T> clz) throws Exception;

	/**
	 * 查询datagrid列表类型的数据，没有规定数据是什么类型，就是map
	 */
	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo, Map<String, Object> paramMap)
			throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo, Class<T> clz) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo,
			ParamEntity paramEntity) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, ParamEntity paramEntity)
			throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, ParamEntity paramEntity,
			Class<?> clz) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(String sql, PageInfo pageInfo) throws Exception;

	public List<Map<String, Object>> queryBySql(String sql, Map<String, Object> paramMap) throws Exception;

	public List<Map<String, Object>> queryBySql(String sql) throws Exception;

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
			throws Exception;

	public List<Map<String, Object>> queryBySql(Class<?> mapperClass, String id) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo,
			Map<String, Object> paramMap, Class<?> clz) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo,
			Map<String, Object> paramMap) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, Map<String, Object> paramMap)
			throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, PageInfo pageInfo, Class<?> clz)
			throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, Map<String, Object> paramMap,
			Class<?> clz) throws Exception;

	public <T> DatagridEntity queryBySqlToDatagrid(Class<?> mapperClass, String id, Class<?> clz) throws Exception;
}
