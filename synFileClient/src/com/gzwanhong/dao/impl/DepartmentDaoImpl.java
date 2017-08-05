package com.gzwanhong.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.DepartmentDao;
import com.gzwanhong.domain.Department;
import com.gzwanhong.domain.User;
import com.gzwanhong.mapper.DepartmentMapper;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Repository
@Scope(value = "prototype")
public class DepartmentDaoImpl extends BaseDaoImpl implements DepartmentDao {
	public List<Department> queryTreegrid(User user) throws Exception {
		List<Department> list = new ArrayList<Department>();

		if ("admin".equals(user.getUserName())) {
			Department example = new Department();
			list = this.queryByExample(example);
		} else {
			Map<String, Object> paramMap = JsonUtil.beanToMap(user);
			list = this.queryBySql(DepartmentMapper.class, "queryTreegrid",
					paramMap, Department.class);
		}

		return list;
	}

	public List<Department> queryComboboxByUser(User user) throws Exception {
		List<Department> list = new ArrayList<Department>();

		String sql = "";
		if (!WhUtil.isEmpty(user) && "admin".equals(user.getUserName())) {
			sql = "select d.* from department d ";
			list = this.queryBySql(sql, Department.class);
		} else {
			sql = "select d.* from department d where FIND_IN_SET(d.id,querySubId('"
					+ user.getDepartmentId() + "','department'))";
		}
		System.out.println(sql);
		list = this.queryBySql(sql, Department.class);

		return list;
	}

	/**
	 * 查找它下级的部门ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<String> querySubDeptId(String id) throws Exception {
		List<String> idList = null;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		List<Map<String, Object>> list = queryBySql(DepartmentMapper.class,
				"querySubDeptId", paramMap);

		if (!WhUtil.isEmpty(list) && list.size() > 0
				&& !WhUtil.isEmpty(list.get(0).get("ids"))) {
			String[] ids = WhUtil.toString(list.get(0).get("ids")).split(",");
			idList = Arrays.asList(ids);
		}

		return idList;
	}

	/**
	 * 查找它下级的部门ID，除了自己部门，就是不返回传进来的这个ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<String> querySubDeptIdNotOwn(String id) throws Exception {
		List<String> idList = null;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		List<Map<String, Object>> list = queryBySql(DepartmentMapper.class,
				"querySubDeptIdNotOwn", paramMap);

		if (!WhUtil.isEmpty(list) && list.size() > 0
				&& !WhUtil.isEmpty(list.get(0).get("ids"))) {
			String[] ids = WhUtil.toString(list.get(0).get("ids")).split(",");
			idList = Arrays.asList(ids);
		}

		return idList;
	}
}
